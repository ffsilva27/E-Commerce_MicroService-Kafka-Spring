package br.com.letscode.compra.kafka;

import br.com.letscode.compra.controller.CompraController;
import br.com.letscode.compra.dto.CompraRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReceiveKafkaMessage {

    private final String KAFKA_TOPIC = "COMPRA_TOPICO_PROCESSADA";

    @KafkaListener(topics = KAFKA_TOPIC, groupId = "group-1")
    public void listenTopicCreateCompra(CompraRequest compraDTO) {
        CompraController.compras.put(compraDTO.getCpf(), compraDTO);
    }

}