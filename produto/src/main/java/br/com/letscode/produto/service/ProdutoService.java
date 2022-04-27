package br.com.letscode.produto.service;

import br.com.letscode.produto.dto.ProdutoRequest;
import br.com.letscode.produto.dto.ProdutoResponse;
import br.com.letscode.produto.exception.BadRequest;
import br.com.letscode.produto.exception.NotFound;
import br.com.letscode.produto.model.Produto;
import br.com.letscode.produto.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public Page<ProdutoResponse> listByCodigo(Produto produto) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(produto, matcher);
        Pageable pageable = PageRequest.of(0,5);
        List<Produto> produtoList = produtoRepository.findAll(example);
        List<ProdutoResponse> produtoResponses = produtoList.stream().map(p -> new ProdutoResponse(p)).collect(Collectors.toList());
        Page<ProdutoResponse> retorno = new PageImpl<ProdutoResponse>(produtoResponses, pageable, produtoResponses.size());
        return retorno;
    }

    public Optional<Produto> findByCodigo(String codigo){
        return produtoRepository.findByCodigo(codigo);
    }

    public ProdutoResponse createProduct(ProdutoRequest produtoRequest) {
        String productCode = this.createProductCode();
        Optional<Produto> produtoVerificado = findByCodigo(productCode);

        while (produtoVerificado.isPresent()){
            productCode = this.createProductCode();
            produtoVerificado = findByCodigo(productCode);
        }

        Produto produto = new Produto();
        produto.setCodigo(productCode);
        produto.setNome(produtoRequest.getNome());
        produto.setPreco(produtoRequest.getPreco());
        produto.setQtde_disponivel(produtoRequest.getQtdeDisponivel());

        return new ProdutoResponse(produtoRepository.save(produto));

    }

    public String createProductCode(){
        Random ra = new Random();
        Character prefixo = (char) (ra.nextInt(26) + 'A');
        Integer nAleatorio = ra.nextInt(999);
        String sufixo;
        if(nAleatorio<=9){
            sufixo = "00" + Integer.toString(nAleatorio);
        }else if(nAleatorio<=99){
            sufixo = "0" + Integer.toString(nAleatorio);
        }else{
            sufixo = Integer.toString(nAleatorio);
        }
        return  prefixo + sufixo;
    }

    public void updateQuantity(Map<String, Integer> produtos) throws BadRequest, NotFound {
        for (Map.Entry<String, Integer> entry : produtos.entrySet()) {
            Produto produto = produtoRepository.findByCodigo(entry.getKey()).orElseThrow(() -> new NotFound("Produto não encontrado"));
            if(produto.getQtde_disponivel()< entry.getValue()){
                throw new BadRequest("Qtde insuficiente. Não possuimos estoque suficiente do " + entry.getValue());
            }
            produto.setQtde_disponivel(produto.getQtde_disponivel() - entry.getValue());
            produtoRepository.save(produto);
        }
    }
}