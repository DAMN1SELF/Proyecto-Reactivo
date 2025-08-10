package service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import model.Pedido;
@Service
public class PedidosServiceImpl implements PedidosService {
	@Value("${topic}")
	String topico;
	@Autowired
	KafkaTemplate<String,Pedido> kafkaTemplate;

	@Override
	public void registrarPedido(Pedido pedido) {
		CompletableFuture<SendResult<String,Pedido>> future=kafkaTemplate.send(topico, pedido);
		future.whenCompleteAsync((r,t)->{
			if(t!=null) {
				throw new RuntimeException();
			}
			System.out.println("Se ha registrado el pedido "+r.getProducerRecord().value().getNombre()+" en el tópico "+r.getRecordMetadata().topic());
		});
	}

}
