package br.com.letscode.compra.annotation;

import br.com.letscode.compra.dto.UserResponse;
import br.com.letscode.compra.exceptions.Unauthorized;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticateAspect {

    private final HttpServletRequest request;

    @Before("@annotation(Authenticate)")
    public void logExecutionTime() throws Unauthorized {
        String requestHeader = request.getHeader("Authorization");

        WebClient client = WebClient.create("http://localhost:8083");
        UserResponse userResponse = client.method(HttpMethod.POST)
                .uri("/user/authenticate")
                .header("Authorization", requestHeader)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();

        if (userResponse != null){
            System.out.println(userResponse);
        } else {
            throw new Unauthorized("Unauthorized");
        }
    }

}
