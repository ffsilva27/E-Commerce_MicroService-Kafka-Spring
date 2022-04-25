package br.com.letscode.user.service;

import br.com.letscode.user.dto.UserRequest;
import br.com.letscode.user.model.Authority;
import br.com.letscode.user.model.AuthorityKey;
import br.com.letscode.user.model.User;
import br.com.letscode.user.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public void createAuthority(UserRequest userRequest, User user){
        Authority authority = new Authority();

        authority.setAuthorityKey(new AuthorityKey(userRequest.getUserName(), "ROLE_" + userRequest.getAuthority()));
        authority.setUser(user);

        authorityRepository.save(authority);
    }
}
