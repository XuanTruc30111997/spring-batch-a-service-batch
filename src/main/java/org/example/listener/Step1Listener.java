package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

import static org.example.constants.Constants.*;

@Slf4j
@Component
public class Step1Listener extends StepExecutionListenerSupport {
    public ExitStatus afterStep(StepExecution stepExecution) {
        String exitCode = stepExecution.getExitStatus().getExitCode();
        log.info("Step1 listen: status: {} --- skipCount: {}", exitCode, stepExecution.getSkipCount());
        if (exitCode.equals(ERROR_STATUS)) {
            return new ExitStatus(ERROR_STATUS);
        }

        if (stepExecution.getSkipCount() > 0) {
            return new ExitStatus(SKIP_STATUS);
        }

        return null;
    }
}
