package org.chwin.firefighting.apiserver.dsl;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.sound.midi.Soundbank;

public class spelUtil {

    public static void main(String[] args) {
        ExpressionParser parser = new SpelExpressionParser();

        Expression exp = parser.parseExpression("new org.chwin.firefighting.apiserver.dsl.lombokTest().m1()");

        System.out.println(exp.getValue());
     //   String message = exp.getValue(String.class);
     //   System.out.println(message);

    }
}
