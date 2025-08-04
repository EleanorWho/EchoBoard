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
 * 项目实体
 * EchoBoard协作的核心单位，支持小团队(2-10人)协作
 */
@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "项目名称不能为空")
    @Size(min = 2, max = 100, message = "项目名称长度应在2-100字符之间")
    @Column(nullable = false)
    private String name;

    @Size(max = 500, message = "项目描述不能超过500字符")
    @Column(columnDefinition = "TEXT")
    private String description;

    // 项目状态
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.ACTIVE;

    // 项目创建者
    @NotNull(message = "项目创建者不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    // 第三方集成配置（为未来扩展预留）
    @Column(name = "github_repo_url")
    private String githubRepoUrl; // GitHub仓库地址

    @Column(name = "github_repo_owner")
    private String githubRepoOwner; // GitHub仓库所有者

    @Column(name = "github_repo_name")
    private String githubRepoName; // GitHub仓库名称

    @Column(name = "figma_file_url")
    private String figmaFileUrl; // Figma设计文件地址

    @Column(name = "figma_file_key")
    private String figmaFileKey; // Figma文件ID

    // 项目设置
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false; // 项目是否公开

    @Column(name = "max_members")
    private Integer maxMembers = 10; // 最大成员数量限制

    // 审计字段
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 项目成员关系
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProjectMember> members = new HashSet<>();

    // 构造函数
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

    // 业务方法
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
     * 项目状态枚举
     */
    public enum ProjectStatus {
        ACTIVE("活跃"),
        ARCHIVED("已归档"),
        DELETED("已删除");

        private final String displayName;

        ProjectStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}