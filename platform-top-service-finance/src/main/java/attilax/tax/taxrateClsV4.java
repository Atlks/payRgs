package attilax.tax;


import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * v4增加可读性
 */
public class taxrateClsV4 {

    public static void main(String[] args) {


        System.out.println(StringUtils.rightPad("abc", 5) + "after");

        Map input1 = new HashMap() {{
            put("loc地点", "CA");
            put("shoplist物品列表", new ArrayList<Map>() {{
                add(new HashMap() {{
                    put("item物品名", "book");
                    put("qty数量", 1);
                    put("price价格", 17.99);
                }});
                add(new HashMap() {{
                    put("item物品名", "potato chips");
                    put("qty数量", 1);
                    put("price价格", 3.99);
                }});
            }});
        }};
        calcProcsss(input1);

//   calcTax(input1);
        //    System.out.println(gettype("potato chips"));


    System.out.println(roundUP("0.16"));
        System.out.println(roundUP("5.16"));
    }
    @Test
    public  void testInput2()
    {

        Map input1 = new LinkedHashMap() {{
            put("loc地点", "NY");
            put("shoplist物品列表", new ArrayList<Map>() {{
                add(new HashMap() {{
                    put("item物品名", "book");
                    put("qty数量", 1);
                    put("price价格", 17.99);
                }});
                add(new HashMap() {{
                    put("item物品名", "pencil");
                    put("qty数量", 3);
                    put("price价格", 2.99);
                }});
            }});
        }};
        calcProcsss(input1);
        System.out.println(input1);
        System.out.println(JSON.toJSONString(input1,true));
    }


    @Test
    public  void testInput3()
    {

        Map input1 = new LinkedHashMap() {{
            put("loc地点", "NY");
            put("shoplist物品列表", new ArrayList<Map>() {{

                add(new HashMap() {{
                    put("item物品名", "pencil");
                    put("qty数量", 2);
                    put("price价格", 2.99);
                }});
                add(new HashMap() {{
                    put("item物品名", "shirt");
                    put("qty数量", 1);
                    put("price价格", 29.99);
                }});
            }});
        }};


     //   System.out.println(JSON.toJSONString(input1,true));
     //   System.out.println(input1);
        calcProcsss(input1);

    }
    //内存数据表LIST  进行投影运算 循环 获取税率，UDF计算单项税金，并聚合运算 计算总税
    public static void calcProcsss(Map input1) {

        ((List) input1.get("shoplist物品列表")).forEach(new Consumer<Map>() {
            @Override
            public void accept(Map item) {
                Object itemtype = gettype(item.get("item物品名"));
                Map item_taxrate = selectTaxrateFrom_taxRateTable_where_loc_and_itemtype(input1.get("loc地点"), itemtype);
                item.put("itemtype物品类型", itemtype);
                item.put("taxrate税率", item_taxrate.get("tax_rate_num税率数字格式"));
                item.put("taxrate税率文本格式", item_taxrate.get("tax rate税率"));
                item.put("item_tax物品税", get_item_tax((Integer) item.get("qty数量"), (double) item.get("price价格"), item_taxrate.get("tax_rate_num税率数字格式")));
                item.put("item_total物品价格不含税", (double) item.get("price价格") * Double.parseDouble(item.get("qty数量").toString()));
                //     item.put("taxrate_numFormat", Float.parseFloat(item_taxrate) );
            }
        });

        DecimalFormat df2 = new DecimalFormat("##0.00");//这样为保持2位
        double subtotal = ((List<Map>) input1.get("shoplist物品列表")).stream().mapToDouble(i -> (double) i.get("item_total物品价格不含税")).sum();
        input1.put("subtotal物品总价不含税",( df2.format(subtotal + 0)));

        double all_sale_tax = ((List<Map>) input1.get("shoplist物品列表")).stream().mapToDouble(i -> (double) i.get("item_tax物品税")).sum();

        input1.put("tax税",roundUP( df2.format(all_sale_tax )));



        double total = Double.parseDouble(input1.get("tax税").toString()) + Double.parseDouble(input1.get("subtotal物品总价不含税").toString());
        input1.put("total总价含税", df2.format(total));



        formatShow(input1);
    }

