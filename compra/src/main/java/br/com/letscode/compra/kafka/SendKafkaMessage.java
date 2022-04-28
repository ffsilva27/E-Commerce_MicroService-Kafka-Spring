package br.com.letscode.compra.kafka;

import br.com.letscode.compra.dto.CompraRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendKafkaMessage {

    private final KafkaTemplate<String, CompraRequest> kafkaTemplate;
    public static final String KAFKA_TOPIC = "COMPRA_TOPICO";

    public void sendMessage(CompraRequest compraRequest) {
        kafkaTemplate.send(KAFKA_TOPIC,compraRequest);
    }

}
