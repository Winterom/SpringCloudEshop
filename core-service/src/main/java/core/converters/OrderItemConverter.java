package core.converters;

import core.entities.OrderItem;
import org.springframework.stereotype.Component;
import web.core.OrderItemDto;

@Component
public class OrderItemConverter {
    public OrderItem dtoToEntity(OrderItemDto orderItemDto) {
        throw new UnsupportedOperationException();
    }

    public OrderItemDto entityToDto(OrderItem orderItem) {
        return new OrderItemDto(orderItem.getProduct().getId(), orderItem.getProduct().getTitle(), orderItem.getQuantity(), orderItem.getPricePerProduct(), orderItem.getPrice());
    }
}
