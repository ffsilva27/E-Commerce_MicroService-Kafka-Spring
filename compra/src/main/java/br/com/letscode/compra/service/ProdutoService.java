package br.com.letscode.compra.service;

import br.com.letscode.compra.exceptions.BadRequest;
import br.com.letscode.compra.exceptions.NotFound;
import br.com.letscode.compra.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class ProdutoService {

    @Autowired
    private HttpServletRequest request;

    public static Produto getProduct(Map.Entry<String,Integer> entry) throws BadRequest {
        WebClient client = WebClient.create("http://localhost:8081");
        Produto produtoMono = client.method(HttpMethod.GET)
                .uri("/produto/{identifier}", entry.getKey())
                //.header("Authorization",request.getHeader("Authorization"))
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                        response -> Mono.error(new NotFound("Produto não encontrado.")))
                .bodyToMono(Produto.class)
                .block();
        return produtoMono;
    }

    public static void updateQuantity(Map<String, Integer> produtos) {
        WebClient client = WebClient.create("http://localhost:8081");
        client
                .patch()
                .uri("/produto")
                //header("Authorization",request.getHeader("Authorization"))
                .bodyValue(produtos)
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                        response -> Mono.error(new NotFound("Produto não encontrado.")))
                .bodyToMono(Produto.class)
                .block();
    }

    public Produto getProduct2(String identifier) {
        WebClient client = WebClient.create("http://localhost:8081");
        return client.method(HttpMethod.GET)
                .uri("/produto/{identifier}", identifier)
                .header("Authorization",request.getHeader("Authorization"))
                .retrieve()
                .bodyToMono(Produto.class)
                .block();
    }
}
