package org.chwin.firefighting.apiserver.QL;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.SelectUtils;

import java.util.ArrayList;

public class SqlParseT {
    @SneakyThrows
    public static void main(String[] args) {
        String sql = "select sum(cnt) as summC1,*,udf(c22) from tab where c1=123 and c2=456 order by c1 desc limit 1,10";
        //    sql = "select * from tab where c1=123    group by c1 ";
   //     sql = " select * from tab where c like '%col_like%' group by c having cc>=12";
        System.out.println("");
        sql="select * from t limit 5";
        sql="set @t=5;update t set c=1,c2=2";
        sql="set @t=5;";
        sql="select * from t  order by c,d desc limit 5";
//        sql="delete t where id=1 ";
//        sql="select a,b from t limit 5";
        Statement Statement1 = CCJSqlParserUtil.parse(sql);
        Select select = (Select) Statement1;
        //    System.out.println(JSON.toJSONString(select,true));


        Select select2 = SelectUtils.buildSelectFromTable(new Table("没啥用aa"));

        //   EqualsTo EqualsToExp=new EqualsTo();


        select2.setSelectBody(new PlainSelect() {{

                                  setSelectItems(new ArrayList<SelectItem>() {{
                                      add(new AllColumns());
                                      add(new SelectExpressionItem() {{
                                          setExpression(new Function() {{
                                              setName("sum");
                                              setParameters(new ExpressionList() {{
                                                  setExpressions(new ArrayList() {{
                                                      add(new Column("cnt"));
                                                  }});
                                              }});

                                          }});
                                          setAlias(new Alias("cntAlias1"));
                                      }});


                                      add(new SelectExpressionItem() {{
                                          setExpression(new Function() {{
                                              setName("udf");
                                              setParameters(new ExpressionList() {{
                                                  setExpressions(new ArrayList() {{
                                                      add(new Column("udfcol"));
                                                  }});
                                              }});

                                          }});
                                          //      setAlias( new Alias("cntAlias1"));
                                      }});
                                  }});
                                  setFromItem(new Table("TABLE1"));
                                  setWhere(
                                          new AndExpression(new EqualsTo() {{
                                              setLeftExpression(new Column("c1"));
                                              setRightExpression(new StringValue("aaa"));
                                          }}, new LikeExpression() {{
                                              setLeftExpression(new Column("c"));

                                              setRightExpression(new StringValue("aaaa") {{
                                                  setValue("%clike%");
                                              }});
                                          }})
                                  );
                                  setOrderByElements(new ArrayList<OrderByElement>() {{
                                      add(new OrderByElement() {{
                                          setExpression(new Column("c1"));
                                          setAsc(true);
                                          setAscDescPresent(false);
                                      }});
                                  }});
                                  setLimit(new Limit() {{
                                      setOffset(7);
                                      setRowCount(10);
                                  }});

                                  setGroupByColumnReferences(new ArrayList() {{
                                      add(new Column("c1"));
                                  }});
                              }}


        );


        //   SelectUtils.addExpression(select2,EqualsToExp);

        System.out.println(select2.toString());//SELECT * FROM TABLE1


//        Select select_where_and = SelectUtils.buildSelectFromTable(new Table("没啥用"));
//
//        select_where_and.setSelectBody(new PlainSelect() {{
//
//                                           setSelectItems(new ArrayList() {{
//                                               add("*");
//                                           }});
//                                           setFromItem(new Table("TABLE1"));
//                                           setWhere(
//                                                   new AndExpression(new EqualsTo() {{
//                                                       setLeftExpression(new Column("c1"));
//                                                       setRightExpression(new StringValue("aaa"));
//                                                   }}, new EqualsTo() {{
//                                                       setLeftExpression(new Column("c2"));
//                                                       setRightExpression(new LongValue(789));
//                                                   }}) {{
//
//                                                   }}
//                                           );
//                                       }}
//
//
//        );
//        System.out.println(select_where_and.toString());//SELECT * FROM TABLE1
        System.out.println("--f");
    }
}
