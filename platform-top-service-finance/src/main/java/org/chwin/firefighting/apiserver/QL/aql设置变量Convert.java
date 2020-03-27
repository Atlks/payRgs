package org.chwin.firefighting.apiserver.QL;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.statement.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class aql设置变量Convert {
    SQLSetStatement setStt = new SQLSetStatement();

    public static void main(String[] args) {

        String s = "设置变量(@用户=123,@时间=1999)";
        Function fun = astiqlAstBuilder.atiql2ast(s);
        System.out.println(fun);

        SQLStatement stt = new aql设置变量Convert().toSqlAst(fun);
        System.out.println(stt.toString());

    }

    public SQLStatement toSqlAst(Function fun) {

        setStt.setItems(new ArrayList());

        SqlUtil.traveTree(fun, new Consumer<Function>() {
            @Override
            public void accept(Function f) {
                if(!f.getName().equals("设置变量"))
                {
                  //  if (f.getName().equals("删除记录")) {
                    return;
                }
                List<Expression> expressions = f.getParameters().getExpressions();
                expressions.forEach(new Consumer<Expression>() {
                    @Override
                    public void accept(Expression expression) {
                        StringValue sv = (StringValue) expression;
                        String var = sv.getValue().split("=")[0];
                        String var_val = sv.getValue().split("=")[1];

                        SQLAssignItem ai = new SQLAssignItem() {{
                            SQLVariantRefExpr target = new SQLVariantRefExpr(var);
                            //    target.setGlobal(true);
                            target.setSession(true);
                            setTarget(target);
                            setValue(new SQLCharExpr(var_val));
                        }};
                        setStt.getItems().add(ai);

                    }
                });

                //     String

            }
        });
        return setStt;
    }
}
