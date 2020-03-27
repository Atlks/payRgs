package org.chwin.firefighting.apiserver.QL;

import antlr.collections.AST;
import lombok.SneakyThrows;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.hibernate.hql.internal.antlr.HqlTokenTypes;
import org.hibernate.hql.internal.ast.HqlParser;

public class JqplUtil {

    @SneakyThrows
    public static String Jqpl2sql(String hql) {


//        String hql = "select _all from info_class1 where c1=123 and fun1(c2) >456";
//        hql = "  from info_class1 where c1=123  ";

        HqlParser hqlParser1 = HqlParser.getInstance(hql);
        hqlParser1.statement();

        final AST hqlAst = hqlParser1.getAST();

        return JqplAst2sql(hqlAst);
    }

    @SneakyThrows
    public static void showAst(String hql) {
        HqlParser hqlParser1 = HqlParser.getInstance(hql);
        hqlParser1.statement();

        final AST hqlAst = hqlParser1.getAST();
        //hqlParser1.showAst(hqlAst,System.out);
        hqlParser1.showAst(hqlAst, System.out);

    }

    public static String JqplAst2sql(AST hqlAst) {

        //hqlParser1.showAst(hqlAst,System.out);
        ASTPrinterAti astPrinterAti = new ASTPrinterAti(HqlTokenTypes.class);
        astPrinterAti.showAst(hqlAst, System.out);
        return astPrinterAti.jqplAst2SqlAstConverter1.toSql();
    }

    @SneakyThrows
    public static String Jqpl2sql(String hql, String pageSubSql) {
        HqlParser hqlParser1 = HqlParser.getInstance(hql);
        hqlParser1.statement();

        final AST hqlAst = hqlParser1.getAST();

        return JqplAst2sql(hqlAst) + " " + CheckLimitSql(pageSubSql);
    }

    @SneakyThrows
    private static String CheckLimitSql(String pageSubSql) {
        String sql = "select * from t " + pageSubSql;
        Select Select1 = (Select) CCJSqlParserUtil.parse(sql);
        PlainSelect PlainSelect1 = (PlainSelect) Select1.getSelectBody();
        Limit lmt = PlainSelect1.getLimit();

        return String.format("limit LIMIT %s OFFSET %s", lmt.getRowCount(), lmt.getOffset());
    }
}
