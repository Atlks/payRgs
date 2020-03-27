package org.chwin.firefighting.apiserver.apcl;

import com.alibaba.fastjson.JSON;
import jdk.nashorn.internal.parser.TokenKind;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.Token;
import org.chwin.firefighting.apiserver.QL.atiQlTokiz;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

@Slf4j
public class apclTokiz {

//    public apclTokiz() {
//        str = null;
////        str = s;
////        str_a = s.toCharArray();
//    }

    public static void main(String[] args) {
        //  TokenKind
        //  Object kind;
        //    kind
        String s = "good(); 设置变量(@用户=123,@时间=1999) ;  if(a>0){ $(xxxx);xxx(); }else{ haha();gaga();} ilike() ";
        List<Token> li = new apclTokiz(s).getListToken();
        li.forEach(new Consumer<Token>() {
            @Override
            public void accept(Token token) {
                System.out.println(token);
            }
        });
        //   System.out.println(JSON.toJSONString(li, true));
    }

    public String s_splitor_es = "{};";
    public String keywords = "if,else,then";
    private final String str;
    char[] str_a;
    int cursor = -1;
    char cur_c;
    Stack stack = new Stack();
    String curStat = "ini";
    //List<Token> li_tokens = new ArrayList<>();

    public apclTokiz(String s) {
        str = s.trim();
        str_a = s.toCharArray();
    }


    public List<Token> getListToken() {
        //   s = s.trim();
        List<Token> li = new ArrayList<>();
        Token tk;
        try {
            while (true) {
                tk = getToken();
                if (tk.image.equals(")"))
                    System.out.println("d");
                if (tk.image.trim().length() == 0)
                    continue;
                if (tk.image == ",")
                    continue;
                li.add(tk);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            //    li.add(tk);
            li.add(getTokenTmp(stack.pop()));
            return li;
        }

    }

    // only (,)
    private Token getToken() {
        cursor++;

        //   try{
        cur_c = str_a[cursor];


        log.info("now char:" + String.valueOf(cur_c));
        switch (cur_c) {
//            case '(':
//                return leftBracket_case();
//            //  break;
//
//            case ')':
//                return ritBracket_case();

            case '{':
                return leftBigBracket();
            case '}':
                return ritBigBracket();

            case ';':
                return semicolon分号处理流程();

            default:
                return Normalchar_case();


        }
        //    cursor++;

    }
   // Logger log = LoggerFactory.getLogger(atiQlTokiz.class);
    Logger log = LoggerFactory.getLogger(atiQlTokiz.class);
    private Token semicolon分号处理流程() {


        switch (curStat) {


            case "leftBigBracketStart___none":
                Object obj = stack.pop();
                String nowtokenStr = obj.toString() + String.valueOf(cur_c);
                stack.push(nowtokenStr);
                return getToken();


            default:
                Object obj2 = stack.pop();
                Token tk = getTokenTmp(obj2);

                return tk;
        }
    }

        private Token leftBigBracket () {

            switch (curStat) {
                case "ini":

                    Object obj = stack.pop();
                    Token tk = getTokenTmp(obj);

                    curStat = "leftBigBracketStart";
                    return tk;
                //    break;

                case "leftBigBracketStart":

                    break;
                case "leftBigBracketEnd":

                    Object obj2 = stack.pop();
                    Token tk2 = getTokenTmp(obj2);


                    curStat = "leftBigBracketStart";
                    return tk2;
                //       break;


                default: {
                    return null;
                }


            }


            return null;
        }

        private Token ritBigBracket () {

            Object obj = stack.pop();
            Token tk = getTokenTmp(obj);


            curStat = "leftBigBracketEnd";
            return tk;
        }

        private Token Normalchar_case () {
            try {
                Object obj = stack.pop();

                if (isSplitor(obj)) {
                    Token tk = getTokenTmp(obj);


                    return tk;
                }
                String nowtokenStr = obj.toString() + String.valueOf(cur_c);
                stack.push(nowtokenStr);
                return getToken();


            } catch (EmptyStackException e) {
                stack.push(String.valueOf(cur_c));
                return getToken();
            }

        }

        // ((((((((
        private Token leftBracket_case () {
            switch (curStat) {

                case "leftBigBracketStart":
                    Object obj = stack.pop();
                    String nowtokenStr = obj.toString() + String.valueOf(cur_c);
                    stack.push(nowtokenStr);
                    return getToken();
                //      break;

                default:
                    Object obj2 = stack.pop();
                    Token tk = getTokenTmp(obj2);
                    return tk;


            }
        }

        private Token comma_cash () {
            Object obj = stack.pop();
            Token tk = getTokenTmp(obj);


            return tk;
        }

        private Token ritBracket_case () {
            switch (curStat) {

                case "leftBigBracketStart":
                    Object obj = stack.pop();
                    String nowtokenStr = obj.toString() + String.valueOf(cur_c);
                    stack.push(nowtokenStr);
                    return getToken();
                //      break;

                default:
                    Object obj2 = stack.pop();
                    Token tk = getTokenTmp(obj2);
                    return tk;


            }
        }

        @NotNull
        private Token getTokenTmp (Object obj){
            Token tk = new Token();
            tk.endColumn = cursor;
            tk.beginColumn = cursor - obj.toString().length();
            tk.image = obj.toString().trim();
            stack.push(cur_c);
            return tk;
        }

        private boolean isSplitor (Object obj){

            return (s_splitor_es.contains(obj.toString()));

        }
    }
