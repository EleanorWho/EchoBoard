package com.app.echoboard.repository;

import com.app.echoboard.model.Role;
import com.app.echoboard.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserRepository 测试类
 * 使用 @DataJpaTest 进行Repository层测试
 */
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindUser() {
        // 创建测试用户
        User user = new User("test@example.com", "Test User", Role.DEVELOPER);
        
        // 保存用户
        User savedUser = userRepository.save(user);
        
        // 验证保存成功
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getName()).isEqualTo("Test User");
        assertThat(savedUser.getRole()).isEqualTo(Role.DEVELOPER);
    }

    @Test
    void testFindByEmail() {
        // 创建并保存测试用户
        User user = new User("developer@echoboard.com", "John Developer", Role.DEVELOPER);
        userRepository.save(user);
        
        // 根据邮箱查找用户
        Optional<User> foundUser = userRepository.findByEmail("developer@echoboard.com");
        
        // 验证查找结果
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("John Developer");
        assertThat(foundUser.get().getRole()).isEqualTo(Role.DEVELOPER);
    }

    @Test
    void testFindByRole() {
        // 创建不同角色的用户
        userRepository.save(new User("dev1@test.com", "Developer 1", Role.DEVELOPER));
        userRepository.save(new User("dev2@test.com", "Developer 2", Role.DEVELOPER));
        userRepository.save(new User("designer@test.com", "Designer", Role.DESIGNER));
        
        // 查找所有开发者
        var developers = userRepository.findByRole(Role.DEVELOPER);
        var designers = userRepository.findByRole(Role.DESIGNER);
        
        // 验证结果
        assertThat(developers).hasSize(2);
        assertThat(designers).hasSize(1);
        assertThat(developers.get(0).getRole()).isEqualTo(Role.DEVELOPER);
        assertThat(designers.get(0).getRole()).isEqualTo(Role.DESIGNER);
    }

    @Test
    void testExistsByEmail() {
        // 创建并保存用户
        userRepository.save(new User("exists@test.com", "Existing User", Role.PRODUCT_OWNER));
        
        // 测试邮箱存在性检查
        assertThat(userRepository.existsByEmail("exists@test.com")).isTrue();
        assertThat(userRepository.existsByEmail("notexists@test.com")).isFalse();
    }

    @Test
    void testOAuthUser() {
        // 创建OAuth用户
        User oauthUser = new User("github@test.com", "GitHub User", Role.DEVELOPER, "github", "12345");
        oauthUser.setOauthUsername("githubuser");
        userRepository.save(oauthUser);
        
        // 根据OAuth信息查找用户
        Optional<User> foundUser = userRepository.findByOauthProviderAndOauthId("github", "12345");
        
        // 验证结果
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getOauthProvider()).isEqualTo("github");
        assertThat(foundUser.get().getOauthId()).isEqualTo("12345");
        assertThat(foundUser.get().isOAuthUser()).isTrue();
    }
}