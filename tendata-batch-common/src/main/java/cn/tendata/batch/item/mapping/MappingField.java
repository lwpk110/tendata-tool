package cn.tendata.batch.item.mapping;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MappingField {

    public enum FieldType {
        DATETIME,INT,LONG,FLOAT,DOUBLE,STR,BOOL
    }
    
    private String name;
    private FieldType type;
    private String[] patterns;
    private List<?> sources;
    private String expression;
    private Object defaultValue;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public FieldType getType() {
        return type;
    }
    
    public void setType(FieldType type) {
        this.type = type;
    }
    
    public String[] getPatterns() {
        return patterns;
    }
    
    public void setPatterns(String[] patterns) {
        this.patterns = patterns;
    }
    
    public List<?> getSources() {
        return sources;
    }
    
    public void setSources(List<?> sources) {
        this.sources = sources;
    }
    
    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
            .append(name)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof MappingField){
            final MappingField other = (MappingField) obj;
            return new EqualsBuilder()
                .append(name, other.name)
                .isEquals();
        } else{
            return false;
        }
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString())
                .append("name", name).append("type", type)
                .append("sources", sources).toString();
    }
}
