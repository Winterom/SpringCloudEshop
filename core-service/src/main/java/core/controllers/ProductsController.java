package core.controllers;


import core.converters.ProductConverter;
import core.entities.Product;
import core.services.ProductsService;
import core.validators.ProductValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import web.core.ProductDto;
import web.exception.ResourceNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Продуктовый контроллер ", description = "Контроллер предоставляющий эндпоинты по сущности Product")
public class ProductsController {
    private final ProductsService productsService;
    private final ProductConverter productConverter;
    private final ProductValidator productValidator;

    @Operation(
            summary = "Запрос на получение страницы продуктов",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    )
            }
    )
    @GetMapping
    public Page<ProductDto> getAllProducts(
            @Parameter(description = "номер страницы") @RequestParam(name = "p", defaultValue = "1") Integer page,
            @Parameter(description = "фильтр по минимальной цене") @RequestParam(name = "min_price", required = false) Integer minPrice,
            @Parameter(description = "фильтр по максимальной цене") @RequestParam(name = "max_price", required = false) Integer maxPrice,
            @Parameter(description = "фильтр по имени продукта") @RequestParam(name = "title_part", required = false) String titlePart
    ) {
        if (page < 1) {
            page = 1;
        }
        return productsService.findAll(minPrice, maxPrice, titlePart, page).map(
                p -> productConverter.entityToDto(p)
        );
    }

    @Operation(
            summary = "Запрос на получение продукта по id",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ProductDto.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public ProductDto getProductById(
            @PathVariable @Parameter(description = "Идентификатор продукта", required = true) Long id)
    {
        Product product = productsService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found, id: " + id));
        return productConverter.entityToDto(product);
    }

    @Operation(
            summary = "Запрос на получение пяти наиболее покупанмых товаров",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(array = @ArraySchema (schema = @Schema(implementation = ProductDto.class)))
                    )
            }
    )

    @GetMapping("most-buying")
    public List<ProductDto> mostBuyingProduct(){
        return productsService.getMostBuyingProduct();
    }

    @Operation(
            summary = "Сохранение нового продукта в базе данных",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ProductDto.class))
                    )
            }
    )
    @PostMapping
    public ProductDto saveNewProduct(@RequestBody ProductDto productDto) {
        productValidator.validate(productDto);
        Product product = productConverter.dtoToEntity(productDto);
        product = productsService.save(product);
        return productConverter.entityToDto(product);
    }
    @Operation(
            summary = "Обновление полей продукта",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ProductDto.class))
                    )
            }
    )
    @PutMapping
    public ProductDto updateProduct(
            @Parameter (schema = @Schema(implementation = ProductDto.class))@RequestBody ProductDto productDto) {
        productValidator.validate(productDto);
        Product product = productsService.update(productDto);
        return productConverter.entityToDto(product);
    }
    @Operation(
            summary = "Удаление продукта из базы данных",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ProductDto.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    public void deleteById(@Parameter(description = "Идентификатор продукта") @PathVariable Long id) {
        productsService.deleteById(id);
    }

    @Operation(
            summary = "Для сервиса интеграции с cart service ",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(array = @ArraySchema (schema = @Schema(implementation = ProductDto.class)))
                    )
            }
    )
    @PostMapping("findbyidinlist")
    public List<ProductDto> findByIdInList(@Parameter(array = @ArraySchema (schema = @Schema(implementation = Long.class))) @RequestBody List<Long> productIdList){
        return productsService.getProductDtoByIdInList(productIdList);
    }
}
