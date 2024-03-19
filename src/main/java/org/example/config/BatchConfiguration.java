package org.example.config;
import org.example.constants.Constants;
import org.example.dto.ProductInput;
import org.example.listener.JobCompleteNotificationListener;
import org.example.model.Product;
import org.example.processor.ProductTransformByFileProcessor;
import org.example.processor.ProductTransformProcessor;
import org.example.reader.ProductFileReader;
import org.example.reader.ProductReader;
import org.example.writer.ProductWriter;
import org.example.writer.ProductWriterByFile;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.List;

@Configuration
public class BatchConfiguration {

    @Bean
    public ProductReader reader() {
        return new ProductReader();
    }
    @Bean
    public FlatFileItemReader<ProductInput> readerByFile() {
        return new ProductFileReader().readByFile();
    }

    @Bean
    public ProductWriter writer() {
        return new ProductWriter();
    }

    @Bean
    public ProductWriterByFile writerByFile() {
        return new ProductWriterByFile();
    }

    @Bean
    public ProductTransformProcessor productTransformProcessor() {
        return new ProductTransformProcessor();
    }

    @Bean
    public ProductTransformByFileProcessor productTransformByFileProcessor() {
        return new ProductTransformByFileProcessor();
    }

    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                      ProductReader reader, ProductTransformProcessor productTransformProcessor, ProductWriter writer) {
        return new StepBuilder("step1", jobRepository)
                .<List<ProductInput>, List<Product>> chunk(1, transactionManager)
                .reader(reader)
                .processor(productTransformProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                      FlatFileItemReader<ProductInput> readerByFile, ProductTransformByFileProcessor productTransformByFileProcessor, ProductWriterByFile writerByFile) {
        return new StepBuilder("step2", jobRepository)
                .<ProductInput, Product> chunk(3, transactionManager)
                .reader(readerByFile)
                .processor(productTransformByFileProcessor)
                .writer(writerByFile)
                .build();
    }

    @Bean(name = Constants.JOB_NAME)
    public Job importProductJob(JobRepository jobRepository, Step step1, Step step2, JobCompleteNotificationListener listener) {
        return new JobBuilder(Constants.JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(step1)
                .next(step2)
                .build();
    }
}
