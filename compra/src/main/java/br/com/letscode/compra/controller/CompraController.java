package br.com.letscode.compra.controller;

import br.com.letscode.compra.annotation.Authenticate;
import br.com.letscode.compra.dto.CompraRequest;
import br.com.letscode.compra.dto.CompraResponse;
import br.com.letscode.compra.dto.KafkaDTO;
import br.com.letscode.compra.exceptions.BadRequest;
import br.com.letscode.compra.exceptions.NotFound;
import br.com.letscode.compra.kafka.SendKafkaMessage;
import br.com.letscode.compra.model.Produto;
import br.com.letscode.compra.service.CompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/compra")
public class CompraController {

    private final CompraService compraService;


    @GetMapping()
    @Authenticate
    public ResponseEntity<Object> listCompras(
            @RequestParam(name = "cpf", required = false) String cpf,
            Pageable pageable
    ) throws NotFound {
        Page<CompraResponse> compraReturn = compraService.listByCPF(cpf, pageable);
        if(cpf!=null && compraReturn.isEmpty()){
            throw new NotFound("Compra não encontrada ou cpf incorreto.");
        }else if(compraReturn.isEmpty()){
            return new ResponseEntity(compraReturn, HttpStatus.NO_CONTENT);
        }else{
            return ResponseEntity.ok(compraReturn);
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    @Authenticate
    public void createProduct(@RequestBody @Valid CompraRequest compraRequest, BindingResult bindingResult, HttpServletRequest request) throws BadRequest {
        if(bindingResult.hasErrors()){
            throw new BadRequest("O campo " + bindingResult.getFieldError().getField() + " deve ser preenchido.");
        }
        KafkaDTO kafkaDTO = new KafkaDTO(request.getHeader("Authorization"), compraRequest);
        compraService.enviaKafka(kafkaDTO);
    }

}
