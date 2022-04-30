package br.com.letscode.compra.kafka;

import br.com.letscode.compra.dto.CompraRequest;
import br.com.letscode.compra.dto.KafkaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SendKafkaMessage {

    private final KafkaTemplate<String, KafkaDTO> kafkaTemplate;
    public static final String KAFKA_TOPIC = "topic-compra";

    public void sendMessage(KafkaDTO kafkaDTO) {
        kafkaTemplate.send(KAFKA_TOPIC, kafkaDTO);
    }

}
