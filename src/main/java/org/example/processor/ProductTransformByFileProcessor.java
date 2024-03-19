package org.example.processor;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.ProductInput;
import org.example.model.Product;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

@Slf4j
public class ProductTransformByFileProcessor implements ItemProcessor<ProductInput, Product>  {
    @Override
    public Product process(ProductInput productInput) throws Exception {
        log.info("Start process products by file");
        UUID uuid = UUID.randomUUID();
        float price = productInput.getPrice() * 2000;

        return Product.builder()
                .id(uuid.toString())
                .name(productInput.getName())
                .price(price)
                .build();
    }
}
