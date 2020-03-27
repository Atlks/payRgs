package org.chwin.firefighting.apiserver.QL;

import antlr.collections.AST;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.hibernate.*;
import org.hibernate.hql.internal.ast.HqlParser;
import org.hibernate.hql.internal.ast.tree.Node;

import java.util.*;

public class JqplHqlParserT {
    @SneakyThrows
    public static void main(String[] args) {
        // ASTPrinterAti
        // try{}catch{}

        Map<String, Filter> enabledFilters = Maps.newLinkedHashMap();
        System.out.println();
        String hql = "select _all from info_class1 where c1=123 and fun1(c2) >456";
        hql = "  from info_class1 where c1=123   order by c1 desc";

        HqlParser hqlParser1 = HqlParser.getInstance(hql);
        hqlParser1.statement();

        final AST hqlAst = hqlParser1.getAST();

     //   System.out.println(JqplAst2SqlAstConverter.Jqpl2sql(hqlAst));
        //

        //-------------- show as json
        Node node1 = (org.hibernate.hql.internal.ast.tree.Node) hqlAst;
        //   System.out.println(  JSON.toJSONString(node1,true) );


        //   QueryTranslatorImpl.JavaConstantConverter strategy = new QueryTranslatorImpl.JavaConstantConverter(null);
        //  NodeTraverser walker = new NodeTraverser(strategy);
        //   walker.traverseDepthFirst( hqlAst );

        System.out.println("");


        System.out.println("--f");
    }
}

