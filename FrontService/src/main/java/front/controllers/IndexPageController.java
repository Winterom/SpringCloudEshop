package front.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@Log4j2
@RequestMapping(value = "/index" )
public class IndexPageController {

    @GetMapping
    public String getIndexPage(){
        log.info("Пришел запрос на главную страницу");
        return "index.html";
    }
}
