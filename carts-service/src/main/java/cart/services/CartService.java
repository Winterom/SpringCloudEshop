package cart.services;

import cart.integrations.ProductsServiceIntegration;
import cart.models.Cart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import web.core.ProductDto;
import web.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final ProductsServiceIntegration productsServiceIntegration;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${utils.cart.prefix}")
    private String cartPrefix;
    @Value("${utils.cart.statistic}")
    private String cartStatistic;

    public CartService(ProductsServiceIntegration productsServiceIntegration, RedisTemplate<String, Object> redisTemplate) {
        this.productsServiceIntegration = productsServiceIntegration;
        this.redisTemplate = redisTemplate;
    }

    public String getCartUuidFromSuffix(String suffix) {
        return cartPrefix + suffix;
    }

    public String generateCartUuid() {
        return UUID.randomUUID().toString();
    }

    public Cart getCurrentCart(String cartKey) {
        if (!redisTemplate.hasKey(cartKey)) {
            redisTemplate.opsForValue().set(cartKey, new Cart());
        }
        return (Cart) redisTemplate.opsForValue().get(cartKey);
    }

    public void addToCart(String cartKey, Long productId) {
        ProductDto productDto = productsServiceIntegration.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Невозможно добавить продукт в корзину. Продукт не найдет, id: " + productId));
        execute(cartKey, c -> {
            c.add(productDto);
        });
        redisTemplate.opsForZSet().add(cartStatistic,productDto.getId(),1);
    }

    public void clearCart(String cartKey) {
        execute(cartKey, Cart::clear);
    }

    public void removeItemFromCart(String cartKey, Long productId) {
        Cart cart = getCurrentCart(cartKey);
        redisTemplate.opsForZSet().incrementScore(cartStatistic,productId,-cart.getQuantityOfItem(productId));
        execute(cartKey, c -> c.remove(productId));
    }

    public void decrementItem(String cartKey, Long productId) {
        redisTemplate.opsForZSet().incrementScore(cartStatistic,productId,-1);
        execute(cartKey, c -> c.decrement(productId));
    }

    public void merge(String userCartKey, String guestCartKey) {
        Cart guestCart = getCurrentCart(guestCartKey);
        Cart userCart = getCurrentCart(userCartKey);
        userCart.merge(guestCart);
        updateCart(guestCartKey, guestCart);
        updateCart(userCartKey, userCart);
    }

    private void execute(String cartKey, Consumer<Cart> action) {
        Cart cart = getCurrentCart(cartKey);
        action.accept(cart);
        redisTemplate.opsForValue().set(cartKey, cart);
    }

    public void updateCart(String cartKey, Cart cart) {
        redisTemplate.opsForValue().set(cartKey, cart);
    }

    public List<ProductDto> getMostAddedToCartProduct() {
       List<Long> result = redisTemplate.opsForZSet().reverseRange(cartStatistic,0,4).stream().map(x->(Long)x).collect(Collectors.toList());
       return  productsServiceIntegration.findByIdInListProduct(result);
    }
}