package damn1self.com.service_producto.service;

import damn1self.com.service_producto.model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {
    Flux<Producto> catalogo();
    Flux<Producto> productosCategoria(String categoria);
    Mono<Producto> productoCodigo(int codigo);
    Mono<Void> altaProducto(Producto producto);
    Mono<Producto> eliminarProducto(int codigo);
    Mono<Producto> actualizarPrecio(int codigo,float precio);
}
