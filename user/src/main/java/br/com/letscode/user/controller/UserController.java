package br.com.letscode.user.controller;

import br.com.letscode.user.dto.LoginRequest;
import br.com.letscode.user.dto.TokenDTO;
import br.com.letscode.user.dto.UserRequest;
import br.com.letscode.user.dto.UserResponse;
import br.com.letscode.user.service.TokenService;
import br.com.letscode.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginRequest loginDTO) {

        UsernamePasswordAuthenticationToken user
                = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        Authentication authentication = authenticationManager.authenticate(user);
        String token = tokenService.generateToken(authentication);

        TokenDTO tokenResponse = new TokenDTO();
        tokenResponse.setToken(token);
        return tokenResponse;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody @Valid UserRequest userRequest){
        userService.createUser(userRequest);
    }

    @GetMapping("/authenticate")
    public UserResponse login(HttpServletRequest request) {
        return tokenService.userToken(request.getHeader("Authorization").split(" ")[1]);
    }
}
