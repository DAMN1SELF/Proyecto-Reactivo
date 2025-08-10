package service;

import model.Envio;
import reactor.core.publisher.Flux;

public interface EnviosService {
	Flux<Envio> pendientes();
}
