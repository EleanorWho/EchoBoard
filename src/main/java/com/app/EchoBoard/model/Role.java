package com.app.echoboard.model;

/**
 * User Role Enum
 * Support four core roles in EchoBoard
 */
public enum Role {
    /**
     * Developer - Focus on code implementation, need to understand design intent
     */
    DEVELOPER("Developer", "Focus on code while staying aligned with design intent"),
    
    /**
     * UI/UX Designer - See implementation in real-time, provide contextual feedback
     */
    DESIGNER("Designer", "See implementations in real-time, provide contextual feedback"),
    
    /**
     * Product Owner - Track progress visually, understand technical constraints
     */
    PRODUCT_OWNER("Product Owner", "Track progress visually, understand technical constraints"),
    
    /**
     * Stakeholder - Stay informed without technical complexity
     * May not be used in a small team
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
     * Current stage: All roles can view all content
     * Future expansion: Granular permission control based on resource type and role
     */
    public boolean canView(String resourceType) {
        // MVP stage: All roles have view permission
        return true;
    }

    /**
     * Reserved: Future expansion of operation permissions
     * For example: Only PRODUCT_OWNER can create projects, DEVELOPER can update task status, etc.
     */
    // public boolean canEdit(String resourceType) {
    //     return switch (this) {
    //         case PRODUCT_OWNER -> true; // Can create projects, assign tasks
    //         case DEVELOPER -> List.of("task_status", "code_review").contains(resourceType);
    //         case DESIGNER -> List.of("design_feedback").contains(resourceType);
    //         case STAKEHOLDER -> false; // Read-only permission
    //     };
    // }
}