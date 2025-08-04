package com.app.echoboard.model;

/**
 * 用户角色枚举
 * 支持EchoBoard中的四种核心角色
 */
public enum Role {
    /**
     * 开发者 - 专注于代码实现，需要了解设计意图
     */
    DEVELOPER("Developer", "Focus on code while staying aligned with design intent"),
    
    /**
     * UI/UX设计师 - 实时查看实现效果，提供上下文反馈
     */
    DESIGNER("Designer", "See implementations in real-time, provide contextual feedback"),
    
    /**
     * 产品负责人 - 可视化跟踪进度，理解技术约束
     */
    PRODUCT_OWNER("Product Owner", "Track progress visually, understand technical constraints"),
    
    /**
     * 利益相关者 - 保持信息同步，无需技术复杂性
     */
    STAKEHOLDER("Stakeholder", "Stay informed without technical complexity");

    private final String displayName;
    private final String description;

    Role(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 当前阶段：所有角色都可以查看所有内容
     * 未来扩展：可以根据资源类型和角色进行细粒度权限控制
     */
    public boolean canView(String resourceType) {
        // MVP阶段：所有角色都有查看权限
        return true;
    }

    /**
     * 预留：未来可以扩展操作权限
     * 例如：只有PRODUCT_OWNER可以创建项目，DEVELOPER可以更新任务状态等
     */
    // public boolean canEdit(String resourceType) {
    //     return switch (this) {
    //         case PRODUCT_OWNER -> true; // 可以创建项目、分配任务
    //         case DEVELOPER -> List.of("task_status", "code_review").contains(resourceType);
    //         case DESIGNER -> List.of("design_feedback").contains(resourceType);
    //         case STAKEHOLDER -> false; // 只读权限
    //     };
    // }
}