package io.github.jchun247.collectables.service.dashboard;

import io.github.jchun247.collectables.dto.UserDashboardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    @Override
    public UserDashboardDto generateUserDashboard(Long userId, int days) {
        UserDashboardDto dashboardDTO = new UserDashboardDto();

        return dashboardDTO;
    }
}
