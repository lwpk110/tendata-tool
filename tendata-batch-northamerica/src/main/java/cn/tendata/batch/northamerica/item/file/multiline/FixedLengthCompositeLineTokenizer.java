package cn.tendata.batch.northamerica.item.file.multiline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.support.PatternMatcher;
import org.springframework.util.Assert;

import cn.tendata.batch.northamerica.item.file.multiline.parsing.FieldDescriptor;
import cn.tendata.batch.support.ConfigOptionsResolver;

public class FixedLengthCompositeLineTokenizer implements LineTokenizer {

    public static final String LINE_MARK_NAME = "__MARK";

    public static final String MULTI_MARK = "_";
    
    private final PatternMatcher<LineTokenizer> tokenizers;

    public FixedLengthCompositeLineTokenizer(ConfigOptionsResolver resolver) {
        Assert.notNull(resolver, "The 'resolver' must not be null");
        this.tokenizers = new PatternMatcher<>(resolveMap(resolver.resolve(FieldDescriptor.class)));
    }
    
    @Override
    public FieldSet tokenize(String line) {
        return tokenizers.match(line).tokenize(line);
    }

    private Map<String, LineTokenizer> resolveMap(Collection<FieldDescriptor> fieldDescriptors){
        Map<String, FixedLengthLineDescriptor> lineMap = new LinkedHashMap<>();
        for (FieldDescriptor fieldDescriptor : fieldDescriptors) {
            FixedLengthLineDescriptor lineDescriptor = lineMap.get(fieldDescriptor.getMark()); 
            if(lineDescriptor == null){
                lineDescriptor = new FixedLengthLineDescriptor(fieldDescriptor.getMark());
                lineDescriptor.add(LINE_MARK_NAME, 1, 2);
                lineMap.put(fieldDescriptor.getMark(), lineDescriptor);
            }
            String name = fieldDescriptor.getName();
            if(fieldDescriptor.getMulti()){
                name = MULTI_MARK + name;
            }
            lineDescriptor.add(name, fieldDescriptor.getStart(), fieldDescriptor.getEnd());
        }
        Map<String, LineTokenizer> tokenizerMap = new LinkedHashMap<>(lineMap.size());
        for (FixedLengthLineDescriptor lineDescriptor : lineMap.values()) {
            FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
            tokenizer.setNames(lineDescriptor.getNames());
            tokenizer.setColumns(lineDescriptor.getRanges());
            tokenizerMap.put(lineDescriptor.getMark() + "*", tokenizer);
        }
        return tokenizerMap;
    }

    private void addMultiMark(){

    }

    static class FixedLengthLineDescriptor {
        
        private final String mark;
        private final List<String> names = new ArrayList<>(50);
        private final List<Range> ranges = new ArrayList<>(50);
        
        public FixedLengthLineDescriptor(String mark) {
            this.mark = mark;
        }

        public String getMark() {
            return mark;
        }

        public String[] getNames() {
            return names.toArray(new String[0]);
        }
        
        public Range[] getRanges() {
            return ranges.toArray(new Range[0]);
        }
        
        public void add(String name, int start, int end){
            names.add(name);
            ranges.add(new Range(start, end));
        }
    }
}
