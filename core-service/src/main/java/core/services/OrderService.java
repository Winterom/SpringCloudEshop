package core.services;

import core.entities.Order;
import core.entities.OrderItem;
import core.entities.OrderStatusEnum;
import core.entities.PayCountryCodeEnum;
import core.integration.CartServiceIntegration;
import core.repositories.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.carts.CartDto;
import web.core.OrderDetailsDto;
import web.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrdersRepository ordersRepository;
    private final CartServiceIntegration cartServiceIntegration;
    private final ProductsService productsService;

    @Transactional
    public void createOrder(String username, OrderDetailsDto orderDetailsDto) {
        CartDto currentCart = cartServiceIntegration.getUserCart(username);
        Order order = new Order();
        order.setAddressLine1(orderDetailsDto.getAddressLine1());
        order.setAddressLine2(orderDetailsDto.getAddressLine2());
        order.setAdminArea1(orderDetailsDto.getAdminArea1());
        order.setAdminArea2(orderDetailsDto.getAdminArea2());
        order.setCountryCode(PayCountryCodeEnum.valueOf(orderDetailsDto.getCountryCode()));
        order.setPostalCode(orderDetailsDto.getPostalCode());
        order.setPhone(orderDetailsDto.getPhone());
        order.setUsername(username);
        order.setStatus(OrderStatusEnum.CREATED);
        order.setTotalPrice(currentCart.getTotalPrice());
        List<OrderItem> items = currentCart.getItems().stream()
                .map(o -> {
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setQuantity(o.getQuantity());
                    item.setPricePerProduct(o.getPricePerProduct());
                    item.setPrice(o.getPrice());
                    item.setProduct(productsService.findById(o.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found")));
                    return item;
                }).collect(Collectors.toList());
        order.setItems(items);
        ordersRepository.save(order);
        cartServiceIntegration.clearUserCart(username);
    }

    public List<Order> findOrdersByUsername(String username) {
        return ordersRepository.findAllByUsername(username);
    }

    public Optional<Order> findById(Long id) {
        return ordersRepository.findById(id);
    }

    public Order updateAfterPay(Long id){
        Order order = findById(id).orElseThrow(()-> new ResourceNotFoundException("???????????????????? ???????????????? ????????????????, ???? ???????????? ?? ????????, id:" +id));
        order.setStatus(OrderStatusEnum.PAID);
        return order;
    }
}
