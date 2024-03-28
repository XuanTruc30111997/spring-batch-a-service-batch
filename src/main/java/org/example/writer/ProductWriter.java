package org.example.writer;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Product;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProductWriter implements ItemWriter<List<Product>> {
    @Override
    public void write(Chunk<? extends List<Product>> products) {
        log.info("Start writing");
        products.forEach(product -> log.info("Writing product {}", product));
    }
}
