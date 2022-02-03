package cart.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import web.dto.ProductDto;


@Service
public class RequestProductService {

    // не понятно как реагировать когда прилетает эксепшн если продукт не найден
    // наверное лучше передавать пустой объект если не найдено
    public ProductDto findById(Long productId) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://localhost:8189/api/v1/products/"+productId,ProductDto.class);
    }
}
