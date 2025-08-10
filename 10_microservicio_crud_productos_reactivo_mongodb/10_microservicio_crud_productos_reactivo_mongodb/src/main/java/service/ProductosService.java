package service;

import model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductosService {
    Flux<Producto> catalogo();

    Flux<Producto> productosCategoria(String categoria);

    Mono<Producto> productoCodigo(Integer codProducto);

    Mono<Void> altaProducto(Producto producto);

    Mono<Producto> actualizarPrecio(Integer codProducto, Double nuevoPrecio);

    Mono<Producto> ajustarStock(Integer codProducto, Integer delta);

    Mono<Producto> eliminarProducto(Integer codProducto);

    Mono<Long> eliminarPorNombre(String nombre);

    Mono<Long> eliminarPorPrecioMenorQue(Double maxPrecio);
}
