package br.com.letscode.compra.service;

import br.com.letscode.compra.dto.CompraRequest;
import br.com.letscode.compra.dto.CompraResponse;
import br.com.letscode.compra.dto.KafkaDTO;
import br.com.letscode.compra.exceptions.BadRequest;
import br.com.letscode.compra.kafka.SendKafkaMessage;
import br.com.letscode.compra.model.*;
import br.com.letscode.compra.repository.CompraProdutoRepository;
import br.com.letscode.compra.repository.CompraRepository;
import br.com.letscode.compra.repository.specification.CompraSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final CompraProdutoRepository compraProdutoRepository;
    private final SendKafkaMessage sendKafkaMessage;
    private final ProdutoService produtoService;

    public Page<CompraResponse> listByCPF(String cpf, Pageable pageable) {
        Specification<Compra> specification = Specification.where(null);
        if (cpf != null) {
            specification = Specification.where(CompraSpecification.filterOneByCpf(cpf));
        }
        return compraRepository
                .findAll(specification, pageable)
                .map(CompraResponse::convert);
    }

    public boolean validaProduto(CompraRequest compraRequest, String token) throws BadRequest {
        int qtdeItens = compraRequest.getProdutos().size();
        int qtdeComparacao = 0;
        for (Map.Entry<String,Integer> entry : compraRequest.getProdutos().entrySet()){
            Produto produto = ProdutoService.getProduct(entry, token);
            if (produto!=null){
                qtdeComparacao++;
            }
        }
        return qtdeItens == qtdeComparacao;
    }

        public void enviaKafka(KafkaDTO kafkaDTO) throws BadRequest {
            CompraRequest compraRequest = kafkaDTO.getCompraRequest();
            if(validaProduto(compraRequest, kafkaDTO.getToken())){
                double sum_values = 0.0;

                Compra compra = new Compra();
                compra.setData_compra(compraRequest.getData());
                compra.setCpf(compraRequest.getCpf());
                compra.setStatus("EM PROCESSAMENTO");
                compra.setValor_total_compra(0.0);
                compraRepository.save(compra);
                for (Map.Entry<String,Integer> entry : compraRequest.getProdutos().entrySet()){
                    Produto produto = ProdutoService.getProduct(entry, kafkaDTO.getToken());
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

                compra.setValor_total_compra(sum_values);

                compraRepository.save(compra);

                sendKafkaMessage.sendMessage(kafkaDTO);

            }else{
                throw new BadRequest("Codigo do produto invalido.");
            }
        }
    }


