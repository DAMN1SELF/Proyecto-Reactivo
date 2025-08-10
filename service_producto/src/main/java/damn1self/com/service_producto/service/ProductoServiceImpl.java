package damn1self.com.service_producto.service;

import damn1self.com.service_producto.model.Producto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private static final List<Producto> productos = new ArrayList<>(List.of(
            new Producto(101, "Laptop Lenovo", "Tecnologia", 2999.99, 15),
            new Producto(102, "Smartphone Samsung", "Tecnologia", 1999.50, 30),
            new Producto(103, "Mouse Logitech", "Accesorios", 89.90, 100),
            new Producto(104, "Silla ergonomica", "Oficina", 450.00, 20),
            new Producto(105, "Audifonos Sony", "Accesorios", 299.99, 50)
    ));


    @Override
    public Flux<Producto> catalogo() {
        return Flux.fromIterable(productos)
                .delayElements(Duration.ofSeconds(2)) ;
    }

    @Override
    public Flux<Producto> productosCategoria(String categoria) {
        return catalogo()
                .filter(p -> p.getCategoria().equals(categoria));
    }

    @Override
    public Mono<Producto> productoCodigo(int codigo) {
        return catalogo()
                .filter(producto -> producto.getCodProducto()==codigo)
                .next();
    }

//    @Override
//    public Mono<Void> altaProducto(Producto producto) {
//        return productoCodigo(producto.getCodProducto())
//                .flatMap(p -> Mono.error(new RuntimeException("Producto ya registrado")))
//                .switchIfEmpty(Mono.defer(() -> {
//                    productos.add(producto);  // Asegúrate de que 'productos' sea mutable
//                    return Mono.empty();
//                }))
//                .then();
//    }
@Override
public Mono<Void> altaProducto(Producto producto) {
    return productoCodigo(producto.getCodProducto())//Mono<Producto>
            .switchIfEmpty(Mono.just(producto).map(p->{
                productos.add(producto);
                return p;
            }))//Mono<Producto>
            .then();//Mono<Void>
}



    @Override
    public Mono<Producto> eliminarProducto(int codigo) {
        return productoCodigo(codigo)
                .map(producto1 -> {
                    productos.removeIf(p->p.getCodProducto()==codigo);
                    return producto1;
                });
    }

    @Override
    public Mono<Producto> actualizarPrecio(int codigo, float precio) {
        return productoCodigo(codigo)
                .map(p->{
                    p.setPrecioUnitario(precio);
                    return p;
                });
    }
}
