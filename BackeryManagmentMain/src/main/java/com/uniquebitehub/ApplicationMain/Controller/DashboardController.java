package com.uniquebitehub.ApplicationMain.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.uniquebitehub.ApplicationMain.Enum.TimeRange;
import com.uniquebitehub.ApplicationMain.Response.DashboardSummaryResponse;
import com.uniquebitehub.ApplicationMain.RestResponse.RestResponse;
import com.uniquebitehub.ApplicationMain.Service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public RestResponse getSummary(@RequestParam TimeRange range) {
        return RestResponse.build()
                .withSuccess(
                        "Dashboard summary fetched",
                        dashboardService.getSummary(range)
                );
    }
}
