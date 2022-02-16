package recommendations.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import web.core.ProductDto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class RecommendationsIntegration {

    private final WebClient coreServiceWebClient;

    private final WebClient cartServiceWebClient;

    public RecommendationsIntegration(@Qualifier("client_core")WebClient coreServiceWebClient, @Qualifier("client_cart") WebClient cartServiceWebClient) {
       this.cartServiceWebClient =cartServiceWebClient;
       this.coreServiceWebClient = coreServiceWebClient;
    }


    public List<ProductDto> getMostBuyingProductDto() {
        return coreServiceWebClient.get()
                .uri("/api/v1/products/most-buying")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CopyOnWriteArrayList<ProductDto>>(){})
                .block();
    }

    public List<ProductDto> getMostAddedToCartProductDto() {
        return cartServiceWebClient.get()
                .uri("/api/v1/cart/0")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CopyOnWriteArrayList<ProductDto>>(){})
                .block();
    }
}
