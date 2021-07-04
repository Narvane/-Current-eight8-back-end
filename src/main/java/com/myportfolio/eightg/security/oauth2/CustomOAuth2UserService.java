package com.myportfolio.eightg.security.oauth2;

import com.myportfolio.eightg.exception.OAuth2AuthenticationProcessingException;
import com.myportfolio.eightg.model.ApplicationUser;
import com.myportfolio.eightg.model.AuthProvider;
import com.myportfolio.eightg.repository.ApplicationUserRepository;
import com.myportfolio.eightg.security.UserPrincipal;
import com.myportfolio.eightg.security.oauth2.user.OAuth2UserInfo;
import com.myportfolio.eightg.security.oauth2.user.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final ApplicationUserRepository applicationUserRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {

            return processOAuth2User(oAuth2UserRequest, oAuth2User);

        } catch (AuthenticationException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());

        }

    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        if(oAuth2UserInfo.getEmail().isEmpty()) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<ApplicationUser> userOptional = applicationUserRepository.findByEmail(oAuth2UserInfo.getEmail());
        ApplicationUser applicationUser;

        if(userOptional.isPresent()) {
            applicationUser = userOptional.get();
            if(!applicationUser.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        applicationUser.getProvider() + " account. Please use your " + applicationUser.getProvider() +
                        " account to login.");
            }
            applicationUser = updateExistingUser(applicationUser, oAuth2UserInfo);

        } else {

            applicationUser = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);

        }

        return UserPrincipal.create(applicationUser, oAuth2User.getAttributes());

    }

    private ApplicationUser registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {

        ApplicationUser applicationUser = new ApplicationUser();

        applicationUser.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        applicationUser.setProviderId(oAuth2UserInfo.getId());
        applicationUser.setName(oAuth2UserInfo.getName());
        applicationUser.setEmail(oAuth2UserInfo.getEmail());
        applicationUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return applicationUserRepository.save(applicationUser);

    }

    private ApplicationUser updateExistingUser(ApplicationUser existingUser, OAuth2UserInfo oAuth2UserInfo) {

        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());

        return applicationUserRepository.save(existingUser);
    }

}
