package service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import model.Envio;
import reactor.core.publisher.Flux;
import repository.EnviosRepository;
@Service
public class EnviosServiceImpl implements EnviosService {
	@Autowired
	EnviosRepository enviosRepository;
	@Override
	public Flux<Envio> pendientes() {
		return enviosRepository.findByPendientes();
	}
	@KafkaListener(topics = "pedidosTopic", groupId = "myGroup2")
	public void gestionEnvios(Envio envio) {
		envio.setFecha(LocalDateTime.now());
		envio.setEstado("pendiente");
		enviosRepository.save(envio).subscribe();
	}
	
}
