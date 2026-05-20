package com.bekzhanuly.courseplatform.service.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Async Email Notification Service
 * Async Process #1 - Email notifications via @Async and CompletableFuture
 * Author: Bekzhanuly Nurmukhamed
 */
@Service
@Slf4j
public class BekzhanulYNurmukhamedEmailNotificationService {

    /**
     * Async #1: Send enrollment confirmation email
     */
    @Async("bekzhanulYAsyncExecutor")
    public CompletableFuture<Boolean> sendEnrollmentConfirmationEmail(
            String toEmail, String studentName, String courseTitle) {
        log.info("[ASYNC] Sending enrollment confirmation email to: {} for course: {}",
                toEmail, courseTitle);
        try {
            // Simulate email sending (replace with actual JavaMailSender)
            Thread.sleep(500);
            log.info("[ASYNC] Enrollment confirmation email sent successfully to: {}", toEmail);
            return CompletableFuture.completedFuture(true);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[ASYNC] Failed to send enrollment email to: {}", toEmail, e);
            return CompletableFuture.completedFuture(false);
        }
    }

    /**
     * Async #2: Send course completion certificate email
     */
    @Async("bekzhanulYAsyncExecutor")
    public CompletableFuture<Boolean> sendCourseCompletionEmail(
            String toEmail, String studentName, String courseTitle) {
        log.info("[ASYNC] Sending course completion certificate to: {} for course: {}",
                toEmail, courseTitle);
        try {
            Thread.sleep(700);
            log.info("[ASYNC] Course completion email sent successfully to: {}", toEmail);
            return CompletableFuture.completedFuture(true);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[ASYNC] Failed to send completion email to: {}", toEmail, e);
            return CompletableFuture.completedFuture(false);
        }
    }

    /**
     * Async #3: Send welcome email after registration
     */
    @Async("bekzhanulYAsyncExecutor")
    public CompletableFuture<Boolean> sendWelcomeEmail(String toEmail, String username) {
        log.info("[ASYNC] Sending welcome email to new user: {}", username);
        try {
            Thread.sleep(300);
            log.info("[ASYNC] Welcome email sent to: {}", toEmail);
            return CompletableFuture.completedFuture(true);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[ASYNC] Failed to send welcome email to: {}", toEmail, e);
            return CompletableFuture.completedFuture(false);
        }
    }

    /**
     * Async #4: Send new review notification to instructor
     */
    @Async("bekzhanulYAsyncExecutor")
    public CompletableFuture<Void> notifyInstructorAboutReview(
            String instructorEmail, String courseTitle, int rating) {
        log.info("[ASYNC] Notifying instructor {} about new {} star review on course: {}",
                instructorEmail, rating, courseTitle);
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(200);
                log.info("[ASYNC] Instructor notification sent for course: {}", courseTitle);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
