package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.dto.UserDashboardDTO;
import io.github.jchun247.collectables.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDashboardDTO> getUserDashboard(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "30") int days
    ) {
        UserDashboardDTO dashboardData = dashboardService.generateUserDashboard(userId, days);
        return ResponseEntity.ok(dashboardData);
    }
}
