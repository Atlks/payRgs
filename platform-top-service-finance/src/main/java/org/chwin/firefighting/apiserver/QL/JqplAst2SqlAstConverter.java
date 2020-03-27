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
import org.apache.velocity.runtime.directive.Break;
import org.hibernate.hql.internal.antlr.HqlTokenTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Slf4j
public abstract class JqplAst2SqlAstConverter {


    public AndExpression curAddNode;

    public List<SelectExpressionItem> selectList_SelectExpressionItem = Lists.newArrayList();
    public Object WhereElement;

    public Stack Stack1 = new Stack();

    public JqplAst2SqlAstConverter() {


    }


    public void processByTokentypenameAndTxt(String tokenTypeName, String text, AST nextSibling)   {
        log.info("now token is ::" + tokenTypeName + "  :  " + text);
        log.info( "now sql"+this.toSql());
        if (tokenTypeName.equals("AGGREGATE")) {   //sum()

            fun_AGGREGATe_fun(text);

              return;

        }


        if (tokenTypeName.equals("IDENT")) {
            try {
                this.iden_evt(text, nextSibling);
            } catch (BreakNextException e) {
                return;
            }

            return;

        }

        //set equ right exp
        if (tokenTypeName.equals("QUOTED_STRING")) {
            if (val_QuotedString_evt(text)) return;
            return;

        }
        if (tokenTypeName.equals("NUM_INT")) {
            try {
                valProcess_NUM_INT__evt(text);
            } catch (BreakNextException e) {
                return;
            }
            return;

        }

        processByTokentypenameAndTxt_QUD_perTypeSttEspePart(tokenTypeName,text,nextSibling);
    }

    private boolean val_QuotedString_evt(String text) {
        Object obj = Stack1.pop();
        if (obj instanceof EqualsTo) // parent is And exp
        {
            EqualsTo Expression1 = (EqualsTo) obj;
            Expression1.setRightExpression(new StringValue(text));
            //not push back
        }


        if (obj instanceof LikeExpression) {
            LikeExpression LikeExpression1 = (LikeExpression) obj;
            LikeExpression1.setRightExpression(new StringValue("aaaa") {{
                setValue(text);
            }});
            //   Stack1.push(obj);
            return true;
        }
        return false;
    }

    public void valProcess_NUM_INT__evt(String text) throws BreakNextException {
        Object obj_last = Stack1.pop();   //ge

        if (text.equals("3"))
            System.out.println("d");
        if (obj_last instanceof EqualsTo) // parent is And exp
        {
            EqualsTo Expression1 = (EqualsTo) obj_last;
            Expression1.setRightExpression(new LongValue(text));
         //   return true;
            //not push back
            throw new BreakNextException();
        }

        if (obj_last instanceof BinaryExpression) // parent is  = > < !=  >=  <=
        {
            BinaryExpression Expression1 = (BinaryExpression) obj_last;
            Expression1.setRightExpression(new LongValue(text));
            throw new BreakNextException();
            //not push back
        }

     //   valProcess_NUM_INT__evt_EspPart(text);
    }
//    public abstract void valProcess_NUM_INT__evt_EspPart(String text) ;

