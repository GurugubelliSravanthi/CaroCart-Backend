package com.carocart.cartservice.feign;

import com.carocart.cartservice.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CAROCART-PRODUCTSERVICE", url = "http://localhost:8082", contextId = "ProductServiceClient")

public interface ProductClient {

    @GetMapping("/products/dto/{id}")
    ProductDTO getProductDTOById(@PathVariable("id") Long id);
}
