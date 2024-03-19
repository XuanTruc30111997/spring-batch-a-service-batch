package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.constants.Constants;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@Slf4j
public class Main implements CommandLineRunner {
    private final JobLauncher jobLauncher;
    private final ApplicationContext applicationContext;

    public Main(JobLauncher jobLauncher, ApplicationContext applicationContext) {
        this.jobLauncher = jobLauncher;
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(Main.class, args)));
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("STARTING JOB");
        Job job = (Job) applicationContext.getBean(Constants.JOB_NAME);

        JobParameters parameters = new JobParametersBuilder().addString(Constants.KEY_JOB, "test8").toJobParameters();
        try {
            var jobExecution = jobLauncher.run(job, parameters);
            var batchStatus = jobExecution.getStatus();

            while (batchStatus.isRunning()) {
                log.info("Still running...");
                Thread.sleep(5000L);
            }
        } catch (JobInstanceAlreadyCompleteException ex) {
            log.warn("WARN ===> {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected ERROR ===> {}", ex);
        }

    }
}