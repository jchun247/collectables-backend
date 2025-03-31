package io.github.jchun247.collectables.service.dashboard;

import io.github.jchun247.collectables.dto.UserDashboardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    @Override
    public UserDashboardDTO generateUserDashboard(Long userId, int days) {
        UserDashboardDTO dashboardDTO = new UserDashboardDTO();

        return dashboardDTO;
    }
}
