package br.dev.rtisystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
@CrossOrigin(origins = "http://localhost:63342")
public class IndexController {

    @GetMapping
    public String index() {
        return "index";
    }
}
