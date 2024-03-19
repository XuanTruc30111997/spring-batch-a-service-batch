package org.example.reader;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.ProductInput;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class ProductFileReader {
    public FlatFileItemReader<ProductInput> readByFile() {
        log.info("Start reading by File");
        return new FlatFileItemReaderBuilder<ProductInput>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names("name", "price")
                .targetType(ProductInput.class)
                .build();
    }
}
