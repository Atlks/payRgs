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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
public class JqplAst2SqlAstConverterDelete extends JqplAst2SqlAstConverter {


    public Delete deleteStatment1 = new Delete();

    //
//
//    public void processByTokentypenameAndTxt(String tokenTypeName, String text, AST nextSibling) {
//        log.info("now token is ::" + tokenTypeName + "  :  " + text);
//
//
//            super.processByTokentypenameAndTxt(tokenTypeName, text, nextSibling);
//            //none
//
//
//        if (tokenTypeName.equals("IDENT")) {
//            iden_evt(text, nextSibling);
//            return;
//
//        }
//    }


    public void processTokenTypename_espPart(String tokenTypeName) {

        log.info("now token is ::" + tokenTypeName);


        //    super.processTokenTypename(tokenTypeName);


        if (tokenTypeName.equals("FROM")) {

            Table tb = new Table();
            deleteStatment1.setTable(tb);
            Stack1.push(tb);

            return;
        }

    }


    @Override
    public void setWhere(Expression newAddExp) {
        deleteStatment1.setWhere(newAddExp);
    }




    public void iden_evt(String text, AST nextSibling) {


        try {
            super.iden_evt(text, nextSibling);
        } catch (BreakNextException e) {
            return;
        }

        Object obj = obj_poped;

        if (obj instanceof Table) //from ele
        {
            Table t = (Table) obj;
            t.setName(text);
            return;
        }

    }

    @Override
    public void processByTokentypenameAndTxt_QUD_perTypeSttEspePart(String tokenTypeName, String text, AST nextSibling) {
        if (tokenTypeName.equals("IDENT")) {
            iden_evt(text, nextSibling);
            return;

        }
    }

    @Override
    protected void setFromTable(String text) {
        deleteStatment1.setTable( new Table(text));
    }


    public String toSql() {


        return deleteStatment1.toString();
    }
}
