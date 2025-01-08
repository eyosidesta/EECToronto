package com.example.EECToronto.Members;

import com.example.EECToronto.Neighborhood.Neighborhood;
import com.example.EECToronto.Neighborhood.NeighborhoodRepository;
import com.example.EECToronto.Status.Status;
import com.example.EECToronto.Status.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MembersService {
    private final MembersRepository membersRepository;
    private final StatusRepository statusRepository;
    private final NeighborhoodRepository neighborhoodRepository;
    @Autowired
    public MembersService(MembersRepository membersRepository, StatusRepository statusRepository, NeighborhoodRepository neighborhoodRepository) {
        this.membersRepository = membersRepository;
        this.statusRepository = statusRepository;
        this.neighborhoodRepository = neighborhoodRepository;
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
        if (members.getStatus() != null) {
            Status status = members.getStatus();
            if(status.getId() != null) {
                Long status_id = status.getId();
                status = statusRepository.findById(status.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid Status Id: " + status_id));
            } else {
                throw new IllegalStateException("Status Id is not provided");
            }
            members.setStatus(status);
        }
        if (members.getNeighborhood() != null) {
            Neighborhood neighborhood = members.getNeighborhood();
            if (neighborhood.getId() != null) {
                Long neighborhood_id = neighborhood.getId();
                neighborhood = neighborhoodRepository.findById(neighborhood.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid Neighborhood Id: " + neighborhood_id));
            } else {
                throw new IllegalStateException("Neighborhood Id is not provided");
            }
            members.setNeighborhood(neighborhood);

        }
        membersRepository.save(members);
    }
}
