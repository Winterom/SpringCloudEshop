package recommendations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import recommendations.integration.RecommendationsIntegration;
import web.core.ProductDto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class RecommendationsService {
    private List<ProductDto> mostBuyingProduct = new CopyOnWriteArrayList<>();
    private List<ProductDto> mostAddedToCart = new CopyOnWriteArrayList<>();
    private RecommendationsIntegration recommendationsIntegration;

    public RecommendationsService() {
    }

    @Autowired
    public RecommendationsService(RecommendationsIntegration recommendationsIntegration) {
        this.recommendationsIntegration = recommendationsIntegration;
    }

    public List<ProductDto> getMostBuyingProductDto(){
        if (mostBuyingProduct.size()==0){
            mostBuyingProduct = recommendationsIntegration.getMostBuyingProductDto();
        }
        return mostBuyingProduct;
    }


    public List<ProductDto> getMostAddedToCartProductDto(){
        if (mostAddedToCart.size()==0){
            mostAddedToCart = recommendationsIntegration.getMostBuyingProductDto();
        }
        return mostAddedToCart;
    }

    //выполнение задачи каждый час
    @Scheduled(fixedRate = 3600000)
    public void initServiceParam(){
        mostBuyingProduct = recommendationsIntegration.getMostBuyingProductDto();
        mostAddedToCart = recommendationsIntegration.getMostBuyingProductDto();
    }

}
