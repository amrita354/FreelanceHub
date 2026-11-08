package com.freelancehub.service;

import com.freelancehub.model.Invoice;
import com.freelancehub.model.Milestone;
import com.freelancehub.repository.InvoiceRepository;
import com.freelancehub.repository.MilestoneRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.Objects;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final MilestoneRepository milestoneRepository;

    public InvoiceService(
            InvoiceRepository invoiceRepository,
            MilestoneRepository milestoneRepository) {

        this.invoiceRepository = invoiceRepository;
        this.milestoneRepository = milestoneRepository;
    }

    // Generate invoice for a completed milestone
    // Only the assigned freelancer can generate it
    @Transactional
    public Invoice generateInvoice(Long milestoneId, Long freelancerId) {

        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Milestone not found"));

        // Verify freelancer ownership
        Long assignedFreelancerId = milestone.getContract()
                .getProposal()
                .getFreelancer()
                .getId();

        if (!Objects.equals(assignedFreelancerId, freelancerId)) {
            throw new IllegalArgumentException(
                    "You cannot generate an invoice for another freelancer's milestone");
        }

        if (!"COMPLETED".equals(milestone.getStatus())) {
            throw new IllegalArgumentException(
                    "Invoice can only be generated for completed milestones");
        }

        if (invoiceRepository.findByMilestone_Id(milestoneId).isPresent()) {
            throw new IllegalArgumentException(
                    "Invoice already exists for this milestone");
        }

        String invoiceNumber = "INV-" +
                UUID.randomUUID().toString()
                        .substring(0, 8).toUpperCase();

        Invoice invoice = new Invoice(
                milestone,
                invoiceNumber,
                LocalDate.now(),
                LocalDate.now().plusDays(15)
        );

        return invoiceRepository.save(invoice);
    }

    // Get invoices for a client
    public List<Invoice> getClientInvoices(Long clientId) {
        return invoiceRepository
                .findByMilestone_Contract_Proposal_Project_Client_Id(clientId);
    }

    // Get invoices for a freelancer
    public List<Invoice> getFreelancerInvoices(Long freelancerId) {
        return invoiceRepository
                .findByMilestone_Contract_Proposal_Freelancer_Id(freelancerId);
    }

    // Simulate payment
    @Transactional
    public void makePayment(Long invoiceId, Long clientId) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invoice not found"));

        Long invoiceClientId = invoice.getMilestone()
                .getContract()
                .getProposal()
                .getProject()
                .getClient()
                .getId();

        if (!invoiceClientId.equals(clientId)) {
            throw new IllegalArgumentException(
                    "You cannot pay another client's invoice");
        }

        if (!"PENDING".equals(invoice.getStatus())) {
            throw new IllegalArgumentException(
                    "Invoice is already paid or processed");
        }

        invoice.setStatus("PAID");

        invoiceRepository.save(invoice);
    }
}