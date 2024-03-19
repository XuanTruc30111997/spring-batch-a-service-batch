package org.example.processor;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.ProductInput;
import org.example.model.Product;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class ProductTransformProcessor implements ItemProcessor<List<ProductInput>, List<Product>> {
    @Override
    public List<Product> process(List<ProductInput> productInputs) throws Exception {
        log.info("Start process products with: {} records", productInputs.size());
        return productInputs.stream().map(this::transformProduct).collect(Collectors.toList());
    }

    private Product transformProduct(ProductInput productInput) {
        UUID uuid = UUID.randomUUID();
        float price = productInput.getPrice() * 1000;

        return Product.builder()
                .id(uuid.toString())
                .name(productInput.getName())
                .price(price)
                .build();
    }
}
