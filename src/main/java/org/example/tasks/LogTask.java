package org.example.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogTask implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            log.info("START Logging.....");

            Thread.sleep(15000);

            log.info("END Logging.....");
        } catch (Exception ex) {
            log.error("ERROR Logging.....");
        }

        return RepeatStatus.FINISHED;
    }
}
