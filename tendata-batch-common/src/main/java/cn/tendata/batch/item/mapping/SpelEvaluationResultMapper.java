package cn.tendata.batch.item.mapping;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public abstract class SpelEvaluationResultMapper implements ResultMapper, InitializingBean, BeanFactoryAware {

    private BeanFactory beanFactory;
    
    private volatile StandardEvaluationContext evaluationContext;
    
    private ExpressionParser expressionParser = new SpelExpressionParser();
    
    @Override
    public void afterPropertiesSet() throws Exception {
        this.evaluationContext = createStandardEvaluationContext(beanFactory);
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    
    public ExpressionParser getExpressionParser() {
        return expressionParser;
    }
    
    public StandardEvaluationContext getEvaluationContext() {
        return evaluationContext;
    }

    public void setEvaluationContext(StandardEvaluationContext evaluationContext) {
        this.evaluationContext = evaluationContext;
    }
    
    private static StandardEvaluationContext createStandardEvaluationContext(BeanFactory beanFactory){
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext(); 
        evaluationContext.addPropertyAccessor(new MapAccessor());
        if (beanFactory != null) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        return evaluationContext;
    }
}
