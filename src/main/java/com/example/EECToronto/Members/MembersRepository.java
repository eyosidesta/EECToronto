package com.example.EECToronto.Members;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MembersRepository extends JpaRepository<Members, Long> {
    Optional<Members> findMemberByEmail(String email);
    Optional<Members> findMemberByPhone(String phone);
}
