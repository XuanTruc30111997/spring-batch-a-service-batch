package org.example.processor;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.ProductInput;
import org.example.exception.NameException;
import org.example.model.Product;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.example.constants.Constants.*;

@Slf4j
public class ProductTransformProcessor extends ItemListenerSupport<List<ProductInput>, List<Product>> implements ItemProcessor<List<ProductInput>, List<Product>> {
    private StepExecution stepExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        this.stepExecution.setExitStatus(new ExitStatus(NONE_STATUS));
    }

    @Override
    public List<Product> process(List<ProductInput> productInputs) throws NameException {
        log.info("Start process products with: {} records", productInputs.size());
        return productInputs.stream().map(this::transformProduct).collect(Collectors.toList());
    }

    private Product transformProduct(ProductInput productInput) {
        UUID uuid = UUID.randomUUID();
        float price = productInput.getPrice() * 1000;

        if (Objects.equals(productInput.getName(), TESTING_SKIP)) {
            throw new NameException(productInput.getName());
        }

        return Product.builder()
                .id(uuid.toString())
                .name(productInput.getName())
                .price(price)
                .build();
    }

    @Override
    public void afterProcess(List<ProductInput> item, List<Product> result) {
        boolean isTestingError = item.stream().anyMatch(pro -> pro.getName().equals(TESTING_ERROR));
        if (isTestingError) {
            this.stepExecution.setExitStatus(new ExitStatus(ERROR_STATUS));
        }

        boolean isTestingFailed = item.stream().anyMatch(pro -> pro.getName().equals(TESTING_FAIL));
        if (isTestingFailed) {
            this.stepExecution.setExitStatus(ExitStatus.FAILED);
        }

        boolean isTestingErrorFailed = item.stream().anyMatch(pro -> pro.getName().equals(TESTING_ERROR_FAIL));
        if (isTestingErrorFailed) {
            this.stepExecution.setExitStatus(new ExitStatus(ERROR_FAIL_STATUS));
        }
    }
}
