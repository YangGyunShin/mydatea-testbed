package com.mydata.mydatatestbed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;

@Controller
public class MainController {

    @GetMapping("/")
    public String main(Model model) {
        // TODO: NoticeService 연동 후 실제 데이터로 교체
        model.addAttribute("notices", Collections.emptyList());
        return "main/index";
    }
}
