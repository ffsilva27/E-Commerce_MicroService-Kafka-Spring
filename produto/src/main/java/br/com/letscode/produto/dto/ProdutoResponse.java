package br.com.letscode.produto.dto;

import br.com.letscode.produto.model.Produto;
import lombok.*;


@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Builder
public class ProdutoResponse {

    private String codigo;
    private String nome;
    private Double preco;
    private Integer qtde_disponivel;

    public ProdutoResponse(Produto produto){
        this.codigo = produto.getCodigo();
        this.nome = produto.getNome();
        this.preco = produto.getPreco();
        this.qtde_disponivel = produto.getQtde_disponivel();
    }

}
