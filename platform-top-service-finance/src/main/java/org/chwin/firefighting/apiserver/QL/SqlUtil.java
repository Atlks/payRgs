package org.chwin.firefighting.apiserver.QL;

import attilax.aql.AqlUtil;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.statement.SQLCallStatement;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.statement.Statement;
import org.chwin.firefighting.apiserver.sql.ExpressionWarp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SqlUtil {

    public static StringValue newtStringValue(String s) {
        StringValue aaa = new StringValue("aaa");
        aaa.setValue(s);
        return aaa;
    }

    public static void traveTree(Expression expNode, Consumer<Function> Fun_Consumer1, Consumer<Expression> NoFun_Consumer1, boolean lastChldFlag) {

        System.out.println("travetree curExp:" + expNode.getClass() + ",          ExpStr:" + expNode.toString());

        if (expNode instanceof Function) {
            Function f = (Function) expNode;
            Fun_Consumer1.accept(f);
            Function Function1 = (Function) expNode;
            List<Expression> list = Function1.getParameters().getExpressions();
            if (list.size() > 0) { // multiChildNodes
                int i = 0;
                boolean lastChldFlag_tmp = false;
                for (Expression node : list) {
                    i++;
                    if (i == list.size()) {
                        lastChldFlag_tmp = true;
                        Map m = Maps.newLinkedHashMap();
                        m.put("fun", Function1);
                        m.put("node", node);
                        m.put("lastChldFlag_tmp", lastChldFlag_tmp);
                        System.out.println(JSON.toJSONString(m, true));
                    }

                    traveTree(node, Fun_Consumer1, NoFun_Consumer1, lastChldFlag_tmp);
                }
            }
        } else {
            // normal expre node

            Map m = Maps.newLinkedHashMap();
            //    m.put("fun",Function1);
            m.put("node", expNode);
            m.put("lastChldFlag_tmp", lastChldFlag);
            System.out.println(JSON.toJSONString(m, true));
            ExpressionWarp ew = new ExpressionWarp();
            ew.isLastCld = lastChldFlag;
            ew.exp = expNode;
            NoFun_Consumer1.accept(ew);
        }

    }


    public static void traveTree(Expression expNode, Consumer<Function> Fun_Consumer1) {

        System.out.println("travetree curExp:" + expNode.getClass() + ",          ExpStr:" + expNode.toString());

        if (expNode instanceof Function) {
            Function f = (Function) expNode;
            Fun_Consumer1.accept(f);
            Function Function1 = (Function) expNode;
            List<Expression> list = Function1.getParameters().getExpressions();
            if (list.size() > 0) { // multiChildNodes
                for (Expression node : list) {
                    traveTree(node, Fun_Consumer1);
                }
            }
        } else {
            // normal expre node
        }

    }

    public static SQLExpr getCond(String ifCond) {
        String sql = ifCond + " then select 1;end if;";
        // 新建 MySQL Parser
        SQLStatementParser parser = new MySqlStatementParser(sql);

        // 使用Parser解析生成AST，这里SQLStatement就是AST
        SQLStatement statement = parser.parseStatement();
        SQLIfStatement ifstt = (SQLIfStatement) statement;
        return ifstt.getCondition();

    }

    public static List<SQLStatement> getLI_SQLStatement_ifBlock(String ifStts) {
        List<SQLStatement> li = new ArrayList<>();
        String[] a = ifStts.split(";");
        for (String aql : a) {
            if (aql.startsWith("查询表格")) {
                Function fun = astiqlAstBuilder.atiql2ast(aql);
                SQLStatement statement = getSqlStatementFromFunast(fun);
                li.add(statement);
                //  System.out.println(stt.toString());
            } else if (aql.startsWith("更新")) {
                Function fun = astiqlAstBuilder.atiql2ast(aql);
                Statement stt = new atiql2SqlAstConverterSelectUpdate().toSqlAst(fun);
                SQLStatement stt2 = getSqlStatementFromJsqlStt(stt.toString());
                li.add(stt2);
            } else if (aql.startsWith("删除")) {
                Function fun = astiqlAstBuilder.atiql2ast(aql);
                Statement stt = new atiql2SqlAstConverterSelectDelete().toSqlAst(fun);
                SQLStatement stt2 = getSqlStatementFromJsqlStt(stt.toString());
                li.add(stt2);
            }

        }
        return li;
    }

    private static SQLStatement getSqlStatementFromJsqlStt(String sql) {
        //  String sql = s;
        // 新建 MySQL Parser
        SQLStatementParser parser = new MySqlStatementParser(sql);
        return parser.parseStatement();
    }

    private static SQLStatement getSqlStatementFromFunast(Function fun) {
        Statement stt = new atiql2sqlAstConverterSelect().toSqlAst(fun);
        String sql = stt.toString();
        // 新建 MySQL Parser
        SQLStatementParser parser = new MySqlStatementParser(sql);

        // 使用Parser解析生成AST，这里SQLStatement就是AST
        return parser.parseStatement();
    }

    public static SQLStatement getSQLStatementFromAql(String aql) {
        if (aql.startsWith("查询表格")) {
            Function fun = astiqlAstBuilder.atiql2ast(aql);
            Statement statement = new atiql2sqlAstConverterSelect().toSqlAst(fun);
            return getSqlStatementFromJsqlStt(statement.toString());
        } else if (aql.startsWith("更新")) {
            Function fun = astiqlAstBuilder.atiql2ast(aql);
            Statement stt = new atiql2SqlAstConverterSelectUpdate().toSqlAst(fun);
            return getSqlStatementFromJsqlStt(stt.toString());
        } else if (aql.startsWith("删除")) {
            Function fun = astiqlAstBuilder.atiql2ast(aql);
            Statement stt = new atiql2SqlAstConverterSelectDelete().toSqlAst(fun);
            return getSqlStatementFromJsqlStt(stt.toString());
        }

        return null;
    }

    public static void printSqwl(List<SQLStatement> sttList) {

        sttList.forEach(new Consumer<SQLStatement>() {
            @Override
            public void accept(SQLStatement token) {
                //   token.
                token.setAfterSemi(true);
                if (token instanceof SQLIfStatement) {
                    System.out.println(printlnIfStt((SQLIfStatement) token));
                    ;
                } else
                    System.out.println(token);
                //    System.out.print(token+";\r\n");

            }
        });
    }

    private static String printlnIfStt(SQLIfStatement token) {

        String ifthen = "IF " + token.getCondition() + "  THEN \r\n";
        //  System.out.print(ifthen);
        List<SQLStatement> li = token.getStatements();
        List<String> ifblockList = new ArrayList<>();
        li.forEach(new Consumer<SQLStatement>() {
            @Override
            public void accept(SQLStatement sqlStatement) {
                sqlStatement.setAfterSemi(true);
                ifblockList.add(sqlStatement.toString());
            }
        });

        ifthen = ifthen + Joiner.on("\r\n").join(ifblockList);
        ifthen = ifthen + "\r\n end if;\r\n";
        return ifthen;
    }

    public static void printSql4dbg(List<SQLStatement> sttList) {

        sttList.forEach(new Consumer<SQLStatement>() {
            @Override
            public void accept(SQLStatement token) {
                //   token.
                token.setAfterSemi(true);
                if (token instanceof SQLIfStatement) {
                    System.out.println(printlnIfStt4dbg((SQLIfStatement) token));

                } else
                    System.out.println(token);
                //    System.out.print(token+";\r\n");

            }
        });
    }


    private static String geneIfSttSql(SQLIfStatement SQLIfStatement1, java.util.function.Function<SQLStatement, String> Consumer1) {
        // java.util.function.
        String ifthen = "IF " + SQLIfStatement1.getCondition() + "  THEN \r\n";
        //  System.out.print(ifthen);
        List<SQLStatement> li = SQLIfStatement1.getStatements();
        List<String> ifblockList = new ArrayList<>();
        li.forEach(new Consumer<SQLStatement>() {
            @Override
            public void accept(SQLStatement sqlStatement) {
                sqlStatement.setAfterSemi(true);
                //  Consumer1
                if (sqlStatement instanceof SQLCallStatement) {
                    String SQLCallStatement_2sql = Consumer1.apply(sqlStatement);
                    ifblockList.add(SQLCallStatement_2sql);
                } else
                    ifblockList.add(sqlStatement.toString());
            }
        });

        String joinThenblockSql = Joiner.on(";\r\n").join(ifblockList)+";";
        ifthen = ifthen + joinThenblockSql;


        //proces  else block
        if (SQLIfStatement1.getElseItem() != null) {
            List<SQLStatement> li_else = SQLIfStatement1.getElseItem().getStatements();
            if (li_else.size() > 0) {
                ifthen = ifthen + "\r\n" + elseProcess(li_else);
            }
        }


        ifthen = ifthen + "\r\n end if;";
        return ifthen;

    }

    private static String printlnIfStt4dbg(SQLIfStatement token) {
        String ifthen = "IF " + token.getCondition() + "  THEN \r\n";
        //  System.out.print(ifthen);
        List<SQLStatement> li = token.getStatements();
        List<String> ifblockList = new ArrayList<>();
        li.forEach(new Consumer<SQLStatement>() {
            @Override
            public void accept(SQLStatement sqlStatement) {
                sqlStatement.setAfterSemi(true);
                ifblockList.add(sqlStatement.toString());
            }
        });

        ifthen = ifthen + Joiner.on(";\r\n").join(ifblockList);


        //proces  else block
        if (token.getElseItem() != null) {
            List<SQLStatement> li_else = token.getElseItem().getStatements();
            if (li_else.size() > 0) {
                ifthen = ifthen + "\r\n" + elseProcess(li_else);
            }
        }


        ifthen = ifthen + "\r\n end if;";
        return ifthen;

    }

    private static String elseProcess(List<SQLStatement> li_else) {
        List<String> blockList = new ArrayList<>();
        String stt_s = "ELSE \r\n";
        li_else.forEach(new Consumer<SQLStatement>() {
            @Override
            public void accept(SQLStatement sqlStatement) {
                sqlStatement.setAfterSemi(true);
                blockList.add(sqlStatement.toString());
            }
        });
        stt_s = stt_s + Joiner.on("\r\n").join(blockList);
        return stt_s;


    }

    public static String printSql4aqlcall(List<SQLStatement> sttList) {


        java.util.function.Function<SQLStatement, String> callStt2sqlProcessor = new java.util.function.Function<SQLStatement, String>() {

            @Override
            public String apply(SQLStatement sqlStatement) {
                List<String> li = new ArrayList<>();
                if (sqlStatement instanceof SQLCallStatement) {
                    SQLCallStatement SQLCallStatement1 = (SQLCallStatement) sqlStatement;
                    if (SQLCallStatement1.getProcedureName().toString().equals("aql")) {
                        List<SQLExpr> parameters = SQLCallStatement1.getParameters();
                        SQLExpr SQLExpr1 = parameters.get(0);
                        String aqles = ((SQLCharExpr) SQLExpr1).getText();
                        String[] a = aqles.split("\\;");
                        for (String aql : a) {
                            String sql = AqlUtil.getSQLFromAql(aql);
                            li.add(sql);
                        }
                        return Joiner.on(";\r\n").join(li);
                    }
                }
                return null;
            }
        };


        String r = "";
        List<String> li=new ArrayList<>();
        sttList.forEach(new Consumer<SQLStatement>() {
            @Override
            public void accept(SQLStatement SQLStatement1) {
                //   token.
                SQLStatement1.setAfterSemi(true);
                if (SQLStatement1 instanceof SQLIfStatement) {

                    String ifSttSql = geneIfSttSql((SQLIfStatement) SQLStatement1, callStt2sqlProcessor);
                  // r=r+ ifSttSql;
                    li.add(ifSttSql);
                  //  System.out.println(ifSttSql);

                } else
                    li.add(SQLStatement1.toString());
                   // System.out.println(token);
                //    System.out.print(token+";\r\n");

            }
        });
        return Joiner.on(";\r\n").join(li);
    }
}
