package cn.tendata.batch.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude = { BatchAutoConfiguration.class, DataSourceAutoConfiguration.class,
        WebMvcAutoConfiguration.class })
public class SpringBatchAdmin extends SpringBootServletInitializer {
    
    public static void main(String[] args) {
        SpringApplication.run(SpringBatchAdmin.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBatchAdmin.class);
    }
 
    @Configuration
    @ImportResource("classpath:/org/springframework/batch/admin/web/resources/servlet-config.xml")
    static class ServletConfig {
        
    }
    
    @Configuration
    @ImportResource("classpath:/org/springframework/batch/admin/web/resources/webapp-config.xml")
    static class WebappConfig {
        
    }
    
    @Configuration
    @ImportResource({/*"classpath:/META-INF/spring/batch/integration-context.xml",*/
        "classpath:/META-INF/spring/batch/integration-webpower-context.xml"})
    static class BatchIntegrationConfig {

    }
}
