package br.com.letscode.compra.dto;

import br.com.letscode.compra.model.CompraProduto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CompraProdutoResponse {

    private String codigo;
    private String nome;
    private Double preco_unitario;
    private Integer quantidade;


    public static CompraProdutoResponse convert(CompraProduto produto){
        CompraProdutoResponse produtoResponse = new CompraProdutoResponse();

        produtoResponse.setCodigo(produto.getProduto().getCodigo());
        produtoResponse.setNome(produto.getProduto().getNome());
        produtoResponse.setPreco_unitario(produto.getProduto().getPreco());
        produtoResponse.setQuantidade(produto.getQuantidade());
        return produtoResponse;
    }
}
