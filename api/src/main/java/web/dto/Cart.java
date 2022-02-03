package web.dto;

import lombok.Data;

import java.util.List;


@Data
public class Cart {
    private List<CartItemDto> items;
    private int totalPrice;
}
