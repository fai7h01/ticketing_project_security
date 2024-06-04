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
@Table(name = "tasks")
@SQLRestriction("is_deleted is false")
public class Task extends BaseEntity {

    private String taskSubject;
    private String taskDetail;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    private Status status;
    @Column(columnDefinition = "DATE")
    private LocalDate assignedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_employee_id")
    private User assignedEmployee;

}
