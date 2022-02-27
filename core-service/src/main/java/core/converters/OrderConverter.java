package core.converters;

import core.entities.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import web.core.OrderDto;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderConverter {
    private final OrderItemConverter orderItemConverter;

    public Order dtoToEntity(OrderDto orderDto) {
        throw new UnsupportedOperationException();
    }


    public OrderDto entityToDto(Order order) {
        OrderDto out = new OrderDto();
        out.setId(order.getId());
        out.setAddressLine1(order.getAddressLine1());
        out.setAddressLine2(order.getAddressLine2());
        out.setAdminArea1(order.getAdminArea1());
        out.setAdminArea2(order.getAdminArea2());
        out.setCountryCode(order.getCountryCode().name());
        out.setPostalCode(order.getPostalCode());
        out.setPhone(order.getPhone());
        out.setTotalPrice(order.getTotalPrice());
        out.setUsername(order.getUsername());
        out.setItems(order.getItems().stream().map(orderItemConverter::entityToDto).collect(Collectors.toList()));
        return out;
    }
}
