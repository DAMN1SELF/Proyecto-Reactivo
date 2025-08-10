package repository;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductosRepository extends ReactiveMongoRepository<Producto, Integer> {

    // Buscar por el id de negocio (campo único codProducto)
    Mono<Producto> findByCodProducto(Integer codProducto);

    // Listar por categoría
    Flux<Producto> findByCategoria(String categoria);

    // Saber si existe un codProducto
    Mono<Boolean> existsByCodProducto(Integer codProducto);

    // Eliminar por nombre (devuelve cantidad eliminada)
    Mono<Long> deleteByNombre(String nombre);

    // Eliminar todos con precioUnitario < max (devuelve cantidad eliminada)
    @DeleteQuery("{ 'precioUnitario': { $lt: ?0 } }")
    Mono<Long> deleteByPrecioUnitarioLessThan(Double maxPrecio);

}
