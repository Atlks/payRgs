package org.chwin.firefighting.apiserver.QL;

import lombok.SneakyThrows;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.SelectUtils;
import org.apache.ibatis.annotations.SelectKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

public abstract class atiql2sqlAstConverterBase {
    PlainSelect PlainSelect1 = new PlainSelect();
    private Function atiqlAst1;

    atiql2sqlAstConverterBase() {
        if (PlainSelect1.getSelectItems() == null)
            PlainSelect1.setSelectItems(new ArrayList<>());
        PlainSelect1.setFromItem(new Table("tttt"));
        select_where_and.setSelectBody(PlainSelect1);
    }

    public static void main(String[] args) {
//        String s = "查询表格(操作日志表),条件( 操作人=小新),and(时间=2018),查询字段(操作人,时间,事件),排序(时间),翻页(页数=1,每页=20)";
//        s = "查询表格(操作日志表),条件( 操作人=小新),and(时间=2018),and(事件=登录事件),排序(时间),翻页(页数=7,每页=50)";
//        Function fun = astiqlAstBuilder.atiql2ast(s);
//        System.out.println(fun);
//        Statement stt = new atiql2sqlAstConverterBase().toSqlAst(fun);
//        System.out.println(stt.toString());
    }

    public Select select_where_and = SelectUtils.buildSelectFromTable(new Table("没啥用"));

//    private Statement toSqlAst(Function atiqlAst) {
//
//        traveTree(atiqlAst);
//        atiql2sqlAstConverterBase c = new atiql2sqlAstConverterBase();
//        String table = traveNodes(atiqlAst);
//
////        PlainSelect ps=  new PlainSelect();
////        ps.    setFromItem(new Table(table));
//        //     select_where_and.setSelectBody(ps );
//
//        and_processFinaly();
//
//
//        return select_where_and;
//    }

    public void traveTree(Expression atiqlAstTmp) {
        Consumer<Function> Consumer1 = new Consumer<Function>() {
            @Override
            public void accept(Function f) {

                if (f.getName().equals("条件")) {
                    where_evt(f, null);
                    return;

                }


                if (f.getName().equals("and")) {
                    and_evt(f, null);
                    return;
                }

                traveTree_subprocess(f);
            }
        };

        SqlUtil.traveTree(atiqlAstTmp, Consumer1);

    }


    public abstract void traveTree_subprocess(Function f);


//    private void traveTree(Expression atiqlAstTmp) {
//
//        System.out.println("travetree:" + atiqlAstTmp.toString());
//        if (atiqlAstTmp instanceof Function) {
//            Function Function1 = (Function) atiqlAstTmp;
//            List<Expression> list = Function1.getParameters().getExpressions();
//            if (list.size() > 0) { // multiChildNodes
//                for (Expression node : list) {
//                    traveTree(node);
//                }
//            }
//        }
//
//    }

    List<Expression> andList = new ArrayList<>();

    String tab = "";

    private String traveNodes(Function atiqlAst) {
        atiqlAst1 = atiqlAst;
        List li = atiqlAst.getParameters().getExpressions();


        li.forEach(new Consumer<Expression>() {
            @Override
            public void accept(Expression o) {
                if (o instanceof Function) {
                    Function f = (Function) o;
                    if (f.getName().equals("查询表格")) {
                        StringValue expression = (StringValue) f.getParameters().getExpressions().get(0);
                        tab = expression.getValue();
                        PlainSelect1.setFromItem(new Table(tab));
                    }

                    if (f.getName().equals("条件")) {
                        where_evt(f, li);
                        return;
                    }
                    if (f.getName().equals("排序")) {
                        order_evt(f, li);
                        return;
                    }
                    if (f.getName().equals("翻页")) {
                        page_evt(f);
                        return;
                    }

                    if (f.getName().equals("and")) {
                        and_evt(f, li);
                        return;
                    }
                    if (f.getName().equals("查询字段")) {
                        select_evt(f);
                        return;
                    }


                }

            }
        });
        checkSelectFieldAll_evt();
        return tab;
    }

    private void checkSelectFieldAll_evt() {
        if (!hasSeleteElemt(atiqlAst1)) {
            PlainSelect1.getSelectItems().add(new AllColumns());
        }

    }

