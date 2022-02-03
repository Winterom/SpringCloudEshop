package front.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import utils.ProductDto;

import java.util.List;


@Service
public class FrontService {
    @Autowired
    private RestTemplate restTemplate;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @LoadBalanced
    public List<ProductDto> getAllProducts(){
        return  restTemplate.getForObject("http://product-service/api/v1/products", List.class);
    }
}
