package br.com.letscode.compra.kafka;

import br.com.letscode.compra.dto.CompraRequest;
import br.com.letscode.compra.exceptions.BadRequest;
import br.com.letscode.compra.model.Compra;
import br.com.letscode.compra.model.Produto;
import br.com.letscode.compra.repository.CompraProdutoRepository;
import br.com.letscode.compra.repository.CompraRepository;
import br.com.letscode.compra.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReceiveKafkaMessage {

    private final ProdutoService produtoService;
    private final CompraRepository compraRepository;


    @KafkaListener(topics = "topic-compra", groupId = "grupo-1")
    public void listenTopicCreateCompra(CompraRequest compraRequest) throws BadRequest {
        List<Compra> compraList = compraRepository.findByCpf(compraRequest.getCpf());
        Compra compra = compraList.get(compraList.size()-1);
        for (Map.Entry<String,Integer> entry : compraRequest.getProdutos().entrySet()){
            Produto produto = produtoService.getProduct(entry);
            if (produto.getQtde_disponivel() < entry.getValue()) {
                //compraProdutoRepository.deleteAll(compra.getProdutos());
                compra.setStatus("CANCELADO-ESTOQUE-INSUFICIENTE");
            }else{
                compra.setStatus("CONCLUIDO");
                produtoService.updateQuantity(compraRequest.getProdutos());
            }
        }
        compraRepository.save(compra);
    }

}