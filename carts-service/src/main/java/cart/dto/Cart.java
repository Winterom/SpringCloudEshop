package cart.dto;

import lombok.Data;
import web.dto.ProductDto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
public class Cart {
    private List<CartItemDto> items;
    private int totalPrice;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void add(ProductDto product) {
        if (add(product.getId())) {
            return;
        }
        items.add(new CartItemDto(product));
        recalculate();
    }

    public boolean add(Long id) {
        for (CartItemDto o : items) {
            if (o.getProductId().equals(id)) {
                o.changeQuantity(1);
                recalculate();
                return true;
            }
        }
        return false;
    }

    public void decrement(Long productId) {
        Iterator<CartItemDto> iter = items.iterator();
        while (iter.hasNext()) {
            CartItemDto o = iter.next();
            if (o.getProductId().equals(productId)) {
                o.changeQuantity(-1);
                if (o.getQuantity() <= 0) {
                    iter.remove();
                }
                recalculate();
                return;
            }
        }
    }

    public void remove(Long productId) {
        items.removeIf(o -> o.getProductId().equals(productId));
        recalculate();
    }

    public void clear() {
        items.clear();
        totalPrice = 0;
    }

    private void recalculate() {
        totalPrice = 0;
        for (CartItemDto o : items) {
            totalPrice += o.getPrice();
        }
    }

    public void merge(Cart another) {
        for (CartItemDto anotherItem : another.items) {
            boolean merged = false;
            for (CartItemDto myItem : items) {
                if (myItem.getProductId().equals(anotherItem.getProductId())) {
                    myItem.changeQuantity(anotherItem.getQuantity());
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                items.add(anotherItem);
            }
        }
        recalculate();
        another.clear();
    }
}
