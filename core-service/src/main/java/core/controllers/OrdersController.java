package core.controllers;


import core.converters.OrderConverter;
import core.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import web.core.OrderDetailsDto;
import web.core.OrderDto;
import web.core.ProductDto;
import web.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Контроллер заказов", description = "Контроллер предоставляющий эндпоинты по сущности Order")
public class OrdersController {
    private final OrderService orderService;
    private final OrderConverter orderConverter;
    @Operation(
            summary = "Сохранение заказа в базе данных",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "201"
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestHeader String username, @RequestBody OrderDetailsDto orderDetailsDto) {
        orderService.createOrder(username, orderDetailsDto);
    }

    @Operation(
            summary = "Получение списка заказов текущего пользователя",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(array = @ArraySchema (schema = @Schema(implementation = OrderDto.class)))
                    )
            }
    )
    @GetMapping
    public List<OrderDto> getCurrentUserOrders(@Parameter  @RequestHeader String username) {
        return orderService.findOrdersByUsername(username).stream()
                .map(orderConverter::entityToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable Long id) {
        return orderConverter.entityToDto(orderService.findById(id).orElseThrow(() -> new ResourceNotFoundException("ORDER 404")));
    }
}
