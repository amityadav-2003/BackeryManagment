package com.uniquebitehub.ApplicationMain.Response;

import lombok.Data;

@Data
public class DashboardSummaryResponse {

    private long totalOrders;
    private long totalUsers;
    private double totalRevenue;
}
