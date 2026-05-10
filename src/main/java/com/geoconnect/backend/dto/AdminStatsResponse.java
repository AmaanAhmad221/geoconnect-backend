package com.geoconnect.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsResponse {

    private long totalUsers;
    private long totalCustomers;
    private long totalProviders;
    private long totalServices;
    private long totalBookings;
    private long totalReviews;
    private long pendingBookings;
    private long completedBookings;
}