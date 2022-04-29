package br.com.letscode.produto.controller;

import br.com.letscode.produto.annotation.Authenticate;
import br.com.letscode.produto.dto.ProdutoRequest;
import br.com.letscode.produto.dto.ProdutoResponse;
import br.com.letscode.produto.exception.BadRequest;
import br.com.letscode.produto.exception.NotFound;
import br.com.letscode.produto.model.Produto;
import br.com.letscode.produto.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    ProdutoService produtoService;

    @GetMapping
    @Authenticate
    public ResponseEntity<Object> getAll(Produto produto) throws NotFound {
        return ResponseEntity.ok(produtoService.listByCodigo(produto));
    }

    @PostMapping
    @Authenticate
    public ResponseEntity<ProdutoResponse> createProduct(@RequestBody @Valid ProdutoRequest produtoRequest, BindingResult bindingResult) throws BadRequest {
        if(bindingResult.hasErrors()){
            throw new BadRequest("O campo " + bindingResult.getFieldError().getField() + " deve ser preenchido.");
        }
        return ResponseEntity.ok(produtoService.createProduct(produtoRequest));
    }

    @GetMapping("/{id}")
    @Authenticate
    public Produto getProduct(@PathVariable String id) throws NotFound {
        return produtoService.findByCodigo(id).orElseThrow(()->new NotFound("Produto não encontrado."));
    }

    @PatchMapping
    @Authenticate
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateQuantity(@RequestBody Map<String, Integer> produtos) throws BadRequest, NotFound {
        produtoService.updateQuantity(produtos);
    }
}
