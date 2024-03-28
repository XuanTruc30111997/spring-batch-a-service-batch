package org.example.config;
import org.example.constants.Constants;
import org.example.dto.ProductInput;
import org.example.listener.JobCompleteNotificationListener;
import org.example.listener.Step1Listener;
import org.example.model.Product;
import org.example.processor.ProductTransformByFileProcessor;
import org.example.processor.ProductTransformProcessor;
import org.example.reader.ProductFileReader;
import org.example.reader.ProductReader;
import org.example.skip.Step1SkipPolicy;
import org.example.tasks.OnSkipTask;
import org.example.tasks.OnStopTask;
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
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

import static org.example.constants.Constants.*;

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
                      ProductReader reader, ProductTransformProcessor productTransformProcessor, ProductWriter writer, Step1Listener step1Listener) {
        return new StepBuilder("step1", jobRepository)
                .<List<ProductInput>, List<Product>> chunk(1, transactionManager)
                .reader(reader)
                .processor(productTransformProcessor)
                .writer(writer)
                .listener(step1Listener)
                .faultTolerant()
                .skipPolicy(new Step1SkipPolicy())
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

    @Bean
    public Step onStop(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("onStop", jobRepository)
                .tasklet(new OnStopTask(), transactionManager)
                .build();
    }

    @Bean
    public Step onSkip(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("onSkip", jobRepository)
                .tasklet(new OnSkipTask(), transactionManager)
                .build();
    }

    @Bean(name = Constants.JOB_NAME)
    public Job importProductJob(JobRepository jobRepository, Step step1, Step step2, Step onStop, Step onSkip, JobCompleteNotificationListener listener) {
        return new JobBuilder(Constants.JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(step1).on(ERROR_STATUS).end()
                .from(step1).on(SKIP_STATUS).to(onSkip)
                .from(step1).on(ERROR_FAIL_STATUS).fail()
                .from(step1).on("*").to(step2)
                .end()
                .build();
    }
}
