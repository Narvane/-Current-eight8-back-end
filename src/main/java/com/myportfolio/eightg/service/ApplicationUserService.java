package com.myportfolio.eightg.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface ApplicationUserService {

    UserDetails loadUserById(String id);

}
