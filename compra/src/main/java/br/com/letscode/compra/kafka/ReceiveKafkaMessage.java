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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReceiveKafkaMessage {

    public static final String KAFKA_TOPIC = "COMPRA_TOPICO";
    private final SendKafkaMessage sendKafkaMessage;

    private final CompraRepository compraRepository;
    private final CompraProdutoRepository compraProdutoRepository;


    @KafkaListener(topics = KAFKA_TOPIC, groupId = "grupo-1")
    public void listenTopicCreateCompra(CompraRequest compraRequest) throws BadRequest {
        Compra compra = compraRepository.findByCpf(compraRequest.getCpf());
        for (Map.Entry<String,Integer> entry : compraRequest.getProdutos().entrySet()){
            Produto produto = ProdutoService.getProduct(entry);
            if (produto.getQtde_disponivel() < entry.getValue()) {
                compraProdutoRepository.deleteAll(compra.getProdutos());
                compra.setStatus("CANCELADO");
            }else{
                compra.setStatus("CONCLUIDO");
            }
        }

        ProdutoService.updateQuantity(compraRequest.getProdutos());
//
//        if(compra.getValor_total_compra() == null){
//            throw new BadRequest("O campo produtos deve ser preenchido.");
//        }

        compraRepository.save(compra);
        //sendKafkaMessage.sendMessage(compraRequest);
    }

}