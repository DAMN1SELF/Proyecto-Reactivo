package repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductosRepository extends ReactiveCrudRepository<Producto, Integer> {
	Flux<Producto> findByCategoria(String categoria);
	@Transactional
	@Modifying
	Mono<Void> deleteByNombre(String name);
	@Transactional
	@Modifying
	@Query(value="delete from productos where precio>?")
	Mono<Void> deletePrecio(double precioMax);
}
