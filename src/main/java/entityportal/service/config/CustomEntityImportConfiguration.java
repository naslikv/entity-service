package entityportal.service.config;

import entityportal.service.model.Entity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class CustomEntityImportConfiguration {

    public FlatFileItemReader<Entity> customEntityFlatFileItemReader(String path) {
        return new FlatFileItemReaderBuilder<Entity>()
                .name("customEntityReader")
                .resource(new FileSystemResource(path))
                .delimited()
                .names("date", "time", "type", "value")
                .fieldSetMapper(new EntityFieldSetMapper())
                .linesToSkip(1)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Entity> customEntityWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Entity>()
                .sql("INSERT INTO custom_entity (Value,Date,Type,CreatedBy) VALUES (:value, :timeStamp, :type, 1)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    public CustomEntityProcessor customEntityProcessor(){
        return new CustomEntityProcessor();
    }

    public Step customEntityReadStep(JobRepository jobRepository,
                                     JpaTransactionManager jpaTransactionManager,
                                     FlatFileItemReader<Entity> reader,
                                     CustomEntityProcessor processor,
                                     JdbcBatchItemWriter<Entity> writer){
        return new StepBuilder("customEntityReadStep",jobRepository)
                .<Entity, Entity>chunk(100000,jpaTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    public Job customEntityImportJob(JobRepository jobRepository,
                                     Step customEntityReadStep){
        return new JobBuilder("customEntityImportJob",
                jobRepository)
                .start(customEntityReadStep)
                .build();
    }
}
