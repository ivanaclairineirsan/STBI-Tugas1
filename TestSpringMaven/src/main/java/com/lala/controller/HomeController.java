package com.lala.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Ivana Clairine on 10/2/2015.
 */
@Controller
public class HomeController {
    @RequestMapping(value ={"/", ""})
    public String home() {
        return "home";
    }
}
