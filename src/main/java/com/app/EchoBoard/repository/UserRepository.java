package com.app.echoboard.repository;

import com.app.echoboard.model.Role;
import com.app.echoboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User data access interface
 * Inherit JpaRepository to provide basic CRUD operations
 * Add business-related query methods
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     * @param email user email
     * @return user information (may be null)
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by email (case-insensitive)
     * @param email user email
     * @return user information (may be null)
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Check if email exists
     * @param email user email
     * @return whether it exists
     */
    boolean existsByEmail(String email);

    /**
     * Find user list by user role
     * @param role user role
     * @return user list of this role
     */
    List<User> findByRole(Role role);

    /**
     * Find active user list
     * @return active user list
     */
    List<User> findByIsActiveTrue();

    /**
     * Find user by OAuth provider and OAuth ID
     * @param oauthProvider OAuth provider (e.g., "github", "google")
     * @param oauthId OAuth user ID
     * @return user information (may be null)
     */
    Optional<User> findByOauthProviderAndOauthId(String oauthProvider, String oauthId);

    /**
     * Find user list by OAuth provider
     * @param oauthProvider OAuth provider
     * @return user list of this provider
     */
    List<User> findByOauthProvider(String oauthProvider);

    /**
     * Find user list by name (case-insensitive)
     * @param name user name keyword
     * @return matching user list
     */
    List<User> findByNameContainingIgnoreCase(String name);

    /**
     * Find user list by role and active status
     * @param role user role
     * @param isActive whether active
     * @return matching user list
     */
    List<User> findByRoleAndIsActive(Role role, Boolean isActive);

    /**
     * Count users by role
     * @param role user role
     * @return user count
     */
    long countByRole(Role role);

    /**
     * Count active users
     * @return active user count
     */
    long countByIsActiveTrue();

    /**
     * Custom query: find users by project ID
     * @param projectId project ID
     * @return project member user list
     */
    @Query("SELECT DISTINCT u FROM User u " +
           "JOIN u.projectMemberships pm " +
           "WHERE pm.project.id = :projectId " +
           "AND pm.status = 'ACTIVE'")
    List<User> findActiveUsersByProjectId(@Param("projectId") Long projectId);

    /**
     * Custom query: find users by project ID and role
     * @param projectId project ID
     * @param role project role
     * @return matching user list
     */
    @Query("SELECT DISTINCT u FROM User u " +
           "JOIN u.projectMemberships pm " +
           "WHERE pm.project.id = :projectId " +
           "AND pm.projectRole = :role " +
           "AND pm.status = 'ACTIVE'")
    List<User> findActiveUsersByProjectIdAndRole(@Param("projectId") Long projectId, 
                                                 @Param("role") Role role);
}