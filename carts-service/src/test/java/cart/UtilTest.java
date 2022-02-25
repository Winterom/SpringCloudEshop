package cart;

import cart.models.CartItem;
import web.core.ProductDto;

import java.math.BigDecimal;

public class UtilTest {
    public static final Long productId = 134L;
    public static CartItem getCartItemBread(){
        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);
        cartItem.setProductTitle("хлеб");
        cartItem.setQuantity(5);
        cartItem.setPricePerProduct(BigDecimal.valueOf(120));
        cartItem.setPrice(BigDecimal.valueOf(600L));
        return cartItem;
    }
    public static CartItem getCartItemMilk(){
        CartItem cartItem = new CartItem();
        cartItem.setProductId(3L);
        cartItem.setProductTitle("молоко");
        cartItem.setQuantity(4);
        cartItem.setPricePerProduct(BigDecimal.valueOf(80L));
        cartItem.setPrice(BigDecimal.valueOf(320L));
        return cartItem;
    }

    public static ProductDto getProductDtoMeat(){
        ProductDto product = new ProductDto();
        product.setId(productId);
        product.setTitle("мясо");
        product.setPrice(BigDecimal.valueOf(320L));
        return product;
    }
}
