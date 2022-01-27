package front.controllers;

import front.service.FrontService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.ProductDto;

import java.util.List;

@RestController
@Log4j2
@RequestMapping(value = "/products" )
public class RestControllerFront {
    FrontService frontService;


    public RestControllerFront(FrontService frontService) {
        this.frontService = frontService;
    }

    @GetMapping
    public List<ProductDto> getAllProducts(){
        return frontService.getAllProducts();
    }
}
