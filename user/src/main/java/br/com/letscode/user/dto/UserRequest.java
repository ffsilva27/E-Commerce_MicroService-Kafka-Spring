package br.com.letscode.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class UserRequest {
    @NotNull
    private String userName;
    @NotNull
    private String password;
    @NotNull
    private String authority;
}
