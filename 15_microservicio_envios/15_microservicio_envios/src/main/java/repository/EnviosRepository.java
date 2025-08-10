package repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import model.Envio;
import reactor.core.publisher.Flux;

public interface EnviosRepository extends ReactiveCrudRepository<Envio, Integer>{
	@Query("select * from envios where estado='pendiente'")
	Flux<Envio> findByPendientes();
}
