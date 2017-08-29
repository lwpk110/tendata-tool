package cn.tendata.batch.item;

import org.springframework.batch.support.annotation.Classifier;

import cn.tendata.batch.support.SpelEvaluationSupport;

public class SpelEvaluationItemClassifier<T> extends SpelEvaluationSupport {

    public SpelEvaluationItemClassifier(String expression) {
        super(expression);
    }

    @Classifier
    public String classify(T item){
        return getParsedExpression().getValue(item, String.class);
    }
}
