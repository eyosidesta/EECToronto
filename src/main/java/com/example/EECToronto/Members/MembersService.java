package com.example.EECToronto.Members;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MembersService {
    private final MembersRepository membersRepository;
    @Autowired
    public MembersService(MembersRepository membersRepository) {
        this.membersRepository = membersRepository;
    }
    public List<Members> getAllMembersService() {
        return membersRepository.findAll();
    }
    public void addMemberService(Members members) {
        Optional<Members> findMemberByEmail = membersRepository.findMemberByEmail(members.getEmail());
        if (findMemberByEmail.isPresent()) {
            throw new IllegalStateException("Email Taken");
        }
        Optional<Members> findMemberByPhone = membersRepository.findMemberByPhone(members.getPhone());
        if (findMemberByPhone.isPresent()) {
            throw new IllegalStateException("Phone Taken");
        }
        membersRepository.save(members);
    }
}
