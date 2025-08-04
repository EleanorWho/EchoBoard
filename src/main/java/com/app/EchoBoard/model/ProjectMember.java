package com.app.echoboard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 项目成员关系实体
 * 管理用户在特定项目中的角色和权限
 * 支持用户在不同项目中担任不同角色
 */
@Entity
@Table(name = "project_members", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "user_id"}))
public class ProjectMember {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "项目不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @NotNull(message = "用户不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "项目角色不能为空")
    @Enumerated(EnumType.STRING)
    @Column(name = "project_role", nullable = false)
    private Role projectRole;

    // 成员状态
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status = MemberStatus.ACTIVE;

    // 加入方式
    @Enumerated(EnumType.STRING)
    @Column(name = "join_method")
    private JoinMethod joinMethod;

    // 邀请者（如果是被邀请加入的）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by")
    private User invitedBy;

    // 时间戳
    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @Column(name = "left_at")
    private LocalDateTime leftAt;

    // 构造函数
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

    // 业务方法
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
     * 检查该成员在项目中是否有特定权限
     * 当前MVP阶段：所有角色都有查看权限
     * 未来扩展：可以根据角色和资源类型进行细粒度控制
     */
    public boolean hasPermission(String action, String resourceType) {
        if (!isActive()) {
            return false;
        }
        
        // MVP阶段：所有活跃成员都有查看权限
        if ("view".equals(action)) {
            return true;
        }
        
        // 预留：未来可以扩展更复杂的权限逻辑
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
     * 成员状态枚举
     */
    public enum MemberStatus {
        ACTIVE("活跃"),
        LEFT("已离开"),
        SUSPENDED("已暂停");

        private final String displayName;

        MemberStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * 加入方式枚举
     */
    public enum JoinMethod {
        DIRECT("直接加入"),
        INVITED("邀请加入"),
        OAUTH_SYNC("OAuth同步");

        private final String displayName;

        JoinMethod(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}