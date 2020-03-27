package attilax.aql;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import org.chwin.firefighting.apiserver.QL.SqlUtil;
import org.chwin.firefighting.apiserver.QL.aql设置变量Convert;
import org.chwin.firefighting.apiserver.QL.astiqlAstBuilder;
import org.chwin.firefighting.apiserver.sql.ExpressionWarp;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

public class AqlUtil {

    Stack<SQLMethodInvokeExpr> stack = new Stack();

    public static void main(String[] args) {
        String s = "查询表格(操作日志表),条件( 操作人=小新),and(时间=2018),and(事件=登录事件),排序(时间),翻页(页数=7,每页=50)";
       // s = "查询表格(操作日志表),条件( 操作人=小新)";
        //  s = "查询表格(操作日志表)";
      //  s = "查询表格,条件";
        Function fun = astiqlAstBuilder.atiql2ast(s);
        SQLMethodInvokeExpr SQLMethodInvokeExpr1 = new AqlUtil().ToCruidMethidInvokeExpV2(fun);
        System.out.println(SQLMethodInvokeExpr1);
    }

    SQLMethodInvokeExpr rootExp = new SQLMethodInvokeExpr();


    private SQLMethodInvokeExpr ToCruidMethidInvokeExpV2(Function fun) {


        //vonvert to fun2exp
        Function f = (Function) fun;
        SQLMethodInvokeExpr SQLMethodInvokeExpr_new = new SQLMethodInvokeExpr();
        SQLMethodInvokeExpr_new.setMethodName(f.getName());

        //process param
        List<Expression> list = f.getParameters().getExpressions();

        for (Expression node : list) {
            SQLExpr SQLExpr1 = convert2CuridExpAsFunParam(node);
            SQLMethodInvokeExpr_new.getParameters().add(SQLExpr1);
        }


        return SQLMethodInvokeExpr_new;


        //    return rootExp;

    }

//    private SQLMethodInvokeExpr ToCruidMethidInvokeExp(Function fun) {
//
//        // trve double case mode
//        SqlUtil.traveTree(fun, new Consumer<Function>() {
//            @Override
//            public void accept(Function cur_expression) {
//                if (stack.size() == 0) {
//                    Function f = (Function) cur_expression;
//                    rootExp.setMethodName(f.getName());
//                    stack.push(rootExp);
//                    return;
//                }
//
//                //size >1   if curExp is Fun,and curStat=funParams
//
//                //curExpIsFunCase(cur_expression);
//                SQLMethodInvokeExpr parent_SQLMethodInvokeExpr1 = stack.pop();
//
//                //vonvert to fun2exp
//                Function f = (Function) cur_expression;
//                SQLMethodInvokeExpr SQLMethodInvokeExpr_new = new SQLMethodInvokeExpr();
//                SQLMethodInvokeExpr_new.setMethodName(f.getName());
//                parent_SQLMethodInvokeExpr1.getParameters().add(SQLMethodInvokeExpr_new);
//                stack.push(SQLMethodInvokeExpr_new);
//
//                return;
//
//
//                //     System.out.println("------exp:"+ cur_expression.toString());
//            }
//        }, new Consumer<Expression>() {
//            @Override
//            public void accept(Expression cur_expression) {
//                ExpressionWarp ew = (ExpressionWarp) cur_expression;
//                //other exp ,fun params
//                //  curExpIsFunParamCase(cur_expression);
//
//                SQLMethodInvokeExpr parent_SQLMethodInvokeExpr1 = stack.pop();
//                SQLExpr CruidExp_FunParam = convert2CuridExpAsFunParam(ew.exp);
//                //vonvert to fun2exp
//                parent_SQLMethodInvokeExpr1.getParameters().add(CruidExp_FunParam);
//                if (!ew.isLastCld)  //if last  not push parent
//                    stack.push(parent_SQLMethodInvokeExpr1);
//                return;
//
//            }
//        }, true);
//        return rootExp;
//
//    }

    private SQLExpr convert2CuridExpAsFunParam(Expression cur_expression) {
        if (cur_expression instanceof StringValue) {
            SQLCharExpr e = new SQLCharExpr();
            e.setText(((StringValue) cur_expression).getValue());
            return e;
        }
        if (cur_expression instanceof LongValue) {
            SQLIntegerExpr e = new SQLIntegerExpr();
            e.setNumber(((LongValue) cur_expression).getValue());
            return e;
        }
        if(cur_expression instanceof  Function)
        {
            return ToCruidMethidInvokeExpV2((Function)cur_expression);
        }
        return null;
    }



    public static SQLStatement parseToSQLStatement(String aql) {

        if (aql.startsWith("设置变量")) {
            Function fun = astiqlAstBuilder.atiql2ast(aql);
            SQLStatement stt = new aql设置变量Convert().toSqlAst(fun);
            return stt;
        }
        return SqlUtil.getSQLStatementFromAql(aql);

    }

    public static SQLStatement getSQLStatementFromAql(String aql) {
        return SqlUtil.getSQLStatementFromAql(aql);

    }

    public static String getSQLFromAql(String aql) {
        return SqlUtil.getSQLStatementFromAql(aql).toString();

    }
}
