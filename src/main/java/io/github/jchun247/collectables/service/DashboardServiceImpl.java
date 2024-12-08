package io.github.jchun247.collectables.service;

import io.github.jchun247.collectables.dto.UserDashboardDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    @Override
    public UserDashboardDto generateUserDashboard(Long userId, int days) {
        UserDashboardDto dashboardDTO = new UserDashboardDto();

        return dashboardDTO;
    }
}
