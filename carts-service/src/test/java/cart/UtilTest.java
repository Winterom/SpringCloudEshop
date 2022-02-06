package cart;

import cart.models.CartItem;
import web.core.ProductDto;

public class UtilTest {
    public static final Long productId = 134L;
    public static CartItem getCartItemBread(){
        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);
        cartItem.setProductTitle("хлеб");
        cartItem.setQuantity(5);
        cartItem.setPricePerProduct(120);
        cartItem.setPrice(600);
        return cartItem;
    }
    public static CartItem getCartItemMilk(){
        CartItem cartItem = new CartItem();
        cartItem.setProductId(3L);
        cartItem.setProductTitle("молоко");
        cartItem.setQuantity(4);
        cartItem.setPricePerProduct(80);
        cartItem.setPrice(320);
        return cartItem;
    }

    public static ProductDto getProductDtoMeat(){
        ProductDto product = new ProductDto();
        product.setId(productId);
        product.setTitle("мясо");
        product.setPrice(320);
        return product;
    }
}
