package br.com.letscode.produto.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ProdutoRequest {
   @NotEmpty
    private String nome;
    @NotNull
    private Double preco;
    @NotNull
    private Integer qtdeDisponivel;
}
