package com.cydeo.entity;

import com.cydeo.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "projects")
@SQLRestriction("is_deleted is false")
public class Project extends BaseEntity{

    @Column(unique = true)
    private String projectCode;

    private String projectName;

    @Column(columnDefinition = "DATE")
    private LocalDate startDate;

    @Column(columnDefinition = "DATE")
    private LocalDate endDate;
    private String projectDetail;

    @Enumerated(EnumType.STRING)
    private Status projectStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User assignedManager;

}
