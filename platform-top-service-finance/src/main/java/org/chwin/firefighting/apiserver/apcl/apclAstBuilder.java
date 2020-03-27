package org.chwin.firefighting.apiserver.apcl;

import attilax.aql.AqlUtil;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.Token;
import org.chwin.firefighting.apiserver.QL.QlExpress;
import org.chwin.firefighting.apiserver.QL.SqlUtil;
import org.chwin.firefighting.apiserver.QL.atiQlTokiz;

import org.codehaus.groovy.transform.SourceURIASTTransformation;
import org.codehaus.groovy.transform.sc.StaticCompilationMetadataKeys;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

@Slf4j
public class apclAstBuilder {

    //private final String str;
    List<Token> tokenList;
    int cursor = -1;

    Token cur_token;
    Stack<Object> stack = new Stack();  //SQLMethodInvokeExpr
    List<SQLStatement> SQLStatement_list = new ArrayList<>();
    private String curStat = "ini";

    public apclAstBuilder(List<Token> li) {
        tokenList = li;
    }

    public static void main(String[] args) {

        String s = " $(from(tab),where( c=5),and(d=xxx),select(a,b,c))";
        s = "fun(a,b)";

        s = "fun(fun1(),b)";
        s = "fun(fun1(c),b)";

        s = "fun(fun1(c),b(c()))";
        s = "fun(fun1(),b)";
        s = " 表格(操作日志表),条件( 操作人=小新),and(时间=2018),查询字段(操作人,时间,事件),排序(时间),翻页(页数=1,每页=20)";
        s = " if(a>0){ $(xxxx);xxx(); }else{ haha();gaga();}";
        s = "设置变量(@用户=123,@时间=1999) ; " +
                " if @用户>0 { " +
                "查询表格(操作日志表),条件( 操作人=小新);" +
                "删除记录(操作日志表),条件( 操作人id=123);"+

                " }else{" +
                " 更新记录(操作日志表),设置字段(时间=2019,事件=完成事件),条件( 操作人id=123);" +
                "}" +
             "查询表格(操作日志表);";
        // s.substring()

        List<Token> li = new apclTokiz(s).getListToken();
        TokenUtil.foreach(li);

        //    SQLIfStatement
        //   Function fun = atiql2ast(s);
        List<SQLStatement> sttList = new apclAstBuilder(li).getAstSttList();
      // System.out.println(QlExpress.toJsonString(sttList));
     TokenUtil.foreach(sttList);
   // SqlUtil.printSqwl(sttList);

    }

    private List<SQLStatement> getAstSttList() {
        getStatmentListAst();
        return SQLStatement_list;
    }

    private void getStatmentListAst() {
        try {
            while(true){
                cur_token=null;
                cursor++;

                cur_token = tokenList.get(cursor);

                log.info("now processing  token:" + String.valueOf(cur_token));
                switch (cur_token.image) {
                    case "{":
                        leftBracket_case();
                        break;
                    case "}":
                        ritBracket_case();
                        break;
                    case ";":
                        semiColon_cash();
                        break;
                    default:
                        Normalchar_case();
                        break;
                }
            }

        } catch (IndexOutOfBoundsException e) {
            // token list over
            //--e.printStackTrace();
            //   return stack.pop();
//            Object stt_str = stack.pop();
//            if (stt_str instanceof String) {
//                SQLStatement SQLStatement1 = SqlUtil.getSQLStatementFromAql(stt_str.toString());
//                SQLStatement_list.add(SQLStatement1);
//            }


        }
   //     return SQLStatement_list;
    }
//
//    public static Function atiql2ast(String s) {
//        s = "$(" + s + ")";
//        char[] a = s.toCharArray();
//        List<Token> li = new atiQlTokiz(s).getListToken(s);
//        System.out.println(li);
//
//        return (Function) new apclAstBuilder(li).getStatmentListAst();
//    }

//
//    private Expression getStatmentListAst() {
//        try {
//            cursor++;
//
//            cur_token = tokenList.get(cursor);
//
//
//            log.info("now char:" + String.valueOf(cur_token));
//            switch (cur_token.image) {
//                case "{":
//                    return leftBracket_case();
//                case "}":
//                    return ritBracket_case();
//                case ";":
//                    return semiColon_cash();
//                default:
//                    return Normalchar_case();
//            }
//        } catch (IndexOutOfBoundsException e) {
//            // token list over
//            //--e.printStackTrace();
//            return stack.pop();
//
//        }
//
//    }

