package com.example.EECToronto.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardMetricsDTO {
    // Members
    private Long totalMembers;
    private Long maleMembers;
    private Long femaleMembers;

    // Prayer Requests
    private Long totalPrayerRequests;
    private Long prayerRequestsContacted;
    private Long prayerRequestsNotContacted;

    // Accept Christ
    private Long totalAcceptChrist;
    private Long acceptChristContacted;
    private Long acceptChristNotContacted;

    // New Members
    private Long totalNewMembers;
    private Long newMembersContacted;
    private Long newMembersNotContacted;
    private Long newMembersWithin30Days;
    private Long newMembersWithin1Year;

    // Sermons
    private Long totalAmharicSermons;
    private Long totalEnglishSermons;

    // Sermon Groups
    private Long totalAmharicSermonGroups;
    private Long totalEnglishSermonGroups;

    // Events
    private Long totalEvents;
    private Long generalEvents;
    private Long englishServiceEvents;
    private Long youngAdultsAmharicEvents;

    // Contact Messages
    private Long totalContactMessages;

    // Team Chart Data
    private List<TeamChartData> teamChartData;
}


