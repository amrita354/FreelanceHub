
package com.freelancehub.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "milestone_id", nullable = false, unique = true)
    private Milestone milestone;

    private String invoiceNumber;

    private double amount;

    private LocalDate issueDate;

    private LocalDate dueDate;

    private String status;

    private LocalDateTime createdAt;

    protected Invoice() {
    }

    public Invoice(Milestone milestone, String invoiceNumber,
                   LocalDate issueDate, LocalDate dueDate) {

        this.milestone = milestone;
        this.invoiceNumber = invoiceNumber;
        this.amount = milestone.getAmount();
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
