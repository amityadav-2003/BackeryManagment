package com.uniquebitehub.ApplicationMain.Service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniquebitehub.ApplicationMain.Enum.TimeRange;
import com.uniquebitehub.ApplicationMain.Repository.OrderRepository;
import com.uniquebitehub.ApplicationMain.Repository.UserRepository;
import com.uniquebitehub.ApplicationMain.Response.DashboardSummaryResponse;

@Service
public class DashboardService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public DashboardSummaryResponse getSummary(TimeRange range) {

        LocalDateTime startDate = getStartDate(range);

        DashboardSummaryResponse response = new DashboardSummaryResponse();

        if (startDate == null) {
            // ALL TIME
            response.setTotalOrders(orderRepository.count());
            response.setTotalUsers(userRepository.count());
            response.setTotalRevenue(orderRepository.getTotalRevenueAll());
        } else {
            response.setTotalOrders(orderRepository.countByCreatedAtAfter(startDate));
            response.setTotalUsers(userRepository.countByCreatedAtAfter(startDate));
            response.setTotalRevenue(orderRepository.getTotalRevenueAfter(startDate));
        }

        return response;
    }

    private LocalDateTime getStartDate(TimeRange range) {

        switch (range) {
            case TODAY:
                return LocalDateTime.now().minusDays(1);
            case WEEK:
                return LocalDateTime.now().minusDays(7);
            case MONTH:
                return LocalDateTime.now().minusDays(30);
            case ALL:
            default:
                return null;
        }
    }
}
