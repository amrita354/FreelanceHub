
package com.freelancehub.repository;

import com.freelancehub.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByMilestone_Id(Long milestoneId);

    List<Invoice> findByMilestone_Contract_Proposal_Project_Client_Id(
            Long clientId);

    List<Invoice> findByMilestone_Contract_Proposal_Freelancer_Id(
            Long freelancerId);
}
