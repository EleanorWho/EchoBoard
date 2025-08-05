package com.app.echoboard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Project Entity
 * Core unit of EchoBoard collaboration, supports small teams (2-10 members)
 */
@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Project name cannot be blank")
    @Size(min = 2, max = 100, message = "Project name length should be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @Size(max = 500, message = "Project description cannot exceed 500 characters")
    @Column(columnDefinition = "TEXT")
    private String description;

    // Project status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.ACTIVE;

    // Project creator
    @NotNull(message = "Project creator cannot be blank")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    // Third-party integration configuration (reserved for future expansion)
    @Column(name = "github_repo_url")
    private String githubRepoUrl; // GitHub repository URL

    @Column(name = "github_repo_owner")
    private String githubRepoOwner; // GitHub repository owner

    @Column(name = "github_repo_name")
    private String githubRepoName; // GitHub repository name

    @Column(name = "figma_file_url")
    private String figmaFileUrl; // Figma design file URL

    @Column(name = "figma_file_key")
    private String figmaFileKey; // Figma file ID

    // Project settings
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false; // Whether the project is public

    @Column(name = "max_members")
    private Integer maxMembers = 10; // Maximum member limit

    // Audit fields
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Project member relationship
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProjectMember> members = new HashSet<>();

    // Constructors
    public Project() {}

    public Project(String name, String description, User createdBy) {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getGithubRepoUrl() {
        return githubRepoUrl;
    }

    public void setGithubRepoUrl(String githubRepoUrl) {
        this.githubRepoUrl = githubRepoUrl;
    }

    public String getGithubRepoOwner() {
        return githubRepoOwner;
    }

    public void setGithubRepoOwner(String githubRepoOwner) {
        this.githubRepoOwner = githubRepoOwner;
    }

    public String getGithubRepoName() {
        return githubRepoName;
    }

    public void setGithubRepoName(String githubRepoName) {
        this.githubRepoName = githubRepoName;
    }

    public String getFigmaFileUrl() {
        return figmaFileUrl;
    }

    public void setFigmaFileUrl(String figmaFileUrl) {
        this.figmaFileUrl = figmaFileUrl;
    }

    public String getFigmaFileKey() {
        return figmaFileKey;
    }

    public void setFigmaFileKey(String figmaFileKey) {
        this.figmaFileKey = figmaFileKey;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<ProjectMember> getMembers() {
        return members;
    }

    public void setMembers(Set<ProjectMember> members) {
        this.members = members;
    }

    // Business methods
    public boolean hasGithubIntegration() {
        return githubRepoUrl != null && githubRepoOwner != null && githubRepoName != null;
    }

    public boolean hasFigmaIntegration() {
        return figmaFileUrl != null && figmaFileKey != null;
    }

    public int getMemberCount() {
        return members != null ? members.size() : 0;
    }

    public boolean canAddMoreMembers() {
        return getMemberCount() < maxMembers;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", createdBy=" + (createdBy != null ? createdBy.getName() : null) +
                ", memberCount=" + getMemberCount() +
                '}';
    }

    /**
     * Project status enum
     */
    public enum ProjectStatus {
        ACTIVE("Active"),
        ARCHIVED("Archived"),
        DELETED("Deleted");

        private final String displayName;

        ProjectStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}