package cart;

import cart.integrations.ProductsServiceIntegration;
import cart.models.Cart;
import cart.models.CartItem;
import cart.services.CartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CartTest {
    public final String cartKeyUser = "cartKeyUser";
    public final String cartKeyGuest = "cartKeyGuest";


    @Autowired
    public CartService cartService;


    @MockBean
    private ProductsServiceIntegration productsServiceIntegration;

    @BeforeEach
    public void initTest(){
        cartService.clearCart(cartKeyUser);
        cartService.clearCart(cartKeyGuest);
    }
    @Test
    public void merged(){
        CartItem cartItemFirst = UtilTest.getCartItemBread();

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItemFirst);

        Cart cartUser = cartService.getCurrentCart(cartKeyUser);
        cartUser.setItems(cartItems);
        cartUser.setTotalPrice(cartItemFirst.getPrice());

        CartItem cartItemSecond = UtilTest.getCartItemMilk();

        List<CartItem> cartItemsGuest = new ArrayList<>();
        cartItemsGuest.add(cartItemSecond);

        Cart cartGuest = cartService.getCurrentCart(cartKeyGuest);
        cartGuest.setItems(cartItemsGuest);
        cartGuest.setTotalPrice(cartItemSecond.getPrice());

        cartService.updateCart(cartKeyUser, cartUser);
        cartService.updateCart(cartKeyGuest, cartGuest);

        int testAmount = cartItemFirst.getPrice()+ cartItemSecond.getPrice();

        cartService.merge(cartKeyUser,cartKeyGuest);
        Assertions.assertEquals(0, cartService.getCurrentCart(cartKeyGuest).getItems().size());
        Assertions.assertEquals(2,cartService.getCurrentCart(cartKeyUser).getItems().size());
        Assertions.assertEquals(testAmount,cartService.getCurrentCart(cartKeyUser).getTotalPrice());
    }

    @Test
    public void addToCartService(){
        Mockito.doReturn(Optional.of(UtilTest.getProductDtoMeat())).when(productsServiceIntegration).findById(UtilTest.productId);
        cartService.addToCart(cartKeyUser,UtilTest.productId);
        cartService.addToCart(cartKeyUser,UtilTest.productId);
        cartService.addToCart(cartKeyUser,UtilTest.productId);
        cartService.addToCart(cartKeyUser,UtilTest.productId);
        Assertions.assertEquals(1,cartService.getCurrentCart(cartKeyUser).getItems().size());
        Assertions.assertEquals(UtilTest.getProductDtoMeat().getPrice()*4,cartService.getCurrentCart(cartKeyUser).getTotalPrice());
    }

    @Test
    public void removeItemTest(){
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(UtilTest.getCartItemBread());
        cartItems.add(UtilTest.getCartItemMilk());
        Cart cartUser = cartService.getCurrentCart(cartKeyUser);
        cartUser.setItems(cartItems);
        cartService.updateCart(cartKeyUser,cartUser);

        cartService.removeItemFromCart(cartKeyUser,cartUser.getItems().get(0).getProductId());
        Assertions.assertEquals(1,cartService.getCurrentCart(cartKeyUser).getItems().size());
        int resultAmount = cartService.getCurrentCart(cartKeyUser).getItems().get(0).getPrice();
        Assertions.assertEquals(resultAmount,cartService.getCurrentCart(cartKeyUser).getTotalPrice());
    }

    @Test
    public void decrementItemTest(){
        cartService.addToCart(cartKeyUser,UtilTest.productId);
        cartService.addToCart(cartKeyUser,UtilTest.productId);
        cartService.addToCart(cartKeyUser,UtilTest.productId);
        cartService.addToCart(cartKeyUser,UtilTest.productId);

        cartService.decrementItem(cartKeyUser,UtilTest.productId);
        Assertions.assertEquals(3,cartService.getCurrentCart(cartKeyUser).getItems().get(0).getQuantity());
        int resultAmount = cartService.getCurrentCart(cartKeyUser).getItems().get(0).getPrice();
        Assertions.assertEquals(resultAmount,cartService.getCurrentCart(cartKeyUser).getTotalPrice());
    }
    @Test
    public void clearCartTest(){
        cartService.addToCart(cartKeyUser,UtilTest.productId);
        cartService.addToCart(cartKeyUser,UtilTest.productId);
        cartService.addToCart(cartKeyUser,UtilTest.productId);

        cartService.clearCart(cartKeyUser);
        Assertions.assertEquals(0,cartService.getCurrentCart(cartKeyUser).getItems().size());
    }

}