    //...
    Logger log = LoggerFactory.getLogger(atiQlTokiz.class);
    /**
     * none ,only add to last push stack
     *
     * @return
     */
    private void Normalchar_case() {

        log.info(" token:"+cur_token +" curstat:"+curStat);
        try {
            Object obj = stack.pop();



            if (obj instanceof NullValue) {
                //pop ...
            } else
                stack.push(obj);   //include else keyword

            if(cur_token.toString().trim().startsWith("if"))
                System.out.println("0");

            if(  curStat.equals("ifBlockEnd") )
            {


            }

            stack.push(cur_token.image);
            getStatmentListAst();


        } catch (EmptyStackException ex) {  //first stt
            //     Expression e = newtStringValue();
            stack.push(cur_token.image);

            getStatmentListAst();
        }


    }

    @NotNull
    private StringValue newtStringValue(String s) {
        StringValue aaa = new StringValue("aaa");
        aaa.setValue(s);
        return aaa;
    }

    // last is Function,or  the normal Stringvalue Expression
    private void leftBracket_case() {
        String obj_last = (String) stack.pop();  //if fun   if a>1
        // if (obj instanceof StringValue) {
        if (obj_last.trim().startsWith("if")) {
            //   Function f = SqlUtil
            SQLIfStatement ifstt = new SQLIfStatement();
            SQLExpr cond = SqlUtil.getCond(obj_last);
            ifstt.setCondition(cond);
            stack.push(ifstt);
            SQLStatement_list.add(ifstt);
            curStat = "ifBlockStart";


        } else if (obj_last.trim().equals("else")) {
            SQLIfStatement ifstt = (SQLIfStatement) stack.pop();
            SQLIfStatement.Else elseItem = new SQLIfStatement.Else();
            ifstt.setElseItem(elseItem);
            stack.push(ifstt);
            stack.push(elseItem);
            //   SQLIfStatement.Else elseBlock= ifstt.getElseItem();

            curStat = "elseModeStart";

        }


    }


    private void semiColon_cash() {
        Object obj = stack.pop();   //must stt string..
        SQLStatement SQLStatement1 = AqlUtil.parseToSQLStatement(obj.toString());
        //   Function f = (Function) stack.pop();
        //     f.getParameters().getExpressions().add(obj);
        stack.push(SQLStatement1);
        SQLStatement_list.add(SQLStatement1);
        //  return getStatmentListAst();
    }

    //          s = "fun(fun1(),b)";
    private void ritBracket_case() {

        if (curStat.equals("ifBlockStart")) {
            String stts = (String) stack.pop();   //pop cur sub function  ,or sub str exp
            SQLIfStatement SQLIfStatement1 = (SQLIfStatement) stack.pop();
            List<SQLStatement> li = SqlUtil.getLI_SQLStatement_ifBlock(stts);

            li.forEach(new Consumer<SQLStatement>() {
                @Override
                public void accept(SQLStatement sqlStatement) {
                    SQLIfStatement1.addStatement(sqlStatement);
                }
            });
            stack.push(SQLIfStatement1);
            curStat = "ifBlockEnd";
        }

        if (curStat.equals("elseModeStart")) {
            String stts = (String) stack.pop();   //pop cur sub function  ,or sub str exp
            SQLIfStatement.Else elseBlock = (   SQLIfStatement.Else) stack.pop();
            List<SQLStatement> li = SqlUtil.getLI_SQLStatement_ifBlock(stts);

            elseBlock.setStatements(li);
          //  SQLIfStatement1.getElseItem().setStatements(li);
            curStat = "elseEnd";
           // SQLStatement_list.add(SQLIfStatement1);

        }


//        if (obj instanceof NullValue) {
//
//        } else {
//            Function f = (Function) stack.pop();
//            f.getParameters().getExpressions().add(obj);
//            stack.push(f);
//        }


        getStatmentListAst();
    }


}
