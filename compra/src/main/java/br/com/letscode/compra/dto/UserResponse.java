package br.com.letscode.compra.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter
@ToString
public class UserResponse {
    private String username;
    private List<String> roles;
}
