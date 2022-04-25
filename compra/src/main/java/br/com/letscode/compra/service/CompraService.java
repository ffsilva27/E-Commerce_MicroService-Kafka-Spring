package br.com.letscode.compra.service;

import br.com.letscode.compra.dto.CompraRequest;
import br.com.letscode.compra.dto.CompraResponse;
import br.com.letscode.compra.exceptions.BadRequest;
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

import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final CompraProdutoRepository compraProdutoRepository;

    public Page<CompraResponse> listByCPF(String cpf, Pageable pageable) {
        Specification<Compra> specification = Specification.where(null);
        if (cpf != null) {
            specification = Specification.where(CompraSpecification.filterOneByCpf(cpf));
        }
        return compraRepository
                .findAll(specification, pageable)
                .map(CompraResponse::convert);
    }

    @Transactional
    public CompraResponse createCompra(CompraRequest compraRequest) throws BadRequest {
        double sum_values = 0.0;

        Compra compra = new Compra();
        compra.setData_compra(compraRequest.getData());
        compra.setCpf(compraRequest.getCpf());
        compra.setValor_total_compra(0.0);

        compraRepository.save(compra);

        for (Map.Entry<String,Integer> entry : compraRequest.getProdutos().entrySet()){
            Produto produto = ProdutoService.getProduct(entry.getKey());
//            if (produto.isEmpty()){
//                compraProdutoRepository.deleteAll(compra.getProdutos());
//                compraRepository.delete(compra);
//                throw new BadRequest("Produto não encontrado");
//            }
//            if (produto.get().getQtde_disponivel() < entry.getValue()) {
//                compraProdutoRepository.deleteAll(compra.getProdutos());
//                compraRepository.delete(compra);
//                throw new BadRequest("Quantidade indisponível");
//            }
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

        ProdutoService.updateQuantity(compraRequest.getProdutos());
        compra.setValor_total_compra(sum_values);

        if(compra.getValor_total_compra() == null){
            throw new BadRequest("O campo produtos deve ser preenchido.");
        }

        compraRepository.save(compra);

        return CompraResponse.convert(compra);
    }

    public Produto teste(){
        return ProdutoService.getProduct2("A123");
    }

}
