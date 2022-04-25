package br.com.letscode.compra.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

@Getter @Setter
public class CompraRequest {
    @NotNull
    private LocalDateTime data;
    @NotEmpty
    private String cpf;
    @NotEmpty
    private Map<String, Integer> produtos;
}
