package cn.tendata.batch.northamerica.item.file.multiline.parsing;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FieldDescriptor {

    private String name;
    private int start;
    private int end;
    private String mark;
    private boolean multi;

    public FieldDescriptor(){
        super();
    }
    
    public FieldDescriptor(String name, int start, int end, String mark, boolean multi) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.mark = mark;
        this.multi = multi;
    }

    public boolean getMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getStart() {
        return start;
    }
    
    public void setStart(int start) {
        this.start = start;
    }
    
    public int getEnd() {
        return end;
    }
    
    public void setEnd(int end) {
        this.end = end;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
    
    @Override
    public int hashCode(){
        return new HashCodeBuilder()
            .append(name)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof FieldDescriptor){
            final FieldDescriptor other = (FieldDescriptor) obj;
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
                .append("name", name).append("start", start)
                .append("end", end).append("mark", mark).toString();
    }
}
