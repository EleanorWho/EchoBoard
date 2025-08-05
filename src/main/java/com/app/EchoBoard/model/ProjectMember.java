package com.app.echoboard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Project member relationship entity
 * Manage user roles and permissions in specific projects
 * Support users in different projects with different roles
 */
@Entity
@Table(name = "project_members", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "user_id"}))
public class ProjectMember {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Project cannot be blank")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @NotNull(message = "User cannot be blank")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Project role cannot be blank")
    @Enumerated(EnumType.STRING)
    @Column(name = "project_role", nullable = false)
    private Role projectRole;

    // Member status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status = MemberStatus.ACTIVE;

    // Join method
    @Enumerated(EnumType.STRING)
    @Column(name = "join_method")
    private JoinMethod joinMethod;

    // Inviter (if invited)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by")
    private User invitedBy;

    // Timestamp
    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @Column(name = "left_at")
    private LocalDateTime leftAt;

    // Constructors
    public ProjectMember() {}

    public ProjectMember(Project project, User user, Role projectRole) {
        this.project = project;
        this.user = user;
        this.projectRole = projectRole;
        this.joinMethod = JoinMethod.DIRECT;
    }

    public ProjectMember(Project project, User user, Role projectRole, User invitedBy) {
        this(project, user, projectRole);
        this.invitedBy = invitedBy;
        this.joinMethod = JoinMethod.INVITED;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(Role projectRole) {
        this.projectRole = projectRole;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public JoinMethod getJoinMethod() {
        return joinMethod;
    }

    public void setJoinMethod(JoinMethod joinMethod) {
        this.joinMethod = joinMethod;
    }

    public User getInvitedBy() {
        return invitedBy;
    }

    public void setInvitedBy(User invitedBy) {
        this.invitedBy = invitedBy;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public LocalDateTime getLeftAt() {
        return leftAt;
    }

    public void setLeftAt(LocalDateTime leftAt) {
        this.leftAt = leftAt;
    }

    // Business methods
    public boolean isActive() {
        return status == MemberStatus.ACTIVE;
    }

    public void leave() {
        this.status = MemberStatus.LEFT;
        this.leftAt = LocalDateTime.now();
    }

    public void reactivate() {
        this.status = MemberStatus.ACTIVE;
        this.leftAt = null;
    }

    /**
     * Check if this member has specific permissions in the project
     * Current MVP stage: All roles have view permission
     * Future expansion: Granular control based on role and resource type
     */
    public boolean hasPermission(String action, String resourceType) {
        if (!isActive()) {
            return false;
        }
        
        // MVP stage: All active members have view permission
        if ("view".equals(action)) {
            return true;
        }
        
        // Reserved: Future expansion of more complex permission logic
        return projectRole.canView(resourceType);
    }

    @Override
    public String toString() {
        return "ProjectMember{" +
                "id=" + id +
                ", project=" + (project != null ? project.getName() : null) +
                ", user=" + (user != null ? user.getName() : null) +
                ", projectRole=" + projectRole +
                ", status=" + status +
                '}';
    }

    /**
     * Member status enum
     */
    public enum MemberStatus {
        ACTIVE("Active"),
        LEFT("Left"),
        SUSPENDED("Suspended");

        private final String displayName;

        MemberStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Join method enum
     */
    public enum JoinMethod {
        DIRECT("Direct join"),
        INVITED("Invited join"),
        OAUTH_SYNC("OAuth sync");

        private final String displayName;

        JoinMethod(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}