    private void select_evt(Function f) {

//        SelectExpressionItem sei=   new SelectExpressionItem() {{
//            setExpression(new Function() {{
//                setName("sum");
//                setParameters(new ExpressionList() {{
//                    setExpressions(new ArrayList() {{
//                        add(new Column("cnt"));
//                    }});
//                }});
//
//            }});
//            setAlias(new Alias("cntAlias1"));
//        }}


        List<Expression> li = f.getParameters().getExpressions();
        if (li == null || li.size() == 0) {


            PlainSelect1.getSelectItems().add(new AllColumns());
            return;
        }

        li.forEach(new Consumer<Expression>() {
            @Override
            public void accept(Expression e) {
                StringValue sv = (StringValue) e;
                SelectExpressionItem sei = new SelectExpressionItem() {{
                    setExpression(new Column(sv.getValue()));

                }};

                PlainSelect1.getSelectItems().add(sei);

            }
        });
        System.out.println(f);

    }

    Stack<Expression> stack = new Stack();

    public void and_evt(Function f, List li) {


        StringValue expression = (StringValue) f.getParameters().getExpressions().get(0);
        String whs = expression.getValue();
        String[] kv = whs.split("=");
        EqualsTo et = new EqualsTo();
        et.setLeftExpression(new Column(kv[0]));
        et.setRightExpression(SqlUtil.newtStringValue(kv[1]));
        andList.add(et);


    }

    public void and_processFinaly() {
        if (andList.size() == 1) {
            PlainSelect1.setWhere(andList.get(0));
        } else {
            and_reduce();

            PlainSelect1.setWhere(stack.pop());
        }
    }

    public void and_reduce() {
        AndExpression aexx = new AndExpression(null, null);
        stack.push(aexx);
        andList.forEach(new Consumer<Expression>() {
            @Override
            public void accept(Expression expression) {
                Expression e_pop = stack.pop();
                if (e_pop instanceof AndExpression) {
                    AndExpression ae_pop = (AndExpression) e_pop;
                    if (ae_pop.getLeftExpression() == null) {
                        ae_pop.setLeftExpression(expression);
                        stack.push(ae_pop);
                    } else if (ae_pop.getRightExpression() == null) {
                        ae_pop.setRightExpression(expression);
                        stack.push(ae_pop);
                    } else {
                        AndExpression newes = new AndExpression(null, null);
                        newes.setLeftExpression(ae_pop);
                        newes.setRightExpression(expression);
                        stack.push(newes);
                    }
                }

            }
        });
    }

    Limit lmt = new Limit();

    private void page_evt(Function f) {
        // List<Exception> li=f.getParameters().getExpressions();
        List li;

        li = f.getParameters().getExpressions();
        StringValue o = (StringValue) li.get(0);
        String page = o.getValue();
        String page_str = page.split("=")[1];
        int page_int = Integer.parseInt(page_str);

        StringValue o1 = (StringValue) li.get(1);
        String pagesize = o1.getValue();
        int pagesize_int = Integer.parseInt(pagesize.split("=")[1]);
        int offset = pagesize_int * (page_int - 1);

        lmt.setOffset(offset);
        lmt.setRowCount(pagesize_int);
        PlainSelect1.setLimit(lmt);

    }

    private void order_evt(Function f, List li) {
        StringValue expression = (StringValue) f.getParameters().getExpressions().get(0);
        String whs = expression.getValue();

        OrderByElement OrderByElement1 = new OrderByElement() {{
            setExpression(new Column(whs));
            setAsc(true);
            setAscDescPresent(false);
        }};

        ArrayList li2 = new ArrayList<OrderByElement>();
        li2.add(OrderByElement1);
        PlainSelect1.setOrderByElements(li2);
    }

    public void where_evt(Function f, List li) {
        StringValue expression = (StringValue) f.getParameters().getExpressions().get(0);
        String whs = expression.getValue();
        String[] kv = whs.split("=");
        EqualsTo et = new EqualsTo();
        et.setLeftExpression(new Column(kv[0]));
        et.setRightExpression(SqlUtil.newtStringValue(kv[1]));
        andList.add(et);

    }

    boolean hasAndConditon = false;

    private boolean hasAndConditon(List li) {
        boolean b = false;
        li.forEach(new Consumer<Expression>() {
            @Override
            public void accept(Expression o) {
                if (o instanceof Function) {
                    Function f = (Function) o;

                    if (f.getName() == "and") {
                        hasAndConditon = true;
                    }
                }

            }
        });
        return hasAndConditon;
    }


    private boolean hasSeleteElemt(Function atiqlAst1) {
        List li = atiqlAst1.getParameters().getExpressions();

        boolean b = false;
        li.forEach(new Consumer<Expression>() {
            @Override
            public void accept(Expression o) {
                if (o instanceof Function) {
                    Function f = (Function) o;

                    if (f.getName() == "查询字段") {
                        hasAndConditon = true;
                    }
                }

            }
        });
        return hasAndConditon;
    }


}
