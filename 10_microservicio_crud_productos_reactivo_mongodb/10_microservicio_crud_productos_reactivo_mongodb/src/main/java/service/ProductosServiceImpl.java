package service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DuplicateKeyException;

import model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.ProductosRepository;

@Service
public class ProductosServiceImpl implements ProductosService {
	@Autowired
	ProductosRepository productosRepository;
	@Override
	public Flux<Producto> catalogo() {
		return productosRepository.findAll()//Flux<Producto>
				.delayElements(Duration.ofMillis(500));//Flux<Producto>
	}

	@Override
	public Flux<Producto> productosCategoria(String categoria) {
		return productosRepository.findByCategoria(categoria);
	}

    @Override
    public Mono<Producto> productoCodigo(Integer codProducto) {
        return productosRepository.findByCodProducto(codProducto);
    }

    @Override
    public Mono<Void> altaProducto(Producto producto) {
        return productoCodigo(producto.getCodProducto())              // Mono<Producto> si existe, vacío si no
                .flatMap(p -> Mono.<Void>error(
                        new DuplicateKeyException("codProducto ya existe-> " + p.getCodProducto()))) // EXISTE → error
                .switchIfEmpty(
                        productosRepository.save(producto).then()                            // NO existe → guardo y devuelvo Mono<Void>
                );
    }

    @Override
    public Mono<Producto> actualizarPrecio(Integer codProducto, Double nuevoPrecio) {
        return productosRepository.findByCodProducto(codProducto)
                .flatMap(p -> { p.setPrecioUnitario(nuevoPrecio);
                    return productosRepository.save(p);
                });
    }

    @Override
    public Mono<Producto> ajustarStock(Integer codProducto, Integer delta) {
        return productosRepository.findByCodProducto(codProducto)
                .flatMap(p -> {
                    int actual = p.getStock() == null ? 0 : p.getStock();
                    int nuevo = actual + delta;
                    if (nuevo < 0) return Mono.error(new IllegalStateException("Stock insuficiente"));
                    p.setStock(nuevo);
                    return productosRepository.save(p);
                });
    }

    @Override
    public Mono<Producto> eliminarProducto(Integer codProducto) {
        return productosRepository.findByCodProducto(codProducto)
                .flatMap(p -> productosRepository.delete(p).thenReturn(p));
    }

    @Override
    public Mono<Long> eliminarPorNombre(String nombre) {
        return productosRepository.deleteByNombre(nombre);
    }

    @Override
    public Mono<Long> eliminarPorPrecioMenorQue(Double maxPrecio) {
        return productosRepository.deleteByPrecioUnitarioLessThan(maxPrecio);
    }

}
