package cn.tendata.batch.northamerica.item.file.multiline;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;

public class AggregateItemFieldSetMapper<T> implements FieldSetMapper<AggregateItem<T>>, InitializingBean {
    
    private final ExpressionParser parser = new SpelExpressionParser();
    
    private FieldSetMapper<T> delegate;
    
    private Expression beginExpression;
    
    private Expression endExpression;
    
    public void setDelegate(FieldSetMapper<T> delegate) {
        this.delegate = delegate;
    }
    
    public void setBeginExpression(String beginExpression) {
        this.beginExpression = parser.parseExpression(beginExpression);
    }

    public void setEndExpression(String endExpression) {
        this.endExpression = parser.parseExpression(endExpression);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(delegate, "A FieldSetMapper delegate must be provided.");
        Assert.notNull(beginExpression, "'beginExpression' must not be null");
    }

    @Override
    public AggregateItem<T> mapFieldSet(FieldSet fieldSet) throws BindException {
        T item = delegate.mapFieldSet(fieldSet);
        if (beginExpression.getValue(fieldSet, boolean.class)) {
            return new AggregateItem<>(item, true, false);
        }
        if(endExpression != null && endExpression.getValue(fieldSet, boolean.class)){
            return new AggregateItem<>(item, false, true);
        }
        return new AggregateItem<>(item);
    }
}
