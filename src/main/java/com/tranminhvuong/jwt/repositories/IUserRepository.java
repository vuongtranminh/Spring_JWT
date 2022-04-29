package com.tranminhvuong.jwt.repositories;

import com.tranminhvuong.jwt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username
     * @param username
     * @return User
     */
    Optional<User> findByUsername(String username);

    /**
     * Check exists an user by username
     * @param username
     * @return Boolean
     */
    Boolean existsByUsername(String username);

    /**
     * Find user by username
     * @param email
     * @return User
     */
    Optional<User> findByEmail(String email);

    /**
     * Check exists an user email
     * @param email
     * @return Boolean
     */
    Boolean existsByEmail(String email);

}
