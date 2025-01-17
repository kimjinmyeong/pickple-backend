package com.pickple.user.domain.repository;

import com.pickple.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByUsername(String username);

    List<User> findAllByIsDeleteFalse();

    Optional<User> findByUsernameAndIsDeleteFalse(String username);

    // username 중복 여부 확인
    boolean existsByUsername(String username);

    // nickname 중복 여부 확인
    boolean existsByNickname(String nickname);

    // email 중복 여부 확인
    boolean existsByEmail(String email);

    @Query("SELECT u.email FROM User u WHERE u.username = :username")
    Optional<String> findEmailByUsername(@Param("username") String username);
}
