package recommendations.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recommendations.service.RecommendationsService;
import web.core.ProductDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class RecommendationsController {
    private final RecommendationsService recommendationsService;

    public RecommendationsController(RecommendationsService recommendationsService) {
        this.recommendationsService = recommendationsService;
    }

    @GetMapping("/mostadded")
    public List<ProductDto> getMostAddedToCartProduct(){
        return recommendationsService.getMostAddedToCartProductDto();
    }

    @GetMapping("/mostbuying")
    public List<ProductDto> getMostBuyingProduct(){
        return recommendationsService.getMostBuyingProductDto();
    }
}
