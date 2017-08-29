package cn.tendata.batch.item.mapping;

import cn.tendata.batch.support.ConfigOptionsResolver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.expression.Expression;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class MappingFieldResultMapper extends SpelEvaluationResultMapper {

    private final Map<String, Expression> expressionMap = new ConcurrentHashMap<>(20);
    
    private Collection<MappingField> mappingFields = Collections.emptyList();
    private ConfigOptionsResolver configOptionsResolver;
    private boolean preserveNull;
   
    public void setConfigOptionsResolver(ConfigOptionsResolver configOptionsResolver) {
        Assert.state(configOptionsResolver != null, "'configOptionsResolver' must not be null");
        this.configOptionsResolver = configOptionsResolver;
        this.mappingFields = configOptionsResolver.resolve(MappingField.class);
    }
    
    @Override
    public Map<String, Object> mapResult(Map<String, Object> item){
        if(configOptionsResolver == null){
            return item;
        }
        Map<String, Object> resultMap = new LinkedHashMap<>(item.size());
        for (MappingField field : mappingFields) {
            Object val;
            if(field.getExpression() != null){
                val = getExpressionValue(item, field);
            }
            else if(field.getSources() == null || field.getSources().isEmpty()){
                val = field.getDefaultValue() != null ? 
                        ConvertUtils.convertTypedValue(field.getDefaultValue(), field) : null;
            }
            else{
                val = getFieldValue(item, field);
            }
            if(val != null || preserveNull){
                resultMap.put(field.getName(), val);
            }
        }
        return resultMap;
    }
    
    private Expression getParseExpression(String expression){
        Expression parsedExpression = expressionMap.get(expression);
        if(parsedExpression != null){
            return parsedExpression;
        }
        parsedExpression = getExpressionParser().parseExpression(expression);
        expressionMap.put(expression, parsedExpression);
        return parsedExpression;
    }
    
    private Object getExpressionValue(Map<String, Object> item, MappingField field){
        Expression expression = getParseExpression(field.getExpression());
        Object val = expression.getValue(getEvaluationContext(), item);
        if(val != null){
            val = ConvertUtils.convertTypedValue(val, field);
        }
        return val;
    }
    
    private Object getFieldValue(Map<String, Object> item, MappingField field){
        List<Object> values = new ArrayList<>(10);
        List<?> sources = field.getSources();
        for (Object source : sources) {
            if(addValues(values, source, item, field))
                break;
        }
        if(values.isEmpty()){
            return null;
        }
        boolean isArray = sources.get(0) instanceof Collection;
        return isArray ? values.toArray() : values.get(0);
    }
    
    private boolean addValues(List<Object> values, Object obj, Map<String, Object> item, MappingField field){
        if(obj instanceof Collection){
            for (Object obj2 : (Collection<?>)obj) {
                if(addValues(values, obj2, item, field))
                    break;
            }
        }
        if(item.containsKey(obj)){
            Object val = item.get(obj);
            if(val instanceof Collection){
                final String npAddressJoin = String.join(";", (Iterable<? extends String>) val);
                if(!StringUtils.isEmpty(npAddressJoin)){
                    values.add(ConvertUtils.convertTypedValue(npAddressJoin, field));
                }
            }else {
                if(!StringUtils.isEmpty(val)){
                    values.add(ConvertUtils.convertTypedValue(val, field));
                }
                return true;
            }

        }
        return false;
    }

    public void setPreserveNull(boolean preserveNull) {
        this.preserveNull = preserveNull;
    }
}
