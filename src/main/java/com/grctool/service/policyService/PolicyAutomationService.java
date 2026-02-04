package com.grctool.service.policyService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.enums.PolicyStatus;
import com.grctool.model.Policy;
import com.grctool.repository.PolicyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyAutomationService {

    private final PolicyRepository policyRepository;

    /**
     * Automated Task: Runs every day at midnight.
     * Identifies policies in DRAFT or UNDER_REVIEW that haven't been updated in 30 days.
     */
    @Scheduled(cron= "0 0 0 L * ?")// For demonstration, runs every 10 seconds. Change to cron expression for production.
  //  @Transactional
    public void processOverduePolicyReviews() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        
        // Fetch policies based on existing status and timestamp
        List<Policy> stagnantPolicies = policyRepository.findAll().stream()
                .filter(p -> (p.getStatus() == PolicyStatus.DRAFT || p.getStatus() == PolicyStatus.UNDER_REVIEW))
                .filter(p -> p.getUpdatedAt().isBefore(cutoffDate))
                .toList();

        for (Policy policy : stagnantPolicies) {
            triggerAttestationReminder(policy);
        }
    }

    private void triggerAttestationReminder(Policy policy) {
        // Logic to notify the owner defined in the Policy model
        if (policy.getOwner() != null) {
            log.info("Automation Triggered: Sending reminder to {} for policy '{}' (ID: {})", 
                policy.getOwner().getEmail(), policy.getTitle(), policy.getId());
            
            // Here you would integrate with an EmailService
            // emailService.sendReminder(policy.getOwner().getEmail(), "Action Required: Policy Review Overdue");
        }
    }

    /**
     * Automated Status Transition:
     * Logic to automatically archive policies that have reached their lifecycle end.
     */
    @Transactional
    public void autoArchivePolicy(Policy policy) {
        log.info("Policy {} has reached end-of-life. Transitioning to ARCHIVED.", policy.getId());
        policy.setStatus(PolicyStatus.ARCHIVED); // Uses your PolicyStatus enum
        policyRepository.save(policy);
    }

     public void processReminders(LocalDateTime cutoffDate) {
        // Local variable: Created on the stack, cleared as soon as method ends.
        // No memory leak risk!
        List<PolicyStatus> targetStatuses = List.of(PolicyStatus.DRAFT, PolicyStatus.UNDER_REVIEW);
        
        List<Policy> stagnantPolicies = policyRepository.findStagnantPolicies(targetStatuses, cutoffDate);

        for (Policy policy : stagnantPolicies) {
            triggerAttestationReminder(policy);
        }
    }
}
