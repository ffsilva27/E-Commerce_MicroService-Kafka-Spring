package br.com.letscode.compra.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity(name = "compra_produto")
public class CompraProduto {

    @EmbeddedId
    private CompraProdutoKey compraProdutoKey;

    @ManyToOne
    @MapsId("idCompra")
    @JoinColumn(name = "id_compra")
    private Compra compra;

    @ManyToOne
    @MapsId("idProduto")
    @JoinColumn(name = "id_produto")
    private Produto produto;

    @Column(name = "quantidade")
    private Integer quantidade;

}
