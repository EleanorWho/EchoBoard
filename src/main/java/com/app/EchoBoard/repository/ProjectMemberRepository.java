package com.app.echoboard.repository;

import com.app.echoboard.model.Project;
import com.app.echoboard.model.ProjectMember;
import com.app.echoboard.model.Role;
import com.app.echoboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Project member relationship data access interface
 * Inherit JpaRepository to provide basic CRUD operations
 * Add project member relationship related query methods
 */
@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    /**
     * Find member relationship by project and user
     * @param project project
     * @param user user
     * @return member relationship (may be null)
     */
    Optional<ProjectMember> findByProjectAndUser(Project project, User user);

    /**
     * Find member relationship by project ID and user ID
     * @param projectId project ID
     * @param userId user ID
     * @return member relationship (may be null)
     */
    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, Long userId);

    /**
     * Check if user is a project member
     * @param project project
     * @param user user
     * @return whether there is a member relationship
     */
    boolean existsByProjectAndUser(Project project, User user);

    /**
     * Check if user is a project member (by ID)
     * @param projectId project ID
     * @param userId user ID
     * @return whether there is a member relationship
     */
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    /**
     * Find all members by project
     * @param project project
     * @return project member list
     */
    List<ProjectMember> findByProject(Project project);

    /**
     * Find all members by project ID
     * @param projectId project ID
     * @return project member list
     */
    List<ProjectMember> findByProjectId(Long projectId);

    /**
     * Find all member relationships by user
     * @param user user
     * @return user's project member relationship list
     */
    List<ProjectMember> findByUser(User user);

    /**
     * Find all member relationships by user ID
     * @param userId user ID
     * @return user's project member relationship list
     */
    List<ProjectMember> findByUserId(Long userId);

    /**
     * Find members by project and status
     * @param project project
     * @param status member status
     * @return matching member list
     */
    List<ProjectMember> findByProjectAndStatus(Project project, ProjectMember.MemberStatus status);

    /**
     * Find members by project ID and status
     * @param projectId project ID
     * @param status member status
     * @return matching member list
     */
    List<ProjectMember> findByProjectIdAndStatus(Long projectId, ProjectMember.MemberStatus status);

    /**
     * Find active members of a project
     * @param project project
     * @return active member list
     */
    default List<ProjectMember> findActiveProjectMembers(Project project) {
        return findByProjectAndStatus(project, ProjectMember.MemberStatus.ACTIVE);
    }

    /**
     * Find active members by project ID
     * @param projectId project ID
     * @return active member list
     */
    default List<ProjectMember> findActiveProjectMembersByProjectId(Long projectId) {
        return findByProjectIdAndStatus(projectId, ProjectMember.MemberStatus.ACTIVE);
    }

    /**
     * Find project relationships by user and status
     * @param user user
     * @param status member status
     * @return matching project relationship list
     */
    List<ProjectMember> findByUserAndStatus(User user, ProjectMember.MemberStatus status);

    /**
     * Find members by project and role
     * @param project project
     * @param projectRole project role
     * @return member list of this role
     */
    List<ProjectMember> findByProjectAndProjectRole(Project project, Role projectRole);

    /**
     * Find members by project ID and role
     * @param projectId project ID
     * @param projectRole project role
     * @return member list of this role
     */
    List<ProjectMember> findByProjectIdAndProjectRole(Long projectId, Role projectRole);

    /**
     * Find members by project, role and status
     * @param project project
     * @param projectRole project role
     * @param status member status
     * @return matching member list
     */
    List<ProjectMember> findByProjectAndProjectRoleAndStatus(Project project, Role projectRole, ProjectMember.MemberStatus status);

    /**
     * Find members by join method
     * @param joinMethod join method
     * @return member list of this join method
     */
    List<ProjectMember> findByJoinMethod(ProjectMember.JoinMethod joinMethod);

    /**
     * Find members by invited by
     * @param invitedBy invited by
     * @return member list of this invited by
     */
    List<ProjectMember> findByInvitedBy(User invitedBy);

    /**
     * Find members by joined at between
     * @param startDate start date
     * @param endDate end date
     * @return member list of this joined at between
     */
    List<ProjectMember> findByJoinedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Count members by project
     * @param project project
     * @return member count
     */
    long countByProject(Project project);

    /**
     * Count active members by project
     * @param project project
     * @param status member status
     * @return active member count
     */
    long countByProjectAndStatus(Project project, ProjectMember.MemberStatus status);

    /**
     * Count members by project and role
     * @param project project
     * @param projectRole project role
     * @return member count of this role
     */
    long countByProjectAndProjectRole(Project project, Role projectRole);

    /**
     * Count members by user
     * @param user user
     * @return member count
     */
    long countByUser(User user);

    /**
     * Count active members by user
     * @param user user
     * @param status member status
     * @return active member count
     */
    long countByUserAndStatus(User user, ProjectMember.MemberStatus status);

    /**
     * Custom query: find members by user ID and role
     * @param userId user ID
     * @param role project role
     * @return matching member list
     */
    @Query("SELECT pm FROM ProjectMember pm " +
           "WHERE pm.user.id = :userId " +
           "AND pm.projectRole = :role " +
           "AND pm.status = 'ACTIVE'")
    List<ProjectMember> findActiveProjectMembershipsByUserIdAndRole(@Param("userId") Long userId, 
                                                                    @Param("role") Role role);

    /**
     * Custom query: find active members by project ID and role
     * @param projectId project ID
     * @param role project role
     * @return matching member list
     */
    @Query("SELECT pm FROM ProjectMember pm " +
           "WHERE pm.project.id = :projectId " +
           "AND pm.projectRole = :role " +
           "AND pm.status = 'ACTIVE'")
    List<ProjectMember> findActiveProjectMembersByProjectIdAndRole(@Param("projectId") Long projectId, 
                                                                   @Param("role") Role role);

    /**
     * Custom query: find recent members by project ID
     * @param projectId project ID
     * @return recent member list (sorted by joined at descending)
     */
    @Query("SELECT pm FROM ProjectMember pm " +
           "WHERE pm.project.id = :projectId " +
           "AND pm.status = 'ACTIVE' " +
           "ORDER BY pm.joinedAt DESC")
    List<ProjectMember> findRecentProjectMembers(@Param("projectId") Long projectId);

    /**
     * Custom query: find recent members by project ID (with pagination)
     * @param projectId project ID
     * @param pageable pagination parameters (can set size to limit number)
     * @return recent member list
     */
    @Query("SELECT pm FROM ProjectMember pm " +
           "WHERE pm.project.id = :projectId " +
           "AND pm.status = 'ACTIVE' " +
           "ORDER BY pm.joinedAt DESC")
    org.springframework.data.domain.Page<ProjectMember> findRecentProjectMembers(
            @Param("projectId") Long projectId, 
            org.springframework.data.domain.Pageable pageable);

    /**
     * Custom query: find members invited by a user
     * @param inviterId invited by ID
     * @return invited member list
     */
    @Query("SELECT pm FROM ProjectMember pm " +
           "WHERE pm.invitedBy.id = :inviterId " +
           "AND pm.joinMethod = 'INVITED'")
    List<ProjectMember> findMembersInvitedByUser(@Param("inviterId") Long inviterId);

    /**
     * Custom query: delete all member relationships in a project (soft delete)
     * @param projectId project ID
     */
    @Query("UPDATE ProjectMember pm SET pm.status = 'LEFT', pm.leftAt = CURRENT_TIMESTAMP " +
           "WHERE pm.project.id = :projectId AND pm.status = 'ACTIVE'")
    void deactivateAllProjectMembers(@Param("projectId") Long projectId);
}