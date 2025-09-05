package com.wbsrisktaskerx.wbsrisktaskerx.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "campaign")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "branches_id", nullable = false)
    Branch branch;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    Admin admin;

    @Column(name = "campaign_name", nullable = false)
    String campaignName;

    @Column(name = "campaign_type", nullable = false)
    String campaignType;

    @Column(name = "start_date", nullable = false)
    OffsetDateTime startDate;

    @Column(name = "end_date", nullable = false)
    OffsetDateTime endDate;

    @Column(name = "budget", nullable = false)
    Double budget;

    @Column(name = "target_audience", nullable = false)
    String targetAudience;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "social_media")
    String socialMedia;

    @Column(name = "search_engine")
    String searchEngine;

    @Column(name = "traditional_channel")
    String traditionalChannel;

    @Column(name = "campaign_goal")
    String campaignGoal;

    @Column(name = "note_details")
    String noteDetails;
    
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    Boolean isActive = true;

    @Builder.Default
    @Column(name = "is_deleted")
    Boolean isDeleted = false;
}
