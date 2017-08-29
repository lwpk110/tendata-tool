package cn.tendata.batch.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.integration.config.EnableIntegration;

@Configuration
@EnableIntegration
@ImportResource("classpath:/META-INF/spring/batch/integration-context.xml")
public class BatchIntegrationConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
