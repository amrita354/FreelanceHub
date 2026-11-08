
package com.freelancehub.repository;

import com.freelancehub.model.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository
        extends JpaRepository<Proposal, Long> {

    List<Proposal> findByProject_Id(Long projectId);

    List<Proposal> findByFreelancer_Id(Long freelancerId);

    List<Proposal> findByProject_Client_Id(Long clientId);

    boolean existsByProject_IdAndFreelancer_Id(
            Long projectId, Long freelancerId
    );
}
