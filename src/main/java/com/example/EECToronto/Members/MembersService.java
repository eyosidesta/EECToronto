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

    public Members updateMemberService(Long id, Members updatedMember) {
        Members existingMember = membersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // Check if email is being changed and if new email is already taken
        if (!existingMember.getEmail().equals(updatedMember.getEmail())) {
            Optional<Members> findMemberByEmail = membersRepository.findMemberByEmail(updatedMember.getEmail());
            if (findMemberByEmail.isPresent() && !findMemberByEmail.get().getId().equals(id)) {
                throw new IllegalStateException("Email Taken");
            }
        }

        // Check if phone is being changed and if new phone is already taken
        if (!existingMember.getPhone().equals(updatedMember.getPhone())) {
            Optional<Members> findMemberByPhone = membersRepository.findMemberByPhone(updatedMember.getPhone());
            if (findMemberByPhone.isPresent() && !findMemberByPhone.get().getId().equals(id)) {
                throw new IllegalStateException("Phone Taken");
            }
        }

        // Update fields
        existingMember.setName(updatedMember.getName());
        existingMember.setEmail(updatedMember.getEmail());
        existingMember.setPhone(updatedMember.getPhone());
        existingMember.setGender(updatedMember.getGender());
        if (updatedMember.getDate_of_birth() != null) {
            existingMember.setDate_of_birth(updatedMember.getDate_of_birth());
        }
        if (updatedMember.getAddress() != null) {
            existingMember.setAddress(updatedMember.getAddress());
        }

        return membersRepository.save(existingMember);
    }

    public Members getMemberById(Long id) {
        return membersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
    }
}
