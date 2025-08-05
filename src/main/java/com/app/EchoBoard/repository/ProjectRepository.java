package com.app.echoboard.repository;

import com.app.echoboard.model.Project;
import com.app.echoboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 项目数据访问接口
 * 继承JpaRepository提供基础CRUD操作
 * 添加项目相关的查询方法
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * 根据项目名称查找项目
     * @param name 项目名称
     * @return 项目信息（可能为空）
     */
    Optional<Project> findByName(String name);

    /**
     * 根据项目名称查找项目（忽略大小写）
     * @param name 项目名称
     * @return 项目信息（可能为空）
     */
    Optional<Project> findByNameIgnoreCase(String name);

    /**
     * 检查项目名称是否已存在
     * @param name 项目名称
     * @return 是否存在
     */
    boolean existsByName(String name);

    /**
     * 根据创建者查找项目列表
     * @param createdBy 项目创建者
     * @return 该用户创建的项目列表
     */
    List<Project> findByCreatedBy(User createdBy);

    /**
     * 根据创建者ID查找项目列表
     * @param createdById 项目创建者ID
     * @return 该用户创建的项目列表
     */
    List<Project> findByCreatedById(Long createdById);

    /**
     * 根据项目状态查找项目列表
     * @param status 项目状态
     * @return 该状态的项目列表
     */
    List<Project> findByStatus(Project.ProjectStatus status);

    /**
     * 查找活跃项目列表
     * @return 活跃项目列表
     */
    default List<Project> findActiveProjects() {
        return findByStatus(Project.ProjectStatus.ACTIVE);
    }

    /**
     * 根据项目名称模糊搜索（忽略大小写）
     * @param name 项目名称关键词
     * @return 匹配的项目列表
     */
    List<Project> findByNameContainingIgnoreCase(String name);

    /**
     * 根据描述模糊搜索（忽略大小写）
     * @param description 描述关键词
     * @return 匹配的项目列表
     */
    List<Project> findByDescriptionContainingIgnoreCase(String description);

    /**
     * 根据是否公开查找项目
     * @param isPublic 是否公开
     * @return 符合条件的项目列表
     */
    List<Project> findByIsPublic(Boolean isPublic);

    /**
     * 查找公开的活跃项目
     * @return 公开活跃项目列表
     */
    List<Project> findByIsPublicTrueAndStatus(Project.ProjectStatus status);

    /**
     * 根据GitHub仓库信息查找项目
     * @param githubRepoOwner GitHub仓库所有者
     * @param githubRepoName GitHub仓库名称
     * @return 项目信息（可能为空）
     */
    Optional<Project> findByGithubRepoOwnerAndGithubRepoName(String githubRepoOwner, String githubRepoName);

    /**
     * 根据Figma文件Key查找项目
     * @param figmaFileKey Figma文件Key
     * @return 项目信息（可能为空）
     */
    Optional<Project> findByFigmaFileKey(String figmaFileKey);

    /**
     * 查找有GitHub集成的项目
     * @return 有GitHub集成的项目列表
     */
    @Query("SELECT p FROM Project p WHERE p.githubRepoUrl IS NOT NULL AND p.githubRepoOwner IS NOT NULL AND p.githubRepoName IS NOT NULL")
    List<Project> findProjectsWithGithubIntegration();

    /**
     * 查找有Figma集成的项目
     * @return 有Figma集成的项目列表
     */
    @Query("SELECT p FROM Project p WHERE p.figmaFileUrl IS NOT NULL AND p.figmaFileKey IS NOT NULL")
    List<Project> findProjectsWithFigmaIntegration();

    /**
     * 根据创建时间范围查找项目
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 该时间范围内创建的项目列表
     */
    List<Project> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 统计指定用户创建的项目数量
     * @param createdBy 项目创建者
     * @return 项目数量
     */
    long countByCreatedBy(User createdBy);

    /**
     * 统计指定状态的项目数量
     * @param status 项目状态
     * @return 项目数量
     */
    long countByStatus(Project.ProjectStatus status);

    /**
     * 自定义查询：根据用户ID查找用户参与的所有项目
     * @param userId 用户ID
     * @return 用户参与的项目列表
     */
    @Query("SELECT DISTINCT p FROM Project p " +
           "JOIN p.members pm " +
           "WHERE pm.user.id = :userId " +
           "AND pm.status = 'ACTIVE' " +
           "AND p.status = 'ACTIVE'")
    List<Project> findActiveProjectsByUserId(@Param("userId") Long userId);

    /**
     * 自定义查询：根据用户ID和项目角色查找项目
     * @param userId 用户ID
     * @param role 项目中的角色
     * @return 符合条件的项目列表
     */
    @Query("SELECT DISTINCT p FROM Project p " +
           "JOIN p.members pm " +
           "WHERE pm.user.id = :userId " +
           "AND pm.projectRole = :role " +
           "AND pm.status = 'ACTIVE' " +
           "AND p.status = 'ACTIVE'")
    List<Project> findActiveProjectsByUserIdAndRole(@Param("userId") Long userId, 
                                                    @Param("role") com.app.echoboard.model.Role role);

    /**
     * 自定义查询：查找成员数量超过指定数量的项目
     * @param memberCount 成员数量阈值
     * @return 符合条件的项目列表
     */
    @Query("SELECT p FROM Project p WHERE " +
           "(SELECT COUNT(pm) FROM ProjectMember pm WHERE pm.project = p AND pm.status = 'ACTIVE') > :memberCount")
    List<Project> findProjectsWithMoreThanMembers(@Param("memberCount") int memberCount);

    /**
     * 自定义查询：查找可以添加更多成员的项目
     * @return 可以添加成员的项目列表
     */
    @Query("SELECT p FROM Project p WHERE " +
           "(SELECT COUNT(pm) FROM ProjectMember pm WHERE pm.project = p AND pm.status = 'ACTIVE') < p.maxMembers " +
           "AND p.status = 'ACTIVE'")
    List<Project> findProjectsCanAddMoreMembers();
}