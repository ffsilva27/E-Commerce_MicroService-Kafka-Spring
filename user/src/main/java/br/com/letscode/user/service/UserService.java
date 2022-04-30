package br.com.letscode.user.service;

import br.com.letscode.user.dto.UserRequest;
import br.com.letscode.user.model.User;
import br.com.letscode.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityService authorityService;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserRequest userRequest){
        User user = new User();
        user.setUserName(userRequest.getUserName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEnabled(true);

        final User userDB = userRepository.save(user);

        userRequest.getAuthority().forEach(a -> authorityService.createAuthority(userDB, a));
    }

}
