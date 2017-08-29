package cn.tendata.batch.item;

import org.springframework.batch.item.ItemProcessor;

import cn.tendata.batch.support.SpelEvaluationSupport;

public class SpelEvaluationFilteringItemProcessor<T> extends SpelEvaluationSupport implements ItemProcessor<T, T> {

    public SpelEvaluationFilteringItemProcessor(String expression) {
        super(expression);
    }

    @Override
    public T process(T item) throws Exception {
        if(getParsedExpression().getValue(item, boolean.class)){
            return null;
        }
        return item;
    }
}
