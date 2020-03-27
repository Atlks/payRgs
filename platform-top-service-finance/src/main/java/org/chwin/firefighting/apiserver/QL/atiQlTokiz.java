package org.chwin.firefighting.apiserver.QL;


import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.Token;


import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

@Slf4j


public class atiQlTokiz {
    Logger log = LoggerFactory.getLogger(atiQlTokiz.class);
    private final String str;
    char[] str_a;
    int cursor = -1;
    char cur_c;
    Stack stack = new Stack();
    //List<Token> li_tokens = new ArrayList<>();

    public atiQlTokiz(String s) {
        str = s;
        str_a = s.toCharArray();
    }


    public List<Token> getListToken(String s) {
        s = s.trim();
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
            case '(':
                return leftBracket_case();
            //  break;

            case ')':
                return ritBracket_case();

            case ',':
                return comma_cash();

            default:
                return Normalchar_case();


        }
        //    cursor++;

    }

    private Token Normalchar_case() {
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


    private Token leftBracket_case() {
        Object obj = stack.pop();
        Token tk = getTokenTmp(obj);


        return tk;
    }

    private Token comma_cash() {
        Object obj = stack.pop();
        Token tk = getTokenTmp(obj);


        return tk;
    }

    private Token ritBracket_case() {
        Object obj = stack.pop();
        Token tk = getTokenTmp(obj);


        return tk;
    }

    @NotNull
    private Token getTokenTmp(Object obj) {
        Token tk = new Token();
        tk.endColumn = cursor;
        tk.beginColumn = cursor - obj.toString().length();
        tk.image = obj.toString().trim();
        stack.push(cur_c);
        return tk;
    }

    private boolean isSplitor(Object obj) {
        return ("(,)".contains(obj.toString()));

    }
}
