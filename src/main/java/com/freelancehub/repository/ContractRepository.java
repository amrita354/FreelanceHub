
package com.freelancehub.repository;

import com.freelancehub.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    Optional<Contract> findByProposal_Id(Long proposalId);

    List<Contract> findByProposal_Project_Client_Id(Long clientId);

    List<Contract> findByProposal_Freelancer_Id(Long freelancerId);
}
