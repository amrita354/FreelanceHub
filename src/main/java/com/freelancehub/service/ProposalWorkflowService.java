package com.freelancehub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ProposalWorkflowService {

    private final ProposalService proposalService;
    private final ContractService contractService;

    public ProposalWorkflowService(
            ProposalService proposalService,
            ContractService contractService) {

        this.proposalService = proposalService;
        this.contractService = contractService;
    }

    // Review proposal and create contract in one transaction
    @Transactional
    public void reviewProposal(
            Long proposalId,
            Long clientId,
            String status) {

        // Accept or reject the proposal
        proposalService.updateProposalStatus(
                proposalId,
                clientId,
                status
        );

        // Automatically create contract if accepted
        if ("ACCEPTED".equals(status)) {

            contractService.createContract(
                    proposalId,
                    LocalDate.now(),
                    LocalDate.now().plusDays(30)
            );
        }
    }
}