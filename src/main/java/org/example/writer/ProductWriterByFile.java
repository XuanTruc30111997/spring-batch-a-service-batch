package org.example.writer;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Product;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProductWriterByFile  implements ItemWriter<Product> {
    @Autowired
    DataSource dataSource;

    @Override
    public void write(Chunk<? extends Product> product) throws Exception {
        log.info("Start writing by File");

        SimpleJdbcInsert simpleJdbcInsert =
                new SimpleJdbcInsert(dataSource).withTableName("product");


        List<Product> productList = new ArrayList<>(product.getItems());

        productList.forEach(prod -> {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("id", prod.getId());
            parameters.put("name", prod.getName());
            parameters.put("price", prod.getPrice());

            simpleJdbcInsert.execute(parameters);
        });
    }
}
