package org.example.config;
import org.example.constants.Constants;
import org.example.dto.ProductInput;
import org.example.listener.JobCompleteNotificationListener;
import org.example.model.Product;
import org.example.processor.ProductTransformByFileProcessor;
import org.example.processor.ProductTransformProcessor;
import org.example.reader.ProductFileReader;
import org.example.reader.ProductReader;
import org.example.tasks.LogTask;
import org.example.writer.ProductWriter;
import org.example.writer.ProductWriterByFile;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.List;

@Configuration
public class BatchConfiguration {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    DataSourceTransactionManager transactionManager;

    @Autowired
    ProductReader reader;

    @Autowired
    ProductFileReader productFileReader;

    @Bean
    public FlatFileItemReader<ProductInput> readerByFile() {
        return new ProductFileReader().readByFile();
    }

    @Autowired
    ProductWriter writer;

    @Autowired
    ProductWriterByFile writerByFile;

    @Autowired
    ProductTransformProcessor productTransformProcessor;

    @Autowired
    ProductTransformByFileProcessor productTransformByFileProcessor;

    @Autowired
    LogTask logTask;

    @Bean
    public Flow flow1() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<Flow>("flow1");

        flowBuilder.start(step1()).next(step2()).end();

        return flowBuilder.build();
    }

    @Bean
    public Flow flow2() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<Flow>("flow2");

        flowBuilder.start(step3()).end();

        return flowBuilder.build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .<List<ProductInput>, List<Product>> chunk(1, transactionManager)
                .reader(reader)
                .processor(productTransformProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step step3() {
        return new StepBuilder("step3", jobRepository)
                .tasklet(logTask, transactionManager)
                .build();
    }

    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .<ProductInput, Product> chunk(3, transactionManager)
                .reader(productFileReader.readByFile())
                .processor(productTransformByFileProcessor)
                .writer(writerByFile)
                .build();
    }

    @Bean(name = Constants.JOB_NAME)
    public Job importProductJob(Flow flow1, Flow flow2) {
        return new JobBuilder(Constants.JOB_NAME, jobRepository)
                .start(flow1)
                .split(new SimpleAsyncTaskExecutor())
                .add(flow2)
                .end()
                .build();
    }
}
