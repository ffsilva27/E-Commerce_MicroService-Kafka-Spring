package br.com.letscode.compra.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter @Setter
@Embeddable
public class CompraProdutoKey implements Serializable {

    @Column(name = "id_compra")
    private Integer idCompra;

    @Column(name = "id_produto")
    private Integer idProduto;

}
