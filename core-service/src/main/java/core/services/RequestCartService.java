package core.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import web.dto.Cart;

@Service
public class RequestCartService {
    //где взять uid?
    public void clearCart(String cartKey) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject("http://localhost:8192/api/v1/cart/",null);
    }

    public String getCartUuidFromSuffix(String username) {
    }

    public Cart getCurrentCart(String cartKey) {
    }
}
