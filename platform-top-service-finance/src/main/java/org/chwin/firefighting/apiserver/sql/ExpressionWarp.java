package org.chwin.firefighting.apiserver.sql;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

public class ExpressionWarp implements Expression {

    public  boolean isLastCld=false;
    public Expression exp;

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {

    }
}
