
package com.freelancehub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "proposals")
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String coverLetter;

    private double proposedBudget;

    private int estimatedDays;

    private String status;

    private LocalDateTime submittedAt;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    private Freelancer freelancer;

    protected Proposal() {
    }

    public Proposal(String coverLetter, double proposedBudget,
                    int estimatedDays, Project project,
                    Freelancer freelancer) {

        this.coverLetter = coverLetter;
        this.proposedBudget = proposedBudget;
        this.estimatedDays = estimatedDays;
        this.project = project;
        this.freelancer = freelancer;
        this.status = "PENDING";
        this.submittedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public double getProposedBudget() {
        return proposedBudget;
    }

    public int getEstimatedDays() {
        return estimatedDays;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public Project getProject() {
        return project;
    }

    public Freelancer getFreelancer() {
        return freelancer;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
