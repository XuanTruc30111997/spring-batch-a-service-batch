package org.example.reader;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.ProductInput;
import org.springframework.batch.item.ItemReader;

import java.util.ArrayList;
import java.util.List;

import static org.example.constants.Constants.*;

@Slf4j
public class ProductReader implements ItemReader<List<ProductInput>> {
    private boolean isRun = false;

    @Override
    public List<ProductInput> read() {
        if (!isRun) {
            log.info("Start reading");
            List<ProductInput> products = new ArrayList<>();
            products.add(ProductInput.builder().name("Testing 1").price(123).build());
//            products.add(ProductInput.builder().name(TESTING_SKIP).price(1111).build()); // Skip
//            products.add(ProductInput.builder().name(TESTING_ERROR).price(1111).build()); // error
//            products.add(ProductInput.builder().name(TESTING_FAIL).price(1111).build()); // fail
            products.add(ProductInput.builder().name(TESTING_ERROR_FAIL).price(1111).build()); // error fail

            isRun = true;
            return products;
        }

        return null;
    }
}