    public void processTokenTypename(String tokenTypeName)   {

        log.info("now token is ::" + tokenTypeName);
        log.info( "now sql"+this.toSql());
        if (tokenTypeName.equals("LIKE")) {
            Object obj = Stack1.pop();
            if (obj instanceof AndExpression) {
                LikeExpression LikeExpression1 = new LikeExpression();
                setAndExp(obj, LikeExpression1);
                Stack1.push(obj);  //paret and
                Stack1.push(LikeExpression1);  //paret and
                return;
            }


            return;
        }


        if (tokenTypeName.equals("FROM")) {

            Stack1.push("FROM");
            return;


        }

        if (tokenTypeName.equals("GE")) {

            //  Object o = Stack1.pop();


        }


        if (tokenTypeName.equals("AS")) {   //sum()

            as_fun();

        }


        if (tokenTypeName.equals("METHOD_CALL")) {   //sum()

            fun_process("(");
//            Object obj= Stack1.pop();
//
//            if (obj instanceof List) // pop is  selectElem
//            {
//                List curE = (List) obj;
//                Function fun1 = new Function() {{
//                    setName(fun_name);
//                }};curE.add(fun1);
//                Stack1.push(curE); //list   selectElem
//                Stack1.push(fun1);
//            }

        }
        if (tokenTypeName.equals("EXPR_LIST")) {   //sum()


            Function Function1 = (Function) Stack1.pop();
            ExpressionList ExpressionList1 = new ExpressionList() {{
                setExpressions(new ArrayList() {{
                    // add(new Column("udfcol"));
                }});
            }};
            Function1.setParameters(ExpressionList1);

            Stack1.push(Function1);
            Stack1.push(ExpressionList1);
            return;
        }


        if (tokenTypeName.equals("COUNT")) {   //sum()

            SelectExpressionItem asItem = (SelectExpressionItem) Stack1.pop();
            Function fun1 = new Function() {{
                setName("count");

            }};
            asItem.setExpression(fun1);

            Stack1.push(asItem);
            Stack1.push(fun1);


        }


        if (tokenTypeName.equals("ROW_STAR")) {   //sum()

            Function fun1 = (Function) Stack1.pop();

            fun1.setParameters(new ExpressionList() {{
                setExpressions(new ArrayList() {{
                    add(new Column("*"));
                }});
            }});

            //     Stack1.push(asItem);
            //     Stack1.push(fun1);


        }


        if (tokenTypeName.equals("WHERE")) {
            //    curNodeNameStat = "WHERE"
            Stack1.push("WHERE");
          return;

        }

        if (tokenTypeName.equals("EQ")) {
            eq_evt();
            return;

        }

        if (tokenTypeName.equals("AND")) {

            and_evt();
            return;

        }
        processTokenTypename_espPart(tokenTypeName);

    }

    private void setAndExp(Object obj, Expression exp) {

        AndExpression AndExpression1 = (AndExpression) obj;
        if (AndExpression1.getLeftExpression() != null && AndExpression1.getRightExpression() != null) {
            obj = Stack1.pop();
            AndExpression1 = (AndExpression) obj;
        }

        if (AndExpression1.getLeftExpression() == null) {
            AndExpression1.setLeftExpression(exp);
        } else if (AndExpression1.getRightExpression() == null)
            AndExpression1.setRightExpression(exp);
        else
            throw new RuntimeException("err");
    }


    private void as_fun() {
        List<SelectItem> List_SelectItem1 = (List<SelectItem>) Stack1.pop();
        SelectExpressionItem asItem = new SelectExpressionItem();

        //{{
//                setExpression(new Function() {{
//                    setName("sum");
//                    setParameters(new ExpressionList() {{
//                        setExpressions(new ArrayList() {{
//                            add(new Column("cnt"));
//                        }});
//                    }});
//
//                }});

        //     }};

        asItem.setAlias(new Alias("$AsSelectExpressionItem"));

        List_SelectItem1.add(asItem);
        Stack1.push(List_SelectItem1);
        Stack1.push(asItem);
    }

    private void fun_AGGREGATe_fun(String text) {
        final String fun_name = text;
        fun_process(fun_name);
    }

    public void fun_process(String fun_name) {
        Object obj = Stack1.pop();
        if (fun_name.equals("("))
            System.out.println("dbg");
        if (obj instanceof List) // pop is  selectElem
        {
            List curE = (List) obj;
            Function fun1 = new Function();
            fun1.setName(fun_name);

            curE.add(fun1);
            Stack1.push(curE); //list   selectElem
            Stack1.push(fun1);
        } else if (obj instanceof SelectExpressionItem) {  // par is Asitem
            //   obj.getClass()
            SelectExpressionItem asItem = (SelectExpressionItem) obj;
            Function fun1 = new Function();
            fun1.setName(fun_name);
            asItem.setExpression(fun1);
            Stack1.push(asItem);
            Stack1.push(fun1);
        } else {
            if (obj instanceof BinaryExpression) {
                BinaryExpression BinaryExpression1 = (BinaryExpression) obj;
                Function fun1 = new Function();
                fun1.setName(fun_name);
                BinaryExpression1.setLeftExpression(fun1);
                Stack1.push(obj);  //ge
                Stack1.push(fun1); //fun AGGREGATE
            }


        }
    }


