package com.mydata.mydatatestbed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api-guide")
public class ApiGuideController {

    @GetMapping
    public String redirectToBase() {
        return "redirect:/api-guide/base";
    }

    @GetMapping("/base")
    public String basicSpec(Model model) {
        model.addAttribute("activeGroup", "guide");
        model.addAttribute("currentMenu", "/api-guide/base");
        model.addAttribute("breadcrumbItems", getBreadcrumbItems("데이터 표준 API 기본규격"));
        return "api-guide/basic-spec";
    }

    @GetMapping("/auth")
    public String authSpec(Model model) {
        model.addAttribute("activeGroup", "guide");
        model.addAttribute("currentMenu", "/api-guide/auth");
        model.addAttribute("breadcrumbItems", getBreadcrumbItems("데이터 표준 API 인증규격"));
        return "api-guide/auth-spec";
    }

    @GetMapping("/process")
    public String processSpec(Model model) {
        model.addAttribute("activeGroup", "guide");
        model.addAttribute("currentMenu", "/api-guide/process");
        model.addAttribute("breadcrumbItems", getBreadcrumbItems("마이데이터 참여자별 API 처리 절차"));
        return "api-guide/process-spec";
    }

    private List<Map<String, String>> getBreadcrumbItems(String current) {
        return List.of(
                Map.of("name", "홈", "url", "/"),
                Map.of("name", "API가이드", "url", "/api-guide"),
                Map.of("name", current, "url", "")
        );
    }
}