package com.mydata.mydatatestbed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cert-api")
public class CertApiController {

    /**
     * /cert-api 접속 시 개별인증 API 페이지로 리다이렉트
     */
    @GetMapping
    public String redirectToIndividual() {
        return "redirect:/cert-api/individual";
    }

    /**
     * 개별인증 API
     */
    @GetMapping("/individual")
    public String individualApi(Model model) {
        model.addAttribute("sidebarMenus", getSidebarMenus());
        model.addAttribute("currentMenu", "/cert-api/individual");
        model.addAttribute("breadcrumbItems", getBreadcrumbItems("개별인증 API"));
        return "cert-api/individual-api";
    }

    /**
     * 통합인증 API
     */
    @GetMapping("/integrated")
    public String integratedApi(Model model) {
        model.addAttribute("sidebarMenus", getSidebarMenus());
        model.addAttribute("currentMenu", "/cert-api/integrated");
        model.addAttribute("breadcrumbItems", getBreadcrumbItems("통합인증 API"));
        return "cert-api/integrated-api";
    }

    // 마이데이터 인증 API 규격 사이드바 메뉴
    private List<Map<String, String>> getSidebarMenus() {
        return List.of(
                Map.of("name", "개별인증 API", "url", "/cert-api/individual"),
                Map.of("name", "통합인증 API", "url", "/cert-api/integrated")
        );
    }

    // 브레드크럼 생성
    private List<Map<String, String>> getBreadcrumbItems(String current) {
        return List.of(
                Map.of("name", "홈", "url", "/"),
                Map.of("name", "API가이드", "url", "/api-guide"),
                Map.of("name", "마이데이터 인증 API 규격", "url", "/cert-api"),
                Map.of("name", current, "url", "")
        );
    }
}