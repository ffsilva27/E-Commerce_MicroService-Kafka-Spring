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

    public void createAuthority(User user, String role){
        Authority authority = Authority.convert(user, "ROLE_" + role);

        authorityRepository.save(authority);
    }
}
