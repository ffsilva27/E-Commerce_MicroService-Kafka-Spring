package br.com.letscode.compra.kafka;

import br.com.letscode.compra.dto.CompraRequest;
import br.com.letscode.compra.dto.KafkaDTO;
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

    private final CompraRepository compraRepository;


    @KafkaListener(topics = "topic-compra", groupId = "grupo-1")
    public void listenTopicCreateCompra(KafkaDTO kafkaDTO) throws BadRequest {
        CompraRequest compraRequest = kafkaDTO.getCompraRequest();
        List<Compra> compraList = compraRepository.findByCpf(compraRequest.getCpf());
        Compra compra = compraList.get(compraList.size()-1);
        for (Map.Entry<String,Integer> entry : compraRequest.getProdutos().entrySet()){
            Produto produto = ProdutoService.getProduct(entry, kafkaDTO.getToken());
            if (produto.getQtde_disponivel() < entry.getValue()) {
                //compraProdutoRepository.deleteAll(compra.getProdutos());
                compra.setStatus("CANCELADO-ESTOQUE-INSUFICIENTE");
            }else{
                compra.setStatus("CONCLUIDO");
                ProdutoService.updateQuantity(compraRequest.getProdutos(), kafkaDTO.getToken());
            }
        }
        compraRepository.save(compra);
    }

}