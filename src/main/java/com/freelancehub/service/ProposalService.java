
package com.freelancehub.service;

import com.freelancehub.model.Freelancer;
import com.freelancehub.model.Project;
import com.freelancehub.model.Proposal;

import com.freelancehub.repository.ProjectRepository;
import com.freelancehub.repository.ProposalRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final ProjectRepository projectRepository;

    public ProposalService(
            ProposalRepository proposalRepository,
            ProjectRepository projectRepository) {

        this.proposalRepository = proposalRepository;
        this.projectRepository = projectRepository;
    }

    // Freelancer submits a proposal
    public Proposal submitProposal(
            Long projectId,
            Freelancer freelancer,
            String coverLetter,
            double proposedBudget,
            int estimatedDays) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Project not found"
                ));

        if (!"OPEN".equals(project.getStatus())) {
            throw new IllegalArgumentException(
                    "This project is not accepting proposals"
            );
        }

        if (proposedBudget <= 0 || estimatedDays <= 0) {
            throw new IllegalArgumentException(
                    "Budget and estimated days must be positive"
            );
        }

        if (coverLetter == null || coverLetter.isBlank()) {
            throw new IllegalArgumentException(
                    "Cover letter cannot be empty"
            );
        }

        if (proposalRepository.existsByProject_IdAndFreelancer_Id(
                projectId, freelancer.getId())) {

            throw new IllegalArgumentException(
                    "You have already submitted a proposal"
            );
        }

        Proposal proposal = new Proposal(
                coverLetter,
                proposedBudget,
                estimatedDays,
                project,
                freelancer
        );

        return proposalRepository.save(proposal);
    }

    // Get proposals received by a client
    public List<Proposal> getClientProposals(Long clientId) {
        return proposalRepository.findByProject_Client_Id(clientId);
    }

    // Accept or reject a proposal
    public void updateProposalStatus(
            Long proposalId,
            Long clientId,
            String status) {

        if (!"ACCEPTED".equals(status)
                && !"REJECTED".equals(status)) {

            throw new IllegalArgumentException(
                    "Invalid proposal status"
            );
        }

        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Proposal not found"
                ));

        if (!proposal.getProject().getClient().getId()
                .equals(clientId)) {

            throw new IllegalArgumentException(
                    "You cannot manage another client's proposal"
            );
        }

        if (!"PENDING".equals(proposal.getStatus())) {
            throw new IllegalArgumentException(
                    "This proposal has already been reviewed"
            );
        }

        proposal.setStatus(status);
        proposalRepository.save(proposal);

        if ("ACCEPTED".equals(status)) {
            Project project = proposal.getProject();
            project.setStatus("IN_PROGRESS");
            projectRepository.save(project);
        }
    }
    public List<Proposal> getFreelancerProposals(Long freelancerId) {
    return proposalRepository.findByFreelancer_Id(freelancerId);
}
}
