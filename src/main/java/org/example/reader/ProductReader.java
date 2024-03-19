package org.example.reader;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.ProductInput;
import org.springframework.batch.item.ItemReader;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductReader implements ItemReader<List<ProductInput>> {
    private boolean isRun = false;

    @Override
    public List<ProductInput> read() {
        if (!isRun) {
            log.info("Start reading");
            List<ProductInput> products = new ArrayList<>();
            products.add(ProductInput.builder().name("Testing 1").price(123).build());
            products.add(ProductInput.builder().name("Testing 2").price(1111).build());

            isRun = true;
            return products;
        }

        return null;
    }
}
