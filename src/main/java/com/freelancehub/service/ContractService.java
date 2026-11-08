
package com.freelancehub.service;

import com.freelancehub.model.Contract;
import com.freelancehub.model.Proposal;
import com.freelancehub.repository.ContractRepository;
import com.freelancehub.repository.ProposalRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final ProposalRepository proposalRepository;

    public ContractService(
            ContractRepository contractRepository,
            ProposalRepository proposalRepository) {

        this.contractRepository = contractRepository;
        this.proposalRepository = proposalRepository;
    }

    @Transactional
    public Contract createContract(
            Long proposalId,
            LocalDate startDate,
            LocalDate endDate) {

        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Proposal not found"));

        if (!"ACCEPTED".equals(proposal.getStatus())) {
            throw new IllegalArgumentException(
                    "Contract can only be created for accepted proposals");
        }

        if (contractRepository.findByProposal_Id(proposalId).isPresent()) {
            throw new IllegalArgumentException(
                    "Contract already exists for this proposal");
        }

        if (startDate == null || endDate == null ||
                endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Invalid contract dates");
        }

        Contract contract = new Contract(proposal, startDate, endDate);

        return contractRepository.save(contract);
    }

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public List<Contract> getContractsForClient(Long clientId) {
        return contractRepository
                .findByProposal_Project_Client_Id(clientId);
    }

    public List<Contract> getContractsForFreelancer(Long freelancerId) {
        return contractRepository
                .findByProposal_Freelancer_Id(freelancerId);
    }

    public Contract getContractByProposal(Long proposalId) {
        return contractRepository.findByProposal_Id(proposalId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Contract not found"));
    }
}
