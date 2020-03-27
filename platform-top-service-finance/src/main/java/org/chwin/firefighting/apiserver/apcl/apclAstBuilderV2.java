package org.chwin.firefighting.apiserver.apcl;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.parser.Token;
import org.chwin.firefighting.apiserver.QL.SqlUtil;
import org.chwin.firefighting.apiserver.QL.atiQlTokiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

@Slf4j
public class apclAstBuilderV2 {

    public static final String ELSE_block_START_stat = "elseModeStart";
    private final String elseBlockEndStat = "elseBlockEndStat";
    private final String thenBlockStart = "ifBlockStart";
    public static final String thenBlockEnd = "ifBlockEnd";
    private final String keywords_if = "if";
    private final String keywordElse = "else";
    //private final String str;
    List<Token> tokenList;
    int cursor = -1;

    Token cur_token;
    Stack<Object> stack = new Stack();  //SQLMethodInvokeExpr
    List<SQLStatement> SQLStatement_list = new ArrayList<>();
    private String curStat = "ini";


    public apclAstBuilderV2(List<Token> li) {
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
                "删除记录(操作日志表),条件( 操作人id=123);" +

                " }else{" +
                " 更新记录(操作日志表),设置字段(时间=2019,事件=完成事件),条件( 操作人id=123);" +
                "}" +
                "查询表格(操作日志表);";
        // s.substring()

        List<Token> li = new apclTokiz(s).getListToken();
        TokenUtil.foreach(li);

        //    SQLIfStatement
        //   Function fun = atiql2ast(s);
        List<SQLStatement> sttList = new apclAstBuilderV2(li).getAstSttList();
        // System.out.println(QlExpress.toJsonString(sttList));
        //    TokenUtil.foreach(sttList);
        SqlUtil.printSql4dbg(sttList);

    }

    private List<SQLStatement> getAstSttList() {
        getStatmentListAst();
        return SQLStatement_list;
    }

    private void getStatmentListAst() {
        try {
            while (true) {
                cur_token = null;
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


    //...
    Logger log = LoggerFactory.getLogger(atiQlTokiz.class);

    /**
     * none ,only add to last push stack
     *
     * @return
     */
    private void Normalchar_case() {

        log.info(" token:" + cur_token + " curstat:" + curStat);
        try {
            Object obj = stack.pop();


            if (obj instanceof NullValue) {
                //pop ...
            } else
                stack.push(obj);   //include else keyword

            if (cur_token.toString().trim().startsWith(keywords_if))
                System.out.println("0");

            if (curStat.equals(thenBlockEnd)) {


            }

            stack.push(cur_token.image);
            getStatmentListAst();


        } catch (EmptyStackException ex) {  //first stt
            //     Expression e = newtStringValue();
            stack.push(cur_token.image);

            getStatmentListAst();
        }


    }


    // last is Function,or  the normal Stringvalue Expression
    private void leftBracket_case() {

        String obj_last = (String) stack.pop();  //if fun   if a>1
        // if (obj instanceof StringValue) {
        if (obj_last.trim().startsWith(keywords_if)) {
            //   Function f = SqlUtil
            SQLIfStatement ifstt = new SQLIfStatement();
            SQLExpr cond = SqlUtil.getCond(obj_last);
            ifstt.setCondition(cond);
            stack.push(ifstt);
            SQLStatement_list.add(ifstt);
            curStat = thenBlockStart;


        } else if (obj_last.trim().equals(keywordElse)) {
            SQLIfStatement ifstt = (SQLIfStatement) stack.pop();
            SQLIfStatement.Else elseItem = new SQLIfStatement.Else();
            ifstt.setElseItem(elseItem);
            //     stack.push(ifstt);
            stack.push(elseItem);
            //   SQLIfStatement.Else elseBlock= ifstt.getElseItem();

            curStat = ELSE_block_START_stat;

        }


    }


    private void semiColon_cash() {
        Object obj = stack.pop();   //must stt string..
        OtherLanStatment othenLanstt_strfmt = new OtherLanStatment(obj.toString());


        Object LastStrutsBlock;
        try {
            LastStrutsBlock = stack.pop();
        } catch (EmptyStackException e) {
            //first
            LastStrutsBlock = SQLStatement_list;
        }

//then block start
        if (LastStrutsBlock instanceof SQLIfStatement) {
            SQLIfStatement SQLIfStatement1 = (SQLIfStatement) LastStrutsBlock;
            SQLIfStatement1.addStatement(othenLanstt_strfmt);
            stack.push(SQLIfStatement1);


        }

        //else block start
        else if (LastStrutsBlock instanceof SQLIfStatement.Else) {
            SQLIfStatement.Else elseStt = (SQLIfStatement.Else) LastStrutsBlock;
            elseStt.getStatements().add(othenLanstt_strfmt);
            stack.push(elseStt);
        } else { //default  normal stat
            //   OtherLanStatment stt_string = new OtherLanStatment(obj.toString());
            //   SQLExplainStatement stt_string = new SQLExplainStatement(obj.toString());
            //      MySqlExplainStatement
            stack.push(SQLStatement_list);
            //   stack.push(othenLanstt_strfmt);
            SQLStatement_list.add(othenLanstt_strfmt);
        }

    }

    //          s = "fun(fun1(),b)";
    private void ritBracket_case() {

        if (curStat.equals(thenBlockStart)) {
            Object pop = stack.pop();
            if (pop instanceof SQLIfStatement) {
                stack.push(pop); //  contune to add if Block for else...
                curStat = thenBlockEnd;

                return;
            }
            String stts = (String) pop;   //pop cur sub function  ,or sub str exp
            SQLIfStatement SQLIfStatement1 = (SQLIfStatement) stack.pop();

            OtherLanStatment stt_string = new OtherLanStatment(stts.toString());
            SQLIfStatement1.addStatement(stt_string);
            stack.push(SQLIfStatement1);
            curStat = thenBlockEnd;
        }

        if (curStat.equals(ELSE_block_START_stat)) {
            Object pop = stack.pop();

            if (pop instanceof SQLIfStatement.Else) {
                curStat = elseBlockEndStat;
                return;
            }
            String stts = (String) pop;  //

            SQLIfStatement.Else elseBlock = (SQLIfStatement.Else) stack.pop();

            OtherLanStatment stt_string = new OtherLanStatment(stts.toString());

            if (elseBlock.getStatements() == null) {
                List<SQLStatement> li = new ArrayList() {{

                }};
                elseBlock.setStatements(li);
            }

            elseBlock.getStatements().add(stt_string);

            //  SQLIfStatement1.getElseItem().setStatements(li);
            curStat = elseBlockEndStat;
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
