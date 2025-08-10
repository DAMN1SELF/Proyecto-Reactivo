package repository;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductosRepository extends ReactiveMongoRepository<Producto, Integer> {
	Flux<Producto> findByCategoria(String categoria);
	Mono<Void> deleteByNombre(String name);
	@DeleteQuery(value = "{'precioUnitario':{$lt:?0}}")
	Mono<Void> deletePrecio(double precioMax);
}
