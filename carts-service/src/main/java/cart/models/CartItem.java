package cart.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.core.ProductDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Long productId;
    private String productTitle;
    private int quantity;
    private int pricePerProduct;
    private int price;

    public CartItem(ProductDto productDto) {
        this.productId = productDto.getId();
        this.productTitle = productDto.getTitle();
        this.quantity = 1;
        this.pricePerProduct = productDto.getPrice();
        this.price = productDto.getPrice();
    }

    public void changeQuantity(int delta) {
        this.quantity += delta;
        this.price = this.quantity * this.pricePerProduct;
    }
}
