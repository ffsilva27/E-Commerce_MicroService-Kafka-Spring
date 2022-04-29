package br.com.letscode.compra.kafka;

import br.com.letscode.compra.controller.CompraController;
import br.com.letscode.compra.dto.CompraRequest;
import br.com.letscode.compra.exceptions.BadRequest;
import br.com.letscode.compra.model.Compra;
import br.com.letscode.compra.model.CompraProduto;
import br.com.letscode.compra.model.CompraProdutoKey;
import br.com.letscode.compra.model.Produto;
import br.com.letscode.compra.repository.CompraProdutoRepository;
import br.com.letscode.compra.repository.CompraRepository;
import br.com.letscode.compra.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReceiveKafkaMessage {

    private final String KAFKA_TOPIC = "COMPRA_TOPICO_PROCESSADA";
    private final CompraRepository compraRepository;
    private final CompraProdutoRepository compraProdutoRepository;
    private final ProdutoService produtoService;

    @KafkaListener(topics = KAFKA_TOPIC, groupId = "group-1")
    public void listenTopicCreateCompra(CompraRequest compraRequest) throws BadRequest {

        double sum_values = 0.0;

        Compra compra = new Compra();
        compra.setData_compra(compraRequest.getData());
        compra.setCpf(compraRequest.getCpf());
        compra.setValor_total_compra(0.0);

        compraRepository.save(compra);

        for (Map.Entry<String,Integer> entry : compraRequest.getProdutos().entrySet()){
            Produto produto = produtoService.getProduct(entry);
            if (produto==null){
                compraProdutoRepository.deleteAll(compra.getProdutos());
                compraRepository.delete(compra);
                throw new BadRequest("Produto não encontrado");
            }
            if (produto.getQtde_disponivel() < entry.getValue()) {
                compraProdutoRepository.deleteAll(compra.getProdutos());
                compraRepository.delete(compra);
                throw new BadRequest("Quantidade indisponível do produto: " + produto.getNome());
            }
            CompraProdutoKey key = new CompraProdutoKey();
            key.setIdCompra(compra.getId());
            key.setIdProduto(produto.getId());

            CompraProduto compraProduto = new CompraProduto();
            compraProduto.setCompra(compra);
            compraProduto.setProduto(produto);
            compraProduto.setQuantidade(entry.getValue());
            compraProduto.setCompraProdutoKey(key);

            compraProdutoRepository.save(compraProduto);
            compra.getProdutos().add(compraProduto);

            sum_values += produto.getPreco()*entry.getValue();
        }

        produtoService.updateQuantity(compraRequest.getProdutos());
        compra.setValor_total_compra(sum_values);

        if(compra.getValor_total_compra() == null){
            throw new BadRequest("O campo produtos deve ser preenchido.");
        }

        compraRepository.save(compra);

        CompraController.compras.put(compraRequest.getCpf(), compraRequest);
    }

}