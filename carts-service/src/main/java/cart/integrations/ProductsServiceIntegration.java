package cart.integrations;


import cart.configs.CoreServiceIntegrationProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import web.core.ProductDto;
import web.exception.ResourceNotFoundException;
import web.exception.ServerNotResponseException;

import java.util.*;

@Component
public class ProductsServiceIntegration {
    private final WebClient coreServiceClient;
    CoreServiceIntegrationProperty property;

    public ProductsServiceIntegration(WebClient coreServiceClient) {
        this.coreServiceClient = coreServiceClient;
    }

    public Optional<ProductDto> findById(Long id) {
        ProductDto productDto =coreServiceClient.get()
                .uri(property.getCoreServiceUrl() + "/api/v1/products/" + id)
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new ServerNotResponseException("Core-service временно не доступен")))
                .onStatus(HttpStatus.NOT_FOUND::equals,clientResponse -> Mono.error(new ResourceNotFoundException("Не найден продукт с id: "+id)))
                .bodyToMono(ProductDto.class)
                .block();
        return Optional.ofNullable(productDto);
    }

    public List<ProductDto> findByIdInListProduct(List<Long> productList){
        return  coreServiceClient.post()
                .uri(property.getCoreServiceUrl() + "/api/v1/products/findbyidinlist")
                .body(Mono.just(productList), new ParameterizedTypeReference<>() {})
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductDto>>(){})
                .block();
    }
}
