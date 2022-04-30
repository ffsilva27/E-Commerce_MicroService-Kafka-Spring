package br.com.letscode.compra.dto;

import br.com.letscode.compra.model.Compra;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CompraResponse {

    private LocalDateTime data_compra;
    private String cpf_cliente;
    private Double valor_total_compra;
    private String status;
    private List<CompraProdutoResponse> produtos = new ArrayList<>();

    public static CompraResponse convert(Compra compra){
        CompraResponse compraReturn = new CompraResponse();
        compraReturn.setData_compra(compra.getData_compra());
        compraReturn.setCpf_cliente(compra.getCpf());
        compraReturn.setValor_total_compra(compra.getValor_total_compra());
        compraReturn.setStatus(compra.getStatus());
        compraReturn.getProdutos().addAll(compra.getProdutos().stream().map(CompraProdutoResponse::convert).collect(Collectors.toList()));
        return compraReturn;
    }

}
