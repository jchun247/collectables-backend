package io.github.jchun247.collectables.service.dashboard;

import io.github.jchun247.collectables.dto.UserDashboardDTO;

public interface DashboardService {

    UserDashboardDTO generateUserDashboard(Long userId, int days);

}

