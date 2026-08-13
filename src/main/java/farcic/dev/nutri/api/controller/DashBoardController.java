package farcic.dev.nutri.api.controller;

import farcic.dev.nutri.api.dto.response.DashboardResumoResponse;
import farcic.dev.nutri.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashBoardController {

    private final DashboardService dashboardService;

    //GET /api/dashboard/resumo
    @GetMapping("/resumo")
    @ResponseStatus(HttpStatus.OK)
    public DashboardResumoResponse obterResumo() {
        return dashboardService.obterResumo();
    }
}
