package damn1self.com.service_producto.controller;

import damn1self.com.service_producto.model.Producto;
import damn1self.com.service_producto.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin("*")
@RestController
public class ProductoController {
    @Autowired
    ProductoService productosService;
    @GetMapping(value="productos")
    public ResponseEntity<Flux<Producto>> productos(){
        return new ResponseEntity<>(productosService.catalogo(), HttpStatus.OK);
    }
    @GetMapping(value="productos/{categoria}")
    public ResponseEntity<Flux<Producto>> productosCategoria(@PathVariable("categoria") String categoria){
        return new ResponseEntity<>(productosService.productosCategoria(categoria),HttpStatus.OK);
    }
    @GetMapping(value="producto")
    public ResponseEntity<Mono<Producto>> productoCodigo(@RequestParam("cod") int cod) {
        return new ResponseEntity<>(productosService.productoCodigo(cod),HttpStatus.OK);
    }

    @PostMapping(value = "producto/alta", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> altaProducto(@RequestBody Producto producto) {
        return productosService.altaProducto(producto)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(e -> {
                    String msg = e.getMessage(); // 💡 prevenir NPE
                    if (msg != null && msg.contains("Producto ya registrado")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @DeleteMapping(value="producto/eliminar")
    public Mono<ResponseEntity<Producto>> eliminarProducto(@RequestParam("cod") int cod) {
        return productosService.eliminarProducto(cod)//Mono<Producto>
                .map(p->new ResponseEntity<>(p,HttpStatus.OK))//Mono<ResponseEntity<Producto>>
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));//Mono<ResponseEntity<Producto>>
    }
    @PutMapping(value="producto/actualizar-precio")
    public Mono<ResponseEntity<Producto>> actualizarProducto(@RequestParam("cod") int cod,@RequestParam("precio") float precio) {
        return productosService.actualizarPrecio(cod, precio)//Mono<Producto>
                .map(p->new ResponseEntity<>(p,HttpStatus.OK))//Mono<ResponseEntity<Producto>>
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));//Mono<ResponseEntity<Producto>>
    }
}
