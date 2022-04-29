package br.com.letscode.compra.service;

import br.com.letscode.compra.model.Produto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class ProdutoService {

    public static Produto getProduct(String identifier) {
        WebClient client = WebClient.create("http://localhost:8081");
        return client.method(HttpMethod.GET)
                .uri("/produto/{identifier}", identifier)
                .header("Authorization", )
                .retrieve()
                .bodyToMono(Produto.class)
                .block();
    }

    public static void updateQuantity(Map<String, Integer> produtos) {
        WebClient client = WebClient.create("http://localhost:8081");
        client
                .patch()
                .uri("/produto")
                .bodyValue(produtos)
                .retrieve()
                .bodyToMono(Produto.class)
                .block();
    }

    public static Produto getProduct2(String identifier) {
        WebClient client = WebClient.create("http://localhost:8081");
        return client.method(HttpMethod.GET)
                .uri("/produto/{identifier}", identifier)
                .retrieve()
                .bodyToMono(Produto.class)
                .block();
    }
}
