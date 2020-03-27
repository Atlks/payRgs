package org.chwin.firefighting.apiserver.QL;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.Token;
import net.sf.jsqlparser.schema.Column;
import org.hibernate.criterion.NullExpression;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

@Slf4j
public class astiqlAstBuilder {

    //private final String str;
    List<Token> tokenList;
    int cursor = -1;

    Token cur_token;
    Stack<Expression> stack = new Stack();

    public astiqlAstBuilder(List<Token> li) {
        tokenList = li;
    }

    public static void main( String[] args) {

        String s = " $(from(tab),where( c=5),and(d=xxx),select(a,b,c))";
        s = "fun(a,b)";

        s = "fun(fun1(),b)";
        s = "fun(fun1(c),b)";

        s = "fun(fun1(c),b(c()))";
        s = "fun(fun1(),b)";
        s = " 表格(操作日志表),条件( 操作人=小新),and(时间=2018),查询字段(操作人,时间,事件),排序(时间),翻页(页数=1,每页=20)";
       // s.substring()
        Function fun = atiql2ast(s);


        System.out.println(QlExpress.toJsonString(fun));

    }

    public static Function atiql2ast(String s) {
        s = "$(" + s + ")";
        char[] a = s.toCharArray();
        List<Token> li = new atiQlTokiz(s).getListToken(s);
        System.out.println(li);

        return (Function) new astiqlAstBuilder(li).getFuntionAst();
    }

    Logger log = LoggerFactory.getLogger(atiQlTokiz.class);
    private Expression getFuntionAst() {
        try {
            cursor++;

            cur_token = tokenList.get(cursor);


            log.info("now char:" + String.valueOf(cur_token));
            switch (cur_token.image) {
                case "(":
                    return leftBracket_case();
                case ")":
                    return ritBracket_case();
                case ",":
                    return comma_cash();
                default:
                    return Normalchar_case();
            }
        } catch (IndexOutOfBoundsException e) {
            // token list over
            //--e.printStackTrace();
            return stack.pop();

        }

    }

    //...

    /**
     * none ,only add to last push stack
     *
     * @return
     */
    private Expression Normalchar_case() {
        try {
            Expression obj = stack.pop();
            if (obj instanceof NullValue) {
                //pop ...
            } else
                stack.push(obj);

            StringValue aaa = newtStringValue(cur_token.image);
            Expression e = aaa;
            stack.push(e);
            return getFuntionAst();


        } catch (EmptyStackException ex) {
            Expression e = newtStringValue(cur_token.image);
            stack.push(e);

            return getFuntionAst();
        }

    }

    @NotNull
    private StringValue newtStringValue(String s) {
        StringValue aaa = new StringValue("aaa");
        aaa.setValue(s);
        return aaa;
    }

    // last is Function,or  the normal Stringvalue Expression
    private Expression leftBracket_case() {
        Expression obj = stack.pop();
        // if (obj instanceof StringValue) {
        Function f = new Function();
        f.setName(((StringValue) obj).getValue());
        ExpressionList ExpressionList1 = new ExpressionList();
        ExpressionList1.setExpressions(new ArrayList<>());
        f.setParameters(ExpressionList1);
        //  Expression tk = getTokenTmp(obj);
        stack.push(f);
        stack.push(new NullValue());


        return getFuntionAst();
    }


    private Expression comma_cash() {
        Expression obj = stack.pop();
        Function f = (Function) stack.pop();
        f.getParameters().getExpressions().add(obj);
        stack.push(f);

        return getFuntionAst();
    }

    //          s = "fun(fun1(),b)";
    private Expression ritBracket_case() {
        Expression obj = stack.pop();   //pop cur sub function  ,or sub str exp
        if (obj instanceof NullValue) {

        } else {
            Function f = (Function) stack.pop();
            f.getParameters().getExpressions().add(obj);
            stack.push(f);
        }


        return getFuntionAst();
    }


}
