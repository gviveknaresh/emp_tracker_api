package com.emp_managment.emp_tracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "performance_reviews",
        indexes = {
                @Index(name = "idx_reviews_employee", columnList = "employee_id"),
                @Index(name = "idx_reviews_cycle", columnList = "cycle_id"),
        }
)
@Getter
@NoArgsConstructor
public class PerformanceReviewEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     *  * ID-only association pattern:
     *  * - employeeId is used for inserts/updates (avoids fetching EmployeeEntity)
     *  * - employee is read-only and lazily loaded when accessed
     *  * - directly fetching through ids than join fetch
     *
     * Trade-offs:
     * - Hibernate does not manage the relationship via the employee field
     * - Referential integrity is enforced only at the database level (FK constraint)
     * - Invalid IDs may lead to database exceptions instead of application-level validation
     *
     * Can be converted to a regular association if strict consistency and ORM-level
     * relationship management are required.
     *
     **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    @JsonBackReference
    private EmployeeEntity employee;
    @Column(name = "employee_id")
    private Long employeeId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    private ReviewCycleEntity cycle;
    @Column(name = "cycle_id")
    private Long cycleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    private EmployeeEntity reviewedBy;
    @Column(name = "reviewed_by")
    private Long reviewerId;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "reviewer_notes", columnDefinition = "TEXT")
    private String reviewerNotes;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;


    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setReviewerNotes(String reviewerNotes) {
        this.reviewerNotes = reviewerNotes;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setCycleId(Long cycleId) {
        this.cycleId = cycleId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}