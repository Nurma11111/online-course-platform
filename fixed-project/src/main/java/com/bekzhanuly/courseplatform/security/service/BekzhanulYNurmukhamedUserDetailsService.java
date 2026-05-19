package com.bekzhanuly.courseplatform.security.service;

import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedUser;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserDetailsService implementation for Spring Security
 * Author: Bekzhanuly Nurmukhamed
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BekzhanulYNurmukhamedUserDetailsService implements UserDetailsService {

    private final BekzhanulYNurmukhamedUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        log.debug("Loading user by username/email: {}", usernameOrEmail);
        BekzhanulYNurmukhamedUser user = userRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> {
                    log.warn("User not found: {}", usernameOrEmail);
                    return new UsernameNotFoundException("User not found: " + usernameOrEmail);
                });
        return new BekzhanulYNurmukhamedUserDetails(user);
    }
}
