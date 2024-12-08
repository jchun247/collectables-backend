package io.github.jchun247.collectables.service;

import io.github.jchun247.collectables.dto.UserDashboardDto;

public interface DashboardService {

    public UserDashboardDto generateUserDashboard(Long userId, int days);

}

