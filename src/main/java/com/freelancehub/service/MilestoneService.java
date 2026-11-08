package com.freelancehub.service;

import com.freelancehub.model.Contract;
import com.freelancehub.model.Milestone;
import com.freelancehub.repository.ContractRepository;
import com.freelancehub.repository.MilestoneRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final ContractRepository contractRepository;

    public MilestoneService(
            MilestoneRepository milestoneRepository,
            ContractRepository contractRepository) {

        this.milestoneRepository = milestoneRepository;
        this.contractRepository = contractRepository;
    }

    // Create a new milestone
    public Milestone createMilestone(
            Long contractId,
            String title,
            String description,
            double amount,
            LocalDate dueDate) {

        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Contract not found"));

        if (!"ACTIVE".equals(contract.getStatus())) {
            throw new IllegalArgumentException(
                    "Milestones can only be added to active contracts");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero");
        }

        if (dueDate == null ||
                dueDate.isBefore(contract.getStartDate()) ||
                dueDate.isAfter(contract.getEndDate())) {
            throw new IllegalArgumentException(
                    "Due date must be within the contract period");
        }

        // Calculate existing milestone total
        List<Milestone> existingMilestones =
                milestoneRepository.findByContract_Id(contractId);

        double existingTotal = existingMilestones.stream()
                .mapToDouble(Milestone::getAmount)
                .sum();

        // Check contract budget
        double contractBudget = contract.getProposal()
                .getProposedBudget();

        if (existingTotal + amount > contractBudget) {
            throw new IllegalArgumentException(
                    "Total milestone amount cannot exceed contract budget. Remaining budget: "
                            + (contractBudget - existingTotal));
        }

        Milestone milestone = new Milestone(
                contract, title, description, amount, dueDate);

        return milestoneRepository.save(milestone);
    }

    // Get milestones of a contract
    public List<Milestone> getMilestonesByContract(Long contractId) {

        return milestoneRepository.findByContract_Id(contractId);
    }

    // Update milestone status
    public void updateMilestoneStatus(Long milestoneId, String status) {

        if (!"PENDING".equals(status) &&
                !"IN_PROGRESS".equals(status) &&
                !"COMPLETED".equals(status)) {
            throw new IllegalArgumentException("Invalid milestone status");
        }

        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Milestone not found"));

        milestone.setStatus(status);

        milestoneRepository.save(milestone);
    }

    // Get milestone by ID
    public Milestone getMilestoneById(Long milestoneId) {

        return milestoneRepository.findById(milestoneId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Milestone not found"));
    }
}