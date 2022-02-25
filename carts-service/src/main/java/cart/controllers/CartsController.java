package cart.controllers;

import cart.converters.CartConverter;
import cart.services.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import web.carts.CartDto;
import web.core.ProductDto;
import web.dto.StringResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")

public class CartsController {
    private final CartService cartService;
    private final CartConverter cartConverter;

    public CartsController(CartService cartService, CartConverter cartConverter) {
        this.cartService = cartService;
        this.cartConverter = cartConverter;
    }
    @Operation(
            summary = "Запрос на получение корзины по uuid",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CartDto.class))
                    )
            }
    )
    @GetMapping("/{uuid}")
    public CartDto getCart(@RequestHeader(required = false) String username, @Parameter(name = "uuid корзины")  @PathVariable String uuid) {
        return cartConverter.modelToDto(cartService.getCurrentCart(getCurrentCartUuid(username, uuid)));
    }
    @Operation(
            summary = "Запрос на получение новой крзины",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = StringResponse.class))
                    )
            }
    )
    @GetMapping("/generate")
    public StringResponse getCart() {
        return new StringResponse(cartService.generateCartUuid());
    }

    @Operation(
            summary = "Запрос на добавления продукта в корзину",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/{uuid}/add/{productId}")
    public void add(@Parameter(name = "имя пользователя")@RequestHeader(required = false) String username,
                    @Parameter(name = "uuid корзины") @PathVariable String uuid,
                    @Parameter(name = "id продукта")@PathVariable Long productId) {
        cartService.addToCart(getCurrentCartUuid(username, uuid), productId);
    }

    @Operation(
            summary = "Уменьшение количества продукта в корзине",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/{uuid}/decrement/{productId}")
    public void decrement(@Parameter(name = "имя пользователя") @RequestHeader(required = false) String username,
                          @Parameter(name = "uuid корзины") @PathVariable String uuid,
                          @Parameter(name = "id продукта") @PathVariable Long productId) {
        cartService.decrementItem(getCurrentCartUuid(username, uuid), productId);
    }


    @Operation(
            summary = "Удаление продукта из корзины",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/{uuid}/remove/{productId}")
    public void remove(@Parameter(name = "имя пользователя") @RequestHeader(required = false) String username,
                       @Parameter(name = "uuid корзины") @PathVariable String uuid,
                       @Parameter(name = "id продукта") @PathVariable Long productId) {
        cartService.removeItemFromCart(getCurrentCartUuid(username, uuid), productId);
    }
    @Operation(
            summary = "Очистка корзины",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/{uuid}/clear")
    public void clear(@Parameter(name = "имя пользователя") @RequestHeader(required = false) String username,
                      @Parameter(name = "uuid корзины") @PathVariable String uuid) {
        cartService.clearCart(getCurrentCartUuid(username, uuid));
    }
    @Operation(
            summary = "Слияние корзин авторизованного пользователя и неавторизованного",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/{uuid}/merge")
    public void merge(@Parameter(name = "имя пользователя") @RequestHeader(required = false) String username,
                      @Parameter(name = "uuid корзины") @PathVariable String uuid) {
        cartService.merge(
                getCurrentCartUuid(username, null),
                getCurrentCartUuid(null, uuid)
        );
    }

    private String getCurrentCartUuid(String username, String uuid) {
        if (username != null) {
            return cartService.getCartUuidFromSuffix(username);
        }
        return cartService.getCartUuidFromSuffix(uuid);
    }
    @Operation(
            summary = "Для сервиса интеграции с recommendation service ",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
                    )
            }
    )
    @GetMapping("/mostadded")
    public List<ProductDto> getMostAddedToCartProduct(){
       return cartService.getMostAddedToCartProduct();
    }
}
