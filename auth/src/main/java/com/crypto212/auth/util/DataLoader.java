package com.crypto212.auth.util;

import com.crypto212.auth.model.entity.RoleEnum;
import com.crypto212.auth.repository.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
    private final RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (roleRepository.findByRoleName(RoleEnum.USER).isEmpty()) {
            roleRepository.createRole(RoleEnum.USER);
        }
        if (roleRepository.findByRoleName(RoleEnum.ADMIN).isEmpty()) {
            roleRepository.createRole(RoleEnum.ADMIN);
        }
    }
}
