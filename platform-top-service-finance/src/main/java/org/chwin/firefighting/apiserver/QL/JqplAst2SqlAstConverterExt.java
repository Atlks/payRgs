package org.chwin.firefighting.apiserver.QL;

import antlr.collections.AST;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import org.hibernate.hql.internal.antlr.HqlTokenTypes;

import java.util.*;

@Slf4j
public class JqplAst2SqlAstConverterExt {

    public JqplAst2SqlAstConverterSelect JqplAst2SqlAstConverterSelect1 = new JqplAst2SqlAstConverterSelect();
    public JqplAst2SqlAstConverterUpdate JqplAst2SqlAstConverterUpdate1 = new JqplAst2SqlAstConverterUpdate();
    public JqplAst2SqlAstConverterDelete JqplAst2SqlAstConverterDelete1 = new JqplAst2SqlAstConverterDelete();
    public String jqplType = "QUERY";

    public String curNodeNameStat;
    public AndExpression curAddNode;
    public String curNodeStat;
    public List<SelectExpressionItem> selectList_SelectExpressionItem = Lists.newArrayList();
    public Object WhereElement;
    public Delete deleteStatment1;
    public Stack Stack1 = new Stack();
    public Map<String, JqplAst2SqlAstConverter> m_convert = new HashMap<>();

    public JqplAst2SqlAstConverterExt() {

        m_convert.put("QUERY", JqplAst2SqlAstConverterSelect1);

        m_convert.put("UPDATE", JqplAst2SqlAstConverterUpdate1);

        m_convert.put("DELETE", JqplAst2SqlAstConverterDelete1);
        //    JqplAst2SqlAstConverterUpdate1 = new;
    }


    @SneakyThrows
    public void processTokenTypename(String tokenTypeName) {

        log.info("now token is ::" + tokenTypeName);

        if ("QUERY" == tokenTypeName) {
            jqplType = "QUERY";
        }
        if ("UPDATE" == tokenTypeName) {
            jqplType = "UPDATE";
        }
        if ("DELETE" == tokenTypeName) {
            jqplType = "DELETE";
        }
        JqplAst2SqlAstConverter cvt = m_convert.get(jqplType);
        cvt.processTokenTypename(tokenTypeName);


    }

    @SneakyThrows
    public void processByTokentypenameAndTxt(String tokenTypeName, String text, AST nextSibling) {
        log.info("now token is ::" + tokenTypeName + "  :  " + text);

        JqplAst2SqlAstConverter cvt = m_convert.get(jqplType);
        cvt.processByTokentypenameAndTxt(tokenTypeName, text, nextSibling);

        //

    }


    public String toSql() {

        JqplAst2SqlAstConverter cvt = m_convert.get(jqplType);
        return cvt.toSql();


    }


}

