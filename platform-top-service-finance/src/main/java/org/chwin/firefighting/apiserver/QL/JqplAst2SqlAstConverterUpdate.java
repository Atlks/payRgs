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
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.SelectUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Slf4j
public class JqplAst2SqlAstConverterUpdate extends JqplAst2SqlAstConverter {


    public Update UpdateStatment1 = new Update();

    JqplAst2SqlAstConverterUpdate() {
        UpdateStatment1.setColumns(Lists.newArrayList());
    }


    public void processTokenTypename_espPart(String tokenTypeName) {
        log.info("now token is ::" + tokenTypeName);
        //     super.processTokenTypename(tokenTypeName);
        if (tokenTypeName.equals("SET")) {
            set_evt();
            return;
        }
    }
    public void processByTokentypenameAndTxt_QUD_perTypeSttEspePart(String tokenTypeName, String text, AST nextSibling) {
        log.info("now token is ::" + tokenTypeName + "  :  " + text);

        //    super.processByTokentypenameAndTxt(tokenTypeName, text, nextSibling);


        if (tokenTypeName.equals("IDENT")) {
            iden_evt(text, nextSibling);
            return;
        }

        //set equ right exp
//        if (tokenTypeName.equals("QUOTED_STRING")) {
//            if (val_QuotedString_evt(text)) return;
//            return;
//
//        }
        if (tokenTypeName.equals("NUM_INT")) {
            valProcess_NUM_INT__evt(text);
            return;

        }

   //     super.processByTokentypenameAndTxt(tokenTypeName, text, nextSibling);


    }


    public void valProcess_NUM_INT__evt(String text) {



        try {
            super.valProcess_NUM_INT__evt(text);
        } catch (BreakNextException e) {
            return;
        }

        Object obj_last = obj_poped;
        if (obj_last.equals("SET_eq")) {
            if (UpdateStatment1.getExpressions() == null)
                UpdateStatment1.setExpressions(Lists.newArrayList());
            UpdateStatment1.getExpressions().add(new LongValue(text));
            return;
        }


    }




    private void set_evt( ) {
        Stack1.push("SET");
        return;
    }


    @Override
    public void setWhere(Expression newAddExp) {
        UpdateStatment1.setWhere(newAddExp);
    }

    @SneakyThrows
    public void eq_evt() {


            super.eq_evt();


        log.info(UpdateStatment1.toString());


        EqualsTo EqualsTo1 = (EqualsTo) statckParam.pop();


        if (obj_poped.equals("SET")) {

            Stack1.push("SET");
            Stack1.push("SET_eq");

            return;
        }


    }


    public void iden_evt(String text, AST nextSibling) {
        if (text.equals("c"))
            System.out.println("d");
        obj_poped = Stack1.pop();
        Object obj = obj_poped;


        if (obj.equals("SET_eq")) {
            if (UpdateStatment1.getColumns() == null)
                UpdateStatment1.setColumns(Lists.newArrayList());
            UpdateStatment1.getColumns().add(new Column(text));
            Stack1.push(obj);
            return;
        }

        if (obj.equals("FROM")) {
            if (UpdateStatment1.getTables() == null)
                UpdateStatment1.setTables(Lists.newArrayList());
            UpdateStatment1.getTables().add(new Table(text));
            return;
        }

        Stack1.push(obj_poped);
        try {
            super.iden_evt(text, nextSibling);
        } catch (BreakNextException e) {
            return;
        }

    }



    @Override
    protected void setFromTable(String text) {
      if(  UpdateStatment1.getTables()==null)
          UpdateStatment1.setTables(Lists.newArrayList());
        UpdateStatment1.getTables().add(new Table(text));
    }


    public String toSql() {


        return UpdateStatment1.toString();
    }
}
