package org.chwin.firefighting.apiserver.QL;


import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.Token;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

@Slf4j
public class AtiQLParser {



    public static void main(String[] args) {

       // 输出( $(jpql,转换sql结果, 翻页设置是第(3).页, 每页显示(30).条数据);


        Stack stack = new Stack();
        String s = " $(from(tab),where( c=5),and(d=xxx),select(a,b,c))";

        char[] a = s.toCharArray();
        List<Token> li = new atiQlTokiz(s).getListToken(s);
        System.out.println(li);


    }

}


