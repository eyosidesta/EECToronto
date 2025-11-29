package com.example.EECToronto.Dashboard;

import com.example.EECToronto.AcceptChristRequest.AcceptChristRequestRepository;
import com.example.EECToronto.ContactMessage.ContactMessageRepository;
import com.example.EECToronto.DTO.SermonType;
import com.example.EECToronto.Events.EventRepository;
import com.example.EECToronto.Members.Members;
import com.example.EECToronto.Members.MembersRepository;
import com.example.EECToronto.NewMemberRequest.NewMemberRequestRepository;
import com.example.EECToronto.PrayerRequest.PrayerRequestRepository;
import com.example.EECToronto.SermonGroup.SermonGroupRepository;
import com.example.EECToronto.Sermons.SermonRepository;
import com.example.EECToronto.TeamMembers.TeamMembers;
import com.example.EECToronto.TeamMembers.TeamMembersRepository;
import com.example.EECToronto.Teams.Teams;
import com.example.EECToronto.Teams.TeamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final MembersRepository membersRepository;
    private final PrayerRequestRepository prayerRequestRepository;
    private final AcceptChristRequestRepository acceptChristRequestRepository;
    private final NewMemberRequestRepository newMemberRequestRepository;
    private final ContactMessageRepository contactMessageRepository;
    private final SermonRepository sermonRepository;
    private final SermonGroupRepository sermonGroupRepository;
    private final EventRepository eventRepository;
    private final TeamsRepository teamsRepository;
    private final TeamMembersRepository teamMembersRepository;

    @Autowired
    public DashboardService(
            MembersRepository membersRepository,
            PrayerRequestRepository prayerRequestRepository,
            AcceptChristRequestRepository acceptChristRequestRepository,
            NewMemberRequestRepository newMemberRequestRepository,
            ContactMessageRepository contactMessageRepository,
            SermonRepository sermonRepository,
            SermonGroupRepository sermonGroupRepository,
            EventRepository eventRepository,
            TeamsRepository teamsRepository,
            TeamMembersRepository teamMembersRepository) {
        this.membersRepository = membersRepository;
        this.prayerRequestRepository = prayerRequestRepository;
        this.acceptChristRequestRepository = acceptChristRequestRepository;
        this.newMemberRequestRepository = newMemberRequestRepository;
        this.contactMessageRepository = contactMessageRepository;
        this.sermonRepository = sermonRepository;
        this.sermonGroupRepository = sermonGroupRepository;
        this.eventRepository = eventRepository;
        this.teamsRepository = teamsRepository;
        this.teamMembersRepository = teamMembersRepository;
    }

    public DashboardMetricsDTO getDashboardMetrics() {
        DashboardMetricsDTO metrics = new DashboardMetricsDTO();

        // Members metrics
        List<Members> allMembers = membersRepository.findAll();
        metrics.setTotalMembers((long) allMembers.size());
        metrics.setMaleMembers(allMembers.stream()
                .filter(m -> m.getGender() != null && m.getGender().equalsIgnoreCase("Male"))
                .count());
        metrics.setFemaleMembers(allMembers.stream()
                .filter(m -> m.getGender() != null && m.getGender().equalsIgnoreCase("Female"))
                .count());

        // Prayer Requests metrics
        metrics.setTotalPrayerRequests(prayerRequestRepository.count());
        metrics.setPrayerRequestsContacted(prayerRequestRepository.countByContacted(true));
        metrics.setPrayerRequestsNotContacted(prayerRequestRepository.countByContacted(false));

        // Accept Christ metrics
        metrics.setTotalAcceptChrist(acceptChristRequestRepository.count());
        metrics.setAcceptChristContacted(acceptChristRequestRepository.findByContacted(true).size());
        metrics.setAcceptChristNotContacted(acceptChristRequestRepository.findByContacted(false).size());

        // New Members metrics
        metrics.setTotalNewMembers(newMemberRequestRepository.count());
        metrics.setNewMembersContacted(newMemberRequestRepository.findByContacted(true).size());
        metrics.setNewMembersNotContacted(newMemberRequestRepository.findByContacted(false).size());

        // New Members within 30 days and 1 year
        Date thirtyDaysAgo = Date.from(LocalDate.now().minusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date oneYearAgo = Date.from(LocalDate.now().minusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date now = new Date();

        List<com.example.EECToronto.NewMemberRequest.NewMemberRequest> allNewMemberRequests = newMemberRequestRepository.findAll();
        metrics.setNewMembersWithin30Days(allNewMemberRequests.stream()
                .filter(r -> r.getRequestDate() != null && 
                        r.getRequestDate().after(thirtyDaysAgo) && 
                        r.getRequestDate().before(now))
                .count());

        metrics.setNewMembersWithin1Year(allNewMemberRequests.stream()
                .filter(r -> r.getRequestDate() != null && 
                        r.getRequestDate().after(oneYearAgo) && 
                        r.getRequestDate().before(now))
                .count());

        // Sermons metrics
        metrics.setTotalAmharicSermons(sermonRepository.findBySermonType(SermonType.AMHARIC).size());
        metrics.setTotalEnglishSermons(sermonRepository.findBySermonType(SermonType.ENGLISH).size());

        // Sermon Groups metrics
        metrics.setTotalAmharicSermonGroups(sermonGroupRepository.findBySermonType(SermonType.AMHARIC).size());
        metrics.setTotalEnglishSermonGroups(sermonGroupRepository.findBySermonType(SermonType.ENGLISH).size());

        // Events metrics
        metrics.setTotalEvents(eventRepository.count());
        metrics.setGeneralEvents(eventRepository.findByEventType("General").size());
        metrics.setEnglishServiceEvents(eventRepository.findByEventType("English").size());
        metrics.setYoungAdultsAmharicEvents(eventRepository.findByEventType("Amharic").size());

        // Contact Messages metrics
        metrics.setTotalContactMessages(contactMessageRepository.count());

        // Teams and member counts for bar chart
        List<Teams> allTeams = teamsRepository.findAll();
        List<TeamChartData> teamChartData = new ArrayList<>();
        
        for (Teams team : allTeams) {
            List<TeamMembers> teamMembers = teamMembersRepository.findMembersByTeams(team);
            teamChartData.add(new TeamChartData(team.getTeam_name(), (long) teamMembers.size()));
        }
        
        metrics.setTeamChartData(teamChartData);

        return metrics;
    }
}

