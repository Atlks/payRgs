//package org.chwin.firefighting.apiserver.QL;
//
//import antlr.collections.AST;
//import com.google.common.collect.Lists;
//import lombok.extern.slf4j.Slf4j;
//import net.sf.jsqlparser.expression.Alias;
//import net.sf.jsqlparser.expression.Expression;
//import net.sf.jsqlparser.expression.Function;
//import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
//import net.sf.jsqlparser.schema.Column;
//import net.sf.jsqlparser.schema.Table;
//import net.sf.jsqlparser.statement.select.*;
//import net.sf.jsqlparser.util.SelectUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Stack;
//
//@Slf4j
//public class JqplAst2SqlAstConverterSelectbek extends JqplAst2SqlAstConverter {
//    public Select select_where_and = SelectUtils.buildSelectFromTable(new Table("没啥用"));
//
//    public PlainSelect PlainSelect1;//= (PlainSelect) select_where_and.getSelectBody();
//
//
//    public List<SelectExpressionItem> selectList_SelectExpressionItem = Lists.newArrayList();
//    public Object WhereElement;
//
//    //  public Stack Stack1 = new Stack();
//
//    public JqplAst2SqlAstConverterSelectbek() {
//
//
//        PlainSelect body = new PlainSelect() {{
//
//            setSelectItems(new ArrayList());
//            // setFromItem(new Table("TABLE1"));
////                                           setWhere(
////                                                   new AndExpression(new EqualsTo() {{
////                                                       setLeftExpression(new Column("c1"));
////                                                       setRightExpression(new StringValue("aaa"));
////                                                   }}, new EqualsTo() {{
////                                                       setLeftExpression(new Column("c2"));
////                                                       setRightExpression(new LongValue(789));
////                                                   }}) {{
////
////                                                   }}
////                                           );
//        }};
//
//        PlainSelect1 = body;
//        select_where_and.setSelectBody(body);
//    }
////
////    public static String Jqpl2sql(AST hqlAst) {
////
////        //hqlParser1.showAst(hqlAst,System.out);
////        ASTPrinterAti astPrinterAti = new ASTPrinterAti(HqlTokenTypes.class);
////        astPrinterAti.showAst(hqlAst, System.out);
////        return astPrinterAti.jqplAst2SqlAstConverter1.select_where_and.toString();
////    }
//
//
//    public void ini() {
//    }
//
//
//    public void processByTokentypenameAndTxt(String tokenTypeName, String text, AST nextSibling) {
//        log.info("now token is ::" + tokenTypeName + "  :  " + text);
//
//        if (text == "表格1")
//            System.out.println("add");
//        if (text == "a")
//            System.out.println("add");
//
//
//      //      super.processByTokentypenameAndTxt(tokenTypeName, text, nextSibling);
//
//        if (tokenTypeName.equals("AGGREGATE")) {   //sum()
//
//            fun_AGGREGATe_fun(text);
//
//
//        }
//
//
//        if (tokenTypeName.equals("IDENT")) {
//            iden_evt(text, nextSibling);
//            return;
//
//        }
//
//        //set equ right exp
////        if (tokenTypeName.equals("QUOTED_STRING")) {
////            if (val_QuotedString_evt(text)) return;
////
////
////        }
////        if (tokenTypeName.equals("NUM_INT")) {
////            valProcess_NUM_INT__evt(text);
////            return;
////
////        }
//    }
//
////    private boolean val_QuotedString_evt(String text) {
////        Object obj = Stack1.pop();
////        if (obj instanceof EqualsTo) // parent is And exp
////        {
////            EqualsTo Expression1 = (EqualsTo) obj;
////            Expression1.setRightExpression(new StringValue(text));
////            //not push back
////        }
////
//////
//////        if (obj instanceof LikeExpression) {
//////            LikeExpression LikeExpression1 = (LikeExpression) obj;
//////            LikeExpression1.setRightExpression(new StringValue("aaaa") {{
//////                setValue(text);
//////            }});
//////            //   Stack1.push(obj);
//////            return true;
//////        }
////        return false;
////    }
//
////    public void valProcess_NUM_INT__evt(String text) {
////        Object obj_last = Stack1.pop();   //ge
////
////
////        if (obj_last instanceof EqualsTo) // parent is And exp
////        {
////            EqualsTo Expression1 = (EqualsTo) obj_last;
////            Expression1.setRightExpression(new LongValue(text));
////
////            //not push back
////        }
////
////        if (obj_last instanceof BinaryExpression) // parent is  = > < !=  >=  <=
////        {
////            BinaryExpression Expression1 = (BinaryExpression) obj_last;
////            Expression1.setRightExpression(new LongValue(text));
////
////            //not push back
////        }
////    }
//
//
//    public void processTokenTypename(String tokenTypeName) {
//
//        log.info("now token is ::" + tokenTypeName);
//        if (tokenTypeName == "SELECT")
//            System.out.println("");
//
//
//            super.processTokenTypename(tokenTypeName);
//
//
//
//        if (tokenTypeName.equals("SELECT")) {
//            ArrayList selectListE = Lists.newArrayList();
//
//
//            PlainSelect1.setSelectItems(selectListE);
//            Stack1.push(PlainSelect1);
//            selectList_SelectExpressionItem = selectListE;
//
//
//            Stack1.push("SELECT");
//            return;
//
//        }
////
////        if (tokenTypeName.equals("LIKE")) {
////            Object obj = Stack1.pop();
////            if (obj instanceof AndExpression) {
////
////                LikeExpression LikeExpression1 = new LikeExpression();
////
////                setAndExp(obj, LikeExpression1);
////                ;
////                Stack1.push(obj);  //paret and
////                Stack1.push(LikeExpression1);  //paret and
////                return;
////            }
////
////
////            return;
////        }
//
////
//////
////        if (tokenTypeName.equals("SELECT")) {
////
////
////            Stack1.push("SELECT");
////            select
////            return;
////
////        }
//
//
////        if (tokenTypeName.equals("GE")) {
////
////            Object o = Stack1.pop();
////
////            if (o == "HavingExp") {
////                GreaterThanEquals gte = new GreaterThanEquals();
////                PlainSelect PlainSelect1 = getPlainSelectFromStack(Stack1);
////
////                PlainSelect1.setHaving(gte);
////                Stack1.push(PlainSelect1);
////                Stack1.push(gte);
////            }
////        }
//
//
//        if (tokenTypeName.equals("AS")) {   //sum()
//
//            as_fun();
//
//        }
//        if (tokenTypeName.equals("HAVING")) {   //sum()
//
//            HAVING_fun();
//
//        }
//
//
////
////        if (tokenTypeName.equals("COUNT")) {   //sum()
////
////            SelectExpressionItem asItem = (SelectExpressionItem) Stack1.pop();
////            Function fun1 = new Function() {{
////                setName("count");
////
////            }};
////            asItem.setExpression(fun1);
////
////            Stack1.push(asItem);
////            Stack1.push(fun1);
////
////
////        }
//
//
//        if (tokenTypeName.equals("ROW_STAR")) {   //sum()
//
//            Function fun1 = (Function) Stack1.pop();
//
//            fun1.setParameters(new ExpressionList() {{
//                setExpressions(new ArrayList() {{
//                    add(new Column("*"));
//                }});
//            }});
//
//            //     Stack1.push(asItem);
//            //     Stack1.push(fun1);
//
//
//        }
//
////        if (tokenTypeName.equals("QUERY")) {
////
////            Stack1.<PlainSelect>push((PlainSelect) select_where_and.getSelectBody());
////            PlainSelect1 = (PlainSelect) select_where_and.getSelectBody();
////        }
//
////
////        if (tokenTypeName.equals("FROM")) {
//////            curNodeNameStat = "FROM";
//////            Stack1.<PlainSelect>push((PlainSelect) select_where_and.getSelectBody());
////            Object obj = Stack1.pop();
////
////
////            if (obj instanceof Update) {
////
////            } else//sele
////            {
////                Table $tab = new Table("$tab");
////                PlainSelect1.setFromItem($tab);
////
////                Stack1.push($tab);
////            }
////
////        }
//
//
////
////        if (tokenTypeName.equals("EQ")) {
////            eq_evt();
////
////
////        }
////
//
//        if (tokenTypeName.equals("ORDER")) {
//
//            ORDER_NodeProcessor();
//
//
//        }
//
//        if (tokenTypeName.equals("DESCENDING")) {
//
//            orderby_DESCENDING_NodeProcessor();
//
//
//        }
//
//        if (tokenTypeName.equals("GROUP")) {
//            Group_fun();
//
//
//        }
//
//
//    }
////
////    private void setAndExp(Object obj, Expression exp) {
////
////        AndExpression AndExpression1 = (AndExpression) obj;
////        if (AndExpression1.getLeftExpression() != null && AndExpression1.getRightExpression() != null) {
////            obj = Stack1.pop();
////            AndExpression1 = (AndExpression) obj;
////        }
////
////        if (AndExpression1.getLeftExpression() == null) {
////            AndExpression1.setLeftExpression(exp);
////        } else if (AndExpression1.getRightExpression() == null)
////            AndExpression1.setRightExpression(exp);
////        else
////            throw new RuntimeException("err");
////    }
//
//    private void HAVING_fun() {
//
//        PlainSelect PlainSelect1 = getPlainSelectFromStack(Stack1);
//        //  PlainSelect1.setHaving( new HavingExp());
//        Stack1.push(PlainSelect1);
//        Stack1.push("HavingExp");
//    }
//
//    private void Group_fun() {
////        PlainSelect PlainSelect1 = getPlainSelectFromStack(Stack1);
////        GroupByColumnReferencesAti gbca = new GroupByColumnReferencesAti();
////        gbca.setGroupByColumnReferences(new ArrayList<>());
////        Stack1.push(PlainSelect1);
////        Stack1.push(gbca);
//        //   orderby_DESCENDING_NodeProcessor();
//    }
//
//    private void as_fun() {
//        List<SelectItem> List_SelectItem1 = (List<SelectItem>) Stack1.pop();
//        SelectExpressionItem asItem = new SelectExpressionItem();
//
//        //{{
////                setExpression(new Function() {{
////                    setName("sum");
////                    setParameters(new ExpressionList() {{
////                        setExpressions(new ArrayList() {{
////                            add(new Column("cnt"));
////                        }});
////                    }});
////
////                }});
//
//        //     }};
//
//        asItem.setAlias(new Alias("$AsSelectExpressionItem"));
//
//        List_SelectItem1.add(asItem);
//        Stack1.push(List_SelectItem1);
//        Stack1.push(asItem);
//    }
//
//    private void fun_AGGREGATe_fun(String text) {
//        final String fun_name = text;
//        super.fun_process(fun_name);
//    }
////
////    private void fun_process(String fun_name) {
////        Object obj = Stack1.pop();
////        if (fun_name.equals("("))
////            System.out.println("dbg");
////        if (obj instanceof List) // pop is  selectElem
////        {
////            List curE = (List) obj;
////            Function fun1 = new Function();
////            fun1.setName(fun_name);
////
////            curE.add(fun1);
////            Stack1.push(curE); //list   selectElem
////            Stack1.push(fun1);
////        } else if (obj instanceof SelectExpressionItem) {  // par is Asitem
////            //   obj.getClass()
////            SelectExpressionItem asItem = (SelectExpressionItem) obj;
////            Function fun1 = new Function();
////            fun1.setName(fun_name);
////            asItem.setExpression(fun1);
////            Stack1.push(asItem);
////            Stack1.push(fun1);
////        } else {
////            if (obj instanceof BinaryExpression) {
////                BinaryExpression BinaryExpression1 = (BinaryExpression) obj;
////                Function fun1 = new Function();
////                fun1.setName(fun_name);
////                BinaryExpression1.setLeftExpression(fun1);
////                Stack1.push(obj);  //ge
////                Stack1.push(fun1); //fun AGGREGATE
////            }
////
////
////        }
////    }
//
//    private void orderby_DESCENDING_NodeProcessor() {
//        Object obj = Stack1.pop();
//        if (obj instanceof OrderByElement) // pop is OrderByElement
//        {
//            OrderByElement curE = (OrderByElement) obj;
//            curE.setAscDescPresent(true);
//            curE.setAsc(false);
//            Stack1.push(curE);
//        }
//    }
//
//    private void ORDER_NodeProcessor() {
//
//        OrderByElement OrderByElement1 = new OrderByElement() {{
//            //    setExpression(new Column("c1"));
//            //   setAsc(true);
//            //    setAscDescPresent(false);
//        }};
//
//
//        //   PlainSelect PlainSelect1 = getPlainSelectFromStack(Stack1);
//        PlainSelect1.setOrderByElements(new ArrayList<OrderByElement>() {{
//            add(OrderByElement1);
//        }});
//
//        Stack1.push(PlainSelect1);
//        Stack1.push(OrderByElement1);
//
//
//    }
//
//    private PlainSelect getPlainSelectFromStack(Stack stack1) {
//        while (true) {
//
//
//            Object obj = Stack1.pop();
//
//            if (obj instanceof PlainSelect) //where
//                return (PlainSelect) obj;
//        }
//    }
//
////    public void and_evt() {
////        Object obj = Stack1.pop();
////
////
////        if (obj instanceof PlainSelect) //where
////        {
////
////            AndExpression newAddExp = new AndExpression(null, null);
////
////            PlainSelect1.setWhere(newAddExp);
////            Stack1.push(newAddExp);
////
////        }
////
////        if (obj instanceof AndExpression) //and exp
////        {
////            AndExpression AndExpression1_FromStatck = (AndExpression) obj;
////            AndExpression newAddExp = new AndExpression(null, null);
////
////            AndExpression1_FromStatck.setLeftExpression(newAddExp);
////            Stack1.push(AndExpression1_FromStatck);
////            Stack1.push(newAddExp);
////
////        }
////    }
//
//    @Override
//    public void setWhere(Expression newAddExp) {
//        ((PlainSelect) select_where_and.getSelectBody()).setWhere(newAddExp);
//    }
//
////    public void eq_evt() {
////        log.info(select_where_and.toString());
////
////        super.eq_evt();
////
////        EqualsTo EqualsTo1 = new EqualsTo();
////        Object obj = Stack1.pop();
////
////        if (obj.equals("WHERE")) {
////
////
////
////            //    Stack1.push(EqualsTo1);
////            //   UpdateStatment1.getTables().add(new Column())
////
////            return;
////        }
////
////
////
////        if (obj instanceof PlainSelect) //where
////        {
////            PlainSelect PlainSelect1 = (PlainSelect) obj;
////
////            PlainSelect1.setWhere(EqualsTo1);
////            Stack1.push(EqualsTo1);
////
////        }
////
////        if (obj instanceof AndExpression) // parent is And exp  &&eq
////        {
////            AndExpression AndExpression1 = (AndExpression) obj;
////            if (AndExpression1.getLeftExpression() != null && AndExpression1.getRightExpression() != null) {
////                obj = Stack1.pop();
////                AndExpression1 = (AndExpression) obj;
////            }
////
////            if (AndExpression1.getLeftExpression() == null) {
////                AndExpression1.setLeftExpression(EqualsTo1);
////            } else if (AndExpression1.getRightExpression() == null)
////                AndExpression1.setRightExpression(EqualsTo1);
////            else
////                throw new RuntimeException("err");
////
////
////            Stack1.push(obj);  //paret and
////            Stack1.push(EqualsTo1);
////        }
////    }
//
//
//    public void iden_evt(String text, AST nextSibling) {
//
//
//
//            super.iden_evt(text, nextSibling);
//
//
//        Object obj = obj_poped;
//        if (text == "a")
//            System.out.println("D");
////
////        if (obj instanceof LikeExpression) {
////            LikeExpression LikeExpression1 = (LikeExpression) obj;
////            LikeExpression1.setLeftExpression(new Column(text));
////            Stack1.push(obj);
////            return;
////        }
//
//
////        if (obj instanceof "GroupByColumnReferencesAti".getClass() ) //  EXPR_LIST
////        {
////            //  GroupByColumnReferencesAti GroupByColumnReferencesAti1 = (GroupByColumnReferencesAti) obj;
////
////            ArrayList list = new ArrayList();
////            list.add(new Column(text));
////
////            PlainSelect1.setGroupByColumnReferences(list);
////
////            return;
////
////            //   Stack1.push(obj);
////        }
//
////        if (obj instanceof ExpressionList) //  EXPR_LIST
////        {
////            ExpressionList ExpressionList1 = (ExpressionList) obj;
////            ExpressionList1.getExpressions().add(new Column(text));
////            if (nextSibling == null)
////                Stack1.pop();  //pop  METHOD_CALL ele
////
////            return;
////
////            //   Stack1.push(obj);
////        }
//
////
////        if (is$AsSelectExpressionItem(obj)) {
////
////            SelectExpressionItem asItem = (SelectExpressionItem) obj;
////            asItem.setAlias(new Alias(text));
////            return;
////            //   Stack1.push(obj);
////        }
////
////        if (fun_isMETHOD_CALL(obj)) {
////            Function fun = (Function) obj;
////            fun.setName(text);
////            Stack1.push(fun);
////            return;
////        }
//
//
////        if (obj instanceof Function) //
////        {
////            Function fun = (Function) obj;
////            ExpressionList ExpressionList1 = new ExpressionList();
////
////
////            ExpressionList1.setExpressions(new ArrayList() {{
////                add(new Column(text));
////            }});
////
////            fun.setParameters(ExpressionList1);
////
////            //   Stack1.push(obj);
////        }
//
////  obj instanceof List<SelectExpressionItem>
//        if (obj == "SELECT") // select list
//        {
//            //  SelectExpressionItem
//            SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
//            selectExpressionItem.setExpression(new Column(text));
//            selectList_SelectExpressionItem.add(selectExpressionItem);
//            //     li.add(text);
//            Stack1.push(obj);
//        }
//
////        if (obj instanceof FromItem) //from FromItem_table
////        {
////            if (obj instanceof Table) {
////                ((Table) obj).setName(text);
////            }
////            //   Stack1.push(PlainSelect1);
////            return;
////        }
//
////        if (obj instanceof PlainSelect) //from statment
////        {
////            PlainSelect PlainSelect1 = (PlainSelect) select_where_and.getSelectBody();
////            PlainSelect1.setFromItem(new Table(text));
////            Stack1.push(PlainSelect1);
////        }
//
////        if (obj instanceof EqualsTo) // parent is And exp
////        {
////            EqualsTo EqualsTo1 = (EqualsTo) obj;
////            EqualsTo1.setLeftExpression(new Column(text));
////            Stack1.push(EqualsTo1);
////        }
//
//        if (obj instanceof OrderByElement) // pop is OrderByElement
//        {
//            OrderByElement curE = (OrderByElement) obj;
//            curE.setExpression(new Column(text));
//            Stack1.push(curE);
//        }
//
//
//    }
//
//    @Override
//    protected void setFromTable(String text) {
//        PlainSelect1.setFromItem(new Table(text));
//    }
////
////    private boolean is$AsSelectExpressionItem(Object obj) {
////
////        if (obj instanceof SelectExpressionItem) //
////        {
////            SelectExpressionItem SelectExpressionItem1 = (SelectExpressionItem) obj;
////            return SelectExpressionItem1.getAlias().getName().equals("$AsSelectExpressionItem");
////        }
////        return false;
////    }
////
////    private boolean fun_isMETHOD_CALL(Object obj) {
////        if (obj instanceof Function) {
////            Function f = (Function) obj;
////            return f.getName().equals("(");
////
////        } else
////            return false;
////    }
//
//    public String toSql() {
//
//
//        return select_where_and.toString();
//    }
//}
