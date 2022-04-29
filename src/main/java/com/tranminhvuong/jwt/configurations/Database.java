package com.tranminhvuong.jwt.configurations;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tranminhvuong.jwt.common.ERole;
import com.tranminhvuong.jwt.entities.Role;
import com.tranminhvuong.jwt.repositories.IRoleRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * bên trong chứa các Bean method, các Bean này sẽ đc gọi ngay khi khởi chạy ứng dụng
 */
@Slf4j
@Configuration
public class Database {

    /**
     * CodeFirst. khởi chạy thêm vào DB
     * @param iRoleRepository
     * @return CommandLineRunner
     */
    @Bean
    CommandLineRunner iniCommandLineRunner(IRoleRepository iRoleRepository) { // Code first
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {

                Role adminRole = new Role(ERole.ROLE_ADMIN);
                Role moderatorRole = new Role(ERole.ROLE_MODERATOR);
                Role userRole = new Role(ERole.ROLE_USER);

                log.info("Insert data: " + iRoleRepository.save(adminRole));
                log.info("Insert data: " + iRoleRepository.save(moderatorRole));
                log.info("Insert data: " + iRoleRepository.save(userRole));

            }
        };
    }
}
