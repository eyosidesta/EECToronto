package com.example.EECToronto.TeamSMS;

import com.example.EECToronto.Members.MembersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.EECToronto.Members.Members;
import com.azure.communication.sms.models.*;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.communication.sms.*;
import com.azure.core.util.Context;
import java.util.*;

@Service
public class TeamSMSService {
    private final TeamSMSRepository teamSMSRepository;
    private final MembersRepository membersRepository;
    @Autowired
    public TeamSMSService(TeamSMSRepository teamSMSRepository, MembersRepository membersRepository) {
        this.teamSMSRepository = teamSMSRepository;
        this.membersRepository = membersRepository;
    }
    public List<TeamSMS> getAllTeamSMSService() {
        return teamSMSRepository.findAll();
    }

    public void sendTeamSMSService(TeamSMS teamSMS, Members members) {
        Optional<Members> member = membersRepository.findById(1L);
        Members getMemberDetail = member.get();
        String sms_end_point = "https://eectoronto-sms-service.canada.communication.azure.com/";


        System.out.println("Member is: " + getMemberDetail.getName());
    }
    public void sendMessageForAll(String message) {
        List<Members> members = membersRepository.findAll();
        List<Members> all_members = members.stream().toList();
        String sms_end_point = "https://eectoronto-sms-service.canada.communication.azure.com/";
        AzureKeyCredential azureKeyCredential = new AzureKeyCredential("<access-key-credential>");
 
        SmsClient smsClient = new SmsClientBuilder()
                .endpoint(sms_end_point)
                .credential(azureKeyCredential)
                .buildClient();
//        int counter = 0;
        for (Members allMember : all_members) {
            String personalized_message = message.replaceAll("\\{name}", allMember.getName());
//            allMember.
//            System.out.println("member name: " + allMember.getName());
            System.out.println("message is: " + personalized_message);
//            SmsSendResult smsSendResult = smsClient.send("+11111111111", "+2222222222", message);
//            if (i % 160 == 0) {
//
//            }
        }
//        for (Members member : all_members) {
//            System.out.println("member name: " + member.getName());
//        }

    }
}
