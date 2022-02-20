package cart.integrations;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import web.core.ProductDto;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ProductsServiceIntegration {
    private final RestTemplate restTemplate;

    @Value("${integrations.core-service.url}")
    private String productServiceUrl;

    public Optional<ProductDto> findById(Long id) {
        ProductDto productDto = restTemplate.getForObject(productServiceUrl + "/api/v1/products/" + id, ProductDto.class);
        return Optional.ofNullable(productDto);
    }

    public List<ProductDto> findByIdInListProduct(List<Long> productSet){
         return Arrays.asList(Objects.requireNonNull((restTemplate.postForEntity(productServiceUrl + "/api/v1/products/findbyidinlist", productSet, ProductDto[].class)).getBody()));
    }
}
