package cn.tendata.batch.northamerica.config;

import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.support.SingleItemPeekableItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import cn.tendata.batch.item.file.transform.Map2JSONLineAggregator;
import cn.tendata.batch.northamerica.item.file.multiline.AggregateItem;
import cn.tendata.batch.northamerica.item.file.multiline.AggregateItemFieldSetMapper;
import cn.tendata.batch.northamerica.item.file.multiline.AggregateItemProcessor;
import cn.tendata.batch.northamerica.item.file.multiline.AggregateItemReader;
import cn.tendata.batch.northamerica.item.file.multiline.FixedLengthCompositeLineTokenizer;
import cn.tendata.batch.support.YamlConfigOptionsResolver;
import cn.tendata.batch.util.JobParameterUtils;

@Configuration
public class NorthamericaImportsJsonFileStoreJobConfiguration {

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    @Bean
    public Job northamericaImportsJsonFileStoreJob(){
        return jobBuilderFactory.get("northamericaImportsJsonFileStoreJob")
                .start(northamericaImportsJsonFileStoreStep1()).build();
    }
    
    @Bean
    public Step northamericaImportsJsonFileStoreStep1(){
        return stepBuilderFactory.get("northamericaImportsJsonFileStoreStep1")
            .<List<FieldSet>, Map<String, Object>>chunk(1)
            .reader(northamericaImportsItemReader())
            .processor(northamericaImportsItemProcessor())
            .writer(northamericaImportsItemWriter(null))
            .stream(fileItemReader(null))
            .build();
    }
    
    @Bean
    public AggregateItemReader<FieldSet> northamericaImportsItemReader(){
        AggregateItemReader<FieldSet> itemReader = new AggregateItemReader<>();
        SingleItemPeekableItemReader<AggregateItem<FieldSet>> peekableItemReader = new SingleItemPeekableItemReader<>();
        peekableItemReader.setDelegate(fileItemReader(null));
        itemReader.setDelegate(peekableItemReader);
        return itemReader;
    }
    
    @Bean
    @StepScope
    public FlatFileItemReader<AggregateItem<FieldSet>> fileItemReader(
            @Value("#{jobParameters}")Map<String, Object> jobParameters){
        FlatFileItemReader<AggregateItem<FieldSet>> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(resourceLoader.getResource(JobParameterUtils.getInputPathToFile(jobParameters)));
        DefaultLineMapper<AggregateItem<FieldSet>> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(fixedLengthCompositeLineTokenizer());
        AggregateItemFieldSetMapper<FieldSet> fieldSetMapper = new AggregateItemFieldSetMapper<>();
        fieldSetMapper.setDelegate(new PassThroughFieldSetMapper());
        fieldSetMapper.setBeginExpression("#root.readString(0) == '00'");
        lineMapper.setFieldSetMapper(fieldSetMapper);
        itemReader.setLineMapper(lineMapper);
        return itemReader;
    }
    
    private LineTokenizer fixedLengthCompositeLineTokenizer(){
        YamlConfigOptionsResolver resolver = new YamlConfigOptionsResolver();
        resolver.setResource(resourceLoader.getResource("classpath:northamerica/imports/parse.yml"));
        return new FixedLengthCompositeLineTokenizer(resolver);
    }
    
    @Bean
    public ItemProcessor<List<FieldSet>, Map<String, Object>> northamericaImportsItemProcessor(){
        return new AggregateItemProcessor();
    }
    
    @Bean 
    @StepScope
    public FlatFileItemWriter<Map<String, Object>> northamericaImportsItemWriter(
            @Value("#{jobParameters}")Map<String, Object> jobParameters){
        FlatFileItemWriter<Map<String, Object>> itemWriter = new FlatFileItemWriter<>();
        itemWriter.setResource(resourceLoader.getResource(JobParameterUtils.getOutputPathToFile(jobParameters, ".json")));
        Map2JSONLineAggregator lineAggregator = new Map2JSONLineAggregator();
        itemWriter.setLineAggregator(lineAggregator);
        return itemWriter;
    }
}
