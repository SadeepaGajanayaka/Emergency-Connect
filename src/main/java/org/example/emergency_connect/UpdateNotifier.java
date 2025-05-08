package org.example.emergency_connect;

import java.util.ArrayList;
import java.util.List;

public class UpdateNotifier {
    private static List<Admin> adminViews = new ArrayList<>();
    private static List<DashboardView> dashboardViews = new ArrayList<>();

    public static void registerAdminView(Admin adminView) {
        if (!adminViews.contains(adminView)) {
            adminViews.add(adminView);
        }
    }

    public static void registerDashboardView(DashboardView dashboardView) {
        if (!dashboardViews.contains(dashboardView)) {
            dashboardViews.add(dashboardView);
        }
    }

    public static void unregisterAdminView(Admin adminView) {
        adminViews.remove(adminView);
    }

    public static void unregisterDashboardView(DashboardView dashboardView) {
        dashboardViews.remove(dashboardView);
    }

    public static void notifyViews() {
        // Update admin views
        for (Admin adminView : adminViews) {
            adminView.refreshTable();
        }

        // Update dashboard views
        for (DashboardView dashboardView : dashboardViews) {
            dashboardView.refreshDashboard();
        }
    }
}