package com.tranminhvuong.jwt.repositories;

import com.tranminhvuong.jwt.common.ERole;
import com.tranminhvuong.jwt.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find role by name
     * @param name
     * @return Role
     */
    Optional<Role> findByName(ERole name);

}
