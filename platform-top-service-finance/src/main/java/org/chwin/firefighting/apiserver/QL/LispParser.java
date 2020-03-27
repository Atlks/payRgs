package org.chwin.firefighting.apiserver.QL;

import java.util.Stack;

public class LispParser {

    public static void main(String[] args) {

        Stack stack=new Stack();
        String s=" from(tab),where( c=5),and(d=6),select(a,b,c)";
        char[] a=s.toCharArray();
        for(char c :a)
        {
            String cs=  String.valueOf(c);
            if(cs=="(")
            {

            }else if(cs==")")
            {

            }else if(cs==",")
            {

            }else{
                //normal char
                stack.push(cs);
            }
        }


    }
}
