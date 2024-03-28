package org.example.skip;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.NameException;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

@Slf4j
public class Step1SkipPolicy implements SkipPolicy {
    private static final int MAX_SKIP_COUNT = 2;

    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
        log.info("Checking Skip Condition");
        if (t instanceof NameException && skipCount < MAX_SKIP_COUNT) {
            log.info("Increase Skip Count");
            return true;
        }

        return false;
    }
}
