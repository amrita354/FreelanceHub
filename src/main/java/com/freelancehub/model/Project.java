
package com.freelancehub.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    private double budget;

    private LocalDate deadline;

    private String status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    protected Project() {
    }

    public Project(String title, String description,
                   double budget, LocalDate deadline,
                   Client client) {
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.deadline = deadline;
        this.client = client;
        this.status = "OPEN";
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getBudget() {
        return budget;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String getStatus() {
        return status;
    }

    public Client getClient() {
        return client;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setStatus(String status) {
    this.status = status;
}
}
