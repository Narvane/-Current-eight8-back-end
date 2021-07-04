package com.myportfolio.eightg.service.impl;

import com.myportfolio.eightg.model.ApplicationUser;
import com.myportfolio.eightg.repository.ApplicationUserRepository;
import com.myportfolio.eightg.security.UserPrincipal;
import com.myportfolio.eightg.service.ApplicationUserService;
import com.myportfolio.exception.handler.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationUserServiceImpl implements ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;

    public UserDetails loadUserById(String id) {
        ApplicationUser applicationUser = applicationUserRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("id" + id + " not founded")
        );

        return UserPrincipal.create(applicationUser);
    }
}
