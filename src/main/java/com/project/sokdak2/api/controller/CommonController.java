package com.project.sokdak2.api.controller;

import com.project.sokdak2.api.domain.Visits;
import com.project.sokdak2.api.repository.VisitsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CommonController {
    final VisitsRepository visitsRepository;
    @GetMapping("/resume")
    public String resume(Model model) {
        List<Visits> visits
                = visitsRepository.findAllByUriContaining("/resume");
        model.addAttribute("vc", visits.size());
        return "/common/resume";
    }
}
