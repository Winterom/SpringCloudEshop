package core.services;

import com.paypal.orders.*;

import core.entities.OrderStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.exception.PayException;
import web.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayPalService {
    private final OrderService orderService;

    public PayPalService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Transactional
    public OrderRequest createOrderRequest(Long orderId) {
        core.entities.Order order = orderService.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Заказ не найден"));

        if (!order.getStatus().equals(OrderStatusEnum.CREATED)){
            throw new PayException("Заказ может быть оплаченным только в статусе CREATED");
        }
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext()
                .brandName("Spring Web Market")
                .landingPage("BILLING")
                .shippingPreference("SET_PROVIDED_ADDRESS");
        orderRequest.applicationContext(applicationContext);

        List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<>();
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                .referenceId(orderId.toString())
                .description("Spring Web Market Order")
                .amountWithBreakdown(new AmountWithBreakdown().currencyCode("RUB").value(String.valueOf(order.getTotalPrice()))
                        .amountBreakdown(new AmountBreakdown().itemTotal(new Money().currencyCode("RUB").value(String.valueOf(order.getTotalPrice())))))
                .items(order.getItems().stream()
                        .map(orderItem -> new Item()
                                .name(orderItem.getProduct().getTitle())
                                .unitAmount(new Money().currencyCode("RUB").value(String.valueOf(orderItem.getPrice())))
                                .quantity(String.valueOf(orderItem.getQuantity())))
                        .collect(Collectors.toList()))
                .shippingDetail(new ShippingDetail().name(new Name().fullName(order.getUsername()))
                        .addressPortable(new AddressPortable().addressLine1(order.getAddressLine1()).addressLine2(order.getAddressLine2())
                                .adminArea2(order.getAdminArea2()).adminArea1(order.getAdminArea1()).postalCode(order.getPostalCode()).countryCode(order.getCountryCode().name())));
        purchaseUnitRequests.add(purchaseUnitRequest);
        orderRequest.purchaseUnits(purchaseUnitRequests);
        return orderRequest;
    }


}
