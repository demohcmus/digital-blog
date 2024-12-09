package vn.ibex.digital_blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import vn.ibex.digital_blog.util.annotation.ApiMessage;

@RestController
public class HomeController {
    @GetMapping("/")
    @ApiMessage("Get account information")
    public String getAccount(){
        String hello= "Hello World";
        return  hello;
    }
}