    public void and_evt()   {
        Object obj = Stack1.pop();
        obj_poped = obj;


        if (obj_poped.equals("WHERE")) {
            AndExpression newAddExp = new AndExpression(null, null);
            setWhere(newAddExp);
            Stack1.push(newAddExp);
            return;

        }
        if (obj instanceof AndExpression) //and exp
        {
            AndExpression AndExpression1_FromStatck = (AndExpression) obj;
            AndExpression newAddExp = new AndExpression(null, null);

            AndExpression1_FromStatck.setLeftExpression(newAddExp);
            Stack1.push(AndExpression1_FromStatck);
            Stack1.push(newAddExp);

        }
    }

    public abstract void setWhere(Expression newAddExp);

    public Object obj_poped;
    public Stack statckParam = new Stack();

    public void eq_evt()   {
        //  log.info(select_where_and.toString());
        EqualsTo EqualsTo1 = new EqualsTo();
        Object obj = Stack1.pop();
        obj_poped = obj;

        statckParam.push(EqualsTo1);


        if (obj instanceof AndExpression) // parent is And exp  &&eq
        {
            AndExpression AndExpression1 = (AndExpression) obj;
            if (AndExpression1.getLeftExpression() != null && AndExpression1.getRightExpression() != null) {
                obj = Stack1.pop();
                AndExpression1 = (AndExpression) obj;
            }

            if (AndExpression1.getLeftExpression() == null) {
                AndExpression1.setLeftExpression(EqualsTo1);
            } else if (AndExpression1.getRightExpression() == null)
                AndExpression1.setRightExpression(EqualsTo1);
            else
                throw new RuntimeException("err");


            Stack1.push(obj);  //paret and
            Stack1.push(EqualsTo1);
            return;
        }
    }
    //public abstract void iden_evt_espPart(String text, AST nextSibling);
    public abstract void processTokenTypename_espPart(String tokenTypeName);
    public void iden_evt(String text, AST nextSibling) throws BreakNextException {
        Object obj = Stack1.pop();
        obj_poped = obj;


        if (obj instanceof LikeExpression) {
            LikeExpression LikeExpression1 = (LikeExpression) obj;
            LikeExpression1.setLeftExpression(new Column(text));
            Stack1.push(obj);
            throw new BreakNextException();
            //   return;
        }


        if (obj instanceof ExpressionList) //  EXPR_LIST
        {
            ExpressionList ExpressionList1 = (ExpressionList) obj;
            ExpressionList1.getExpressions().add(new Column(text));
            if (nextSibling == null)
                Stack1.pop();  //pop  METHOD_CALL ele
            throw new BreakNextException();
            //    return;

            //   Stack1.push(obj);
        }


        if (is$AsSelectExpressionItem(obj)) {

            SelectExpressionItem asItem = (SelectExpressionItem) obj;
            asItem.setAlias(new Alias(text));

            throw new BreakNextException();
            //   Stack1.push(obj);
        }

        if (fun_isMETHOD_CALL(obj)) {
            Function fun = (Function) obj;
            fun.setName(text);
            Stack1.push(fun);
            throw new BreakNextException();
            //    return;
        }


        if (obj instanceof Function) //
        {
            Function fun = (Function) obj;
            ExpressionList ExpressionList1 = new ExpressionList();


            ExpressionList1.setExpressions(new ArrayList() {{
                add(new Column(text));
            }});

            fun.setParameters(ExpressionList1);
            throw new BreakNextException();
            //   Stack1.push(obj);
        }


        //-----------------------------------------------
        //only for select or delete??   update need rewrite
        if (obj == "FROM") {
            setFromTable(text);

            throw new BreakNextException();
            //  return;
        }


        if (obj instanceof EqualsTo) // parent is And exp
        {
            EqualsTo EqualsTo1 = (EqualsTo) obj;
            EqualsTo1.setLeftExpression(new Column(text));
            Stack1.push(EqualsTo1);
            throw new BreakNextException();
        }


    }
    public abstract void processByTokentypenameAndTxt_QUD_perTypeSttEspePart( String tokenTypeName, String text, AST nextSibling);


    protected abstract void setFromTable(String text);

    private boolean is$AsSelectExpressionItem(Object obj) {

        if (obj instanceof SelectExpressionItem) //
        {
            SelectExpressionItem SelectExpressionItem1 = (SelectExpressionItem) obj;
            return SelectExpressionItem1.getAlias().getName().equals("$AsSelectExpressionItem");
        }
        return false;
    }

    private boolean fun_isMETHOD_CALL(Object obj) {
        if (obj instanceof Function) {
            Function f = (Function) obj;
            return f.getName().equals("(");

        } else
            return false;
    }


    public abstract String toSql();
}
