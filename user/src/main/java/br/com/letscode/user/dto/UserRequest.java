package br.com.letscode.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
public class UserRequest {
    @NotNull
    private String userName;
    @NotNull
    private String password;
    @NotNull
    private Boolean enabled;
    @NotNull
    private List<String> authority;
}