    private static Object roundUP(String format) {
        String price_pre=format.substring(0,format.length()-1);
        int lastNum=Integer.parseInt( format.substring(format.length()-1));
        if(lastNum>0 && lastNum<5)
            return price_pre+"5";
        if(lastNum>=5 && lastNum<=9)
        {
            Double d=   Double.parseDouble(price_pre)+0.10d;
            DecimalFormat df2 = new DecimalFormat("##0.00");//这样为保持2位
            return df2.format(d);
        }

        return    format ; // 0 5
    }

    //格式化展示
    private static void formatShow(Map input1) {
        String tabs = "\t\t\t\t";
        int col_len = 22;

        System.out.println("item" + StringUtils.rightPad("", col_len - 4) + "price" + StringUtils.rightPad("", col_len - 5) + "qty");
        ((List) input1.get("shoplist物品列表")).forEach(new Consumer<Map>() {

            @Override
            public void accept(Map map) {

                String itemPad = StringUtils.rightPad("", col_len - map.get("item物品名").toString().length());
                String pricePad = StringUtils.rightPad("", col_len - map.get("price价格").toString().length());
                System.out.println(map.get("item物品名") + itemPad + "$" + map.get("price价格") + pricePad + map.get("qty数量"));
            }
        });
        String tabs2 = tabs + tabs;
        int col_len2 = 40;
        System.out.println("subtotal:" + StringUtils.rightPad("", col_len2 - 9) + "$" + input1.get("subtotal物品总价不含税"));
        System.out.println("tax:" + StringUtils.rightPad("", col_len2 - 4) + "$" + input1.get("tax税"));
        System.out.println("total:" + StringUtils.rightPad("", col_len2 - 6) + "$" + input1.get("total总价含税"));

    }

    //计算单项税务
    private static Object get_item_tax(Integer qty, double price, Object tax_rate_num) {
        if (tax_rate_num == null)
            return 0d;
        return qty * price * (double) tax_rate_num;


    }

    //查询税率    数据表选择运算
    private static Map selectTaxrateFrom_taxRateTable_where_loc_and_itemtype(Object loc, Object itemtype) {


        List<Map> result = taxRateTable.stream().filter(map_item -> {

            return loc.equals(map_item.get("loc地点").toString()) && itemtype.equals(map_item.get("type").toString());
            //    return true;

        }).collect(Collectors.toList());
        return result.get(0);

    }

    //查询物品类型
    private static Object gettype(Object name) {
        List<Map> itemTypetable = new ArrayList() {
            {
                //loc,sale,type(food)
                add(new HashMap() {{
                    put("item物品名", "potato chips");
                    put("type", "food");

                }});
                add(new HashMap() {{
                    put("item物品名", "shirt");
                    put("type", "cloth");

                }});
            }
        };
        List<Map> result = itemTypetable.stream().filter(map_item -> {

            return name.equals(map_item.get("item物品名").toString());
            //    return true;

        }).collect(Collectors.toList());
        if (result.size() == 0)
            return "other";
        return result.get(0).get("type");
    }

    private static List<Map> taxRateTable = new ArrayList() {{
        //loc,sale,type(food)
        add(new HashMap() {{
            put("loc地点", "CA");
            put("type", "other");
            put("tax rate税率", "9.75%");
            put("tax_rate_num税率数字格式", 0.0975);
        }});
        add(new HashMap() {{
            put("loc地点", "CA");
            put("type", "food");
            put("tax rate税率", "0");
        }});

        add(new HashMap() {{
            put("loc地点", "NY");
            put("type", "other");
            put("tax rate税率", " 8.875%");
            put("tax_rate_num税率数字格式", 0.08875);
        }});
        add(new HashMap() {{
            put("loc地点", "NY");
            put("type", "food");
            put("tax rate税率", "0");
        }});
        add(new HashMap() {{
            put("loc地点", "NY");
            put("type", "cloth");
            put("tax rate税率", "0");
        }});

    }};

}
