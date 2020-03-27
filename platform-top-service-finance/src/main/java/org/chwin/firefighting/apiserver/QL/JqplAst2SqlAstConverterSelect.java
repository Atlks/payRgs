package org.chwin.firefighting.apiserver.QL;

import antlr.collections.AST;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.SelectUtils;
import org.apache.poi.util.SystemOutLogger;
import org.hibernate.hql.internal.antlr.HqlTokenTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Slf4j
public class JqplAst2SqlAstConverterSelect extends JqplAst2SqlAstConverter {
    public Select select_where_and = SelectUtils.buildSelectFromTable(new Table("没啥用"));

    public PlainSelect PlainSelect1;//= (PlainSelect) select_where_and.getSelectBody();


    public List<SelectExpressionItem> selectList_SelectExpressionItem = Lists.newArrayList();
    public Object WhereElement;
    private ArrayList<Expression> GroupByColumnReferencesList;

    //  public Stack Stack1 = new Stack();

    public JqplAst2SqlAstConverterSelect() {


        PlainSelect body = new PlainSelect() {{

            setSelectItems(new ArrayList());

        }};

        PlainSelect1 = body;
        select_where_and.setSelectBody(body);
    }


    public void ini() {
    }

    Logger log = LoggerFactory.getLogger(atiQlTokiz.class);
    public void processByTokentypenameAndTxt_QUD_perTypeSttEspePart(String tokenTypeName, String text, AST nextSibling) {
        log.info("now token is ::" + tokenTypeName + "  :  " + text);

        if (text == "表格1")
            System.out.println("add");
        if (text == "a")
            System.out.println("add");


        //     super.processByTokentypenameAndTxt(tokenTypeName, text, nextSibling);

//        if (tokenTypeName.equals("AGGREGATE")) {   //sum()
//
//            fun_AGGREGATe_fun(text);
//
//            return;
//        }

//
//        if (tokenTypeName.equals("IDENT")) {
//            iden_evt(text, nextSibling);
//            return;
//
//        }


    }

   // Logger log = LoggerFactory.getLogger(atiQlTokiz.class);
    public void processTokenTypename_espPart(String tokenTypeName) {

        log.info("now token is ::" + tokenTypeName);
        if (tokenTypeName == "SELECT")
            System.out.println("");


        //   super.processTokenTypename(tokenTypeName);


        if (tokenTypeName.equals("SELECT")) {
            ArrayList selectListE = Lists.newArrayList();


            PlainSelect1.setSelectItems(selectListE);
            Stack1.push(PlainSelect1);
            selectList_SelectExpressionItem = selectListE;


            Stack1.push("SELECT");
            return;

        }


        if (tokenTypeName.equals("AS")) {   //sum()

            as_fun();
            return;

        }
        if (tokenTypeName.equals("HAVING")) {   //sum()

            HAVING_fun();
            return;

        }


        if (tokenTypeName.equals("ROW_STAR")) {   //sum()

            Function fun1 = (Function) Stack1.pop();

            fun1.setParameters(new ExpressionList() {{
                setExpressions(new ArrayList() {{
                    add(new Column("*"));
                }});
            }});
            return;

        }


        if (tokenTypeName.equals("ORDER")) {

            ORDER_NodeProcessor();

            return;
        }

        if (tokenTypeName.equals("DESCENDING")) {

            orderby_DESCENDING_NodeProcessor();

            return;
        }

        if (tokenTypeName.equals("GROUP")) {
            Group_fun();

            return;
        }


    }


    private void HAVING_fun() {

        //   PlainSelect PlainSelect1 = getPlainSelectFromStack(Stack1);
        //  PlainSelect1.setHaving( new HavingExp());
        Stack1.push(PlainSelect1);
        Stack1.push("HavingExp");
    }

    private void Group_fun() {
        //   PlainSelect PlainSelect1 = getPlainSelectFromStack(Stack1);
        GroupByColumnReferencesList = (new ArrayList<>());
        PlainSelect1.setGroupByColumnReferences(GroupByColumnReferencesList);
        Stack1.push(PlainSelect1);
        Stack1.push("GroupByColumnReferencesAti");
        //   orderby_DESCENDING_NodeProcessor();
    }

    private void as_fun() {
        List<SelectItem> List_SelectItem1 = (List<SelectItem>) Stack1.pop();
        SelectExpressionItem asItem = new SelectExpressionItem();


        asItem.setAlias(new Alias("$AsSelectExpressionItem"));

        List_SelectItem1.add(asItem);
        Stack1.push(List_SelectItem1);
        Stack1.push(asItem);
    }

//    private void fun_AGGREGATe_fun(String text) {
//        final String fun_name = text;
//        super.fun_process(fun_name);
//    }


    private void orderby_DESCENDING_NodeProcessor() {
        Object obj = Stack1.pop();
        if (obj instanceof OrderByElement) // pop is OrderByElement
        {
            OrderByElement curE = (OrderByElement) obj;
            curE.setAscDescPresent(true);
            curE.setAsc(false);
            Stack1.push(curE);
            return;
        }
    }

    ArrayList<OrderByElement> orderByElements_list = new ArrayList<OrderByElement>();

    private void ORDER_NodeProcessor() {

        OrderByElement OrderByElement1 = new OrderByElement() {{
            //    setExpression(new Column("c1"));
            //   setAsc(true);
            //    setAscDescPresent(false);
        }};


        //   PlainSelect PlainSelect1 = getPlainSelectFromStack(Stack1);

        PlainSelect1.setOrderByElements(orderByElements_list);

        Stack1.push(PlainSelect1);
        Stack1.push("ORDER");


    }

    private PlainSelect getPlainSelectFromStack(Stack stack1) {
        while (true) {


            Object obj = Stack1.pop();

            if (obj instanceof PlainSelect) //where
                return (PlainSelect) obj;
        }
    }


    @Override
    public void setWhere(Expression newAddExp) {
        PlainSelect1.setWhere(newAddExp);
    }


    public void iden_evt(String text, AST nextSibling) {


        try {
            super.iden_evt(text, nextSibling);
        } catch (BreakNextException e) {
            return;
        }


        Object obj = obj_poped;
        if (text == "a")
            System.out.println("D");


        if (obj == "GroupByColumnReferencesAti") //  EXPR_LIST
        {

            GroupByColumnReferencesList.add(new Column(text));
            return;

            //   Stack1.push(obj);
        }


//  obj instanceof List<SelectExpressionItem>
        if (obj == "SELECT") // select list
        {
            //  SelectExpressionItem
            SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
            selectExpressionItem.setExpression(new Column(text));
            selectList_SelectExpressionItem.add(selectExpressionItem);
            //     li.add(text);
            Stack1.push(obj);
            return;
        }

        if (obj.equals("ORDER")) {
            //     orderByElements_list
            OrderByElement curE = new OrderByElement();
            curE.setExpression(new Column(text));
            orderByElements_list.add(curE);// add last
            Stack1.push(curE);
            return;
        }

        if (obj instanceof OrderByElement) // pop is OrderByElement
        {
            OrderByElement curE2 = new OrderByElement();
            curE2.setExpression(new Column(text));
            orderByElements_list.add(curE2);
            Stack1.push(curE2);
            if (nextSibling == null) // last
            {

                Stack1.pop();
            }
            return;
        }

    }


    @Override
    protected void setFromTable(String text) {
        PlainSelect1.setFromItem(new Table(text));
    }


    public String toSql() {

        if (PlainSelect1.getSelectItems().size() == 0) {
            PlainSelect1.getSelectItems().add(new AllColumns());
        }
        return select_where_and.toString();
    }
}
