package br.com.letscode.user.dto;

import br.com.letscode.user.model.Authority;
import br.com.letscode.user.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class UserResponse {
    private String username;
    private List<String> roles;

    public static UserResponse convert(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUserName());
        userResponse.setRoles(
                user.getAuthorities()
                    .stream()
                    .map(authority -> authority.getAuthorityKey().getAuthority())
                    .collect(Collectors.toList())
        );

        return userResponse;
    }
}
