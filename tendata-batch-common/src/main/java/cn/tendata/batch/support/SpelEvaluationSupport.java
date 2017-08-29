package cn.tendata.batch.support;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public abstract class SpelEvaluationSupport {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final Expression parsedExpression;
    
    public SpelEvaluationSupport(String expression){
        this.parsedExpression = parser.parseExpression(expression);
    }
    
    public ExpressionParser getParser() {
        return parser;
    }

    public Expression getParsedExpression() {
        return parsedExpression;
    }
}
