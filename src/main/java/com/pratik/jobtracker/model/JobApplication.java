package com.pratik.jobtracker.model;
import jakarta.persistence.*;

@Entity
@Table(name="job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique=false,length=100)
    private String company;

    @Column(nullable = false,length=100)
    private String role;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=20)
    private ApplicationStatus status;

    public JobApplication() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
