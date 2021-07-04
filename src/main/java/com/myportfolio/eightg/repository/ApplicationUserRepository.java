package com.myportfolio.eightg.repository;

import com.myportfolio.eightg.model.ApplicationUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends MongoRepository<ApplicationUser, String> {

    Optional<ApplicationUser> findByEmail(String email);

}
