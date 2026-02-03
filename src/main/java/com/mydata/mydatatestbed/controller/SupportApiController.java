package com.mydata.mydatatestbed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/support-api")
public class SupportApiController {

    @GetMapping
    public String redirectToPortal() {
        return "redirect:/support-api/portal";
    }

    @GetMapping("/portal")
    public String portalApi(Model model) {
        model.addAttribute("activeGroup", "support");
        model.addAttribute("currentMenu", "/support-api/portal");
        model.addAttribute("breadcrumbItems", getBreadcrumbItems("지원 API(종합포털 제공)"));
        return "support-api/portal-api";
    }

    @GetMapping("/provider")
    public String providerApi(Model model) {
        model.addAttribute("activeGroup", "support");
        model.addAttribute("currentMenu", "/support-api/provider");
        model.addAttribute("breadcrumbItems", getBreadcrumbItems("지원 API(마이데이터사업자/정보제공자 제공)"));
        return "support-api/provider-api";
    }

    private List<Map<String, String>> getBreadcrumbItems(String current) {
        return List.of(
                Map.of("name", "홈", "url", "/"),
                Map.of("name", "API가이드", "url", "/api-guide"),
                Map.of("name", "마이데이터 지원 API 규격", "url", "/support-api"),
                Map.of("name", current, "url", "")
        );
    }
}