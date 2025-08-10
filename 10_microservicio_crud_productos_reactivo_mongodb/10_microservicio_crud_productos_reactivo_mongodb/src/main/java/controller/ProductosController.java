package controller;

import lombok.RequiredArgsConstructor;
import model.Producto;
import org.springframework.dao.DuplicateKeyException; // ¡este, el de Spring!
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.ProductosService;

@CrossOrigin("*")
@RestController
@RequestMapping("/producto") // <-- base path del controller
@RequiredArgsConstructor
public class ProductosController {

    private final ProductosService service;

    // GET /producto  -> listar todo
    @GetMapping
    public ResponseEntity<Flux<Producto>> listar() {
        return ResponseEntity.ok(service.catalogo());
    }

    // GET /producto/categoria/{categoria} -> listar por categoría
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<Flux<Producto>> listarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(service.productosCategoria(categoria));
    }

    // GET /producto/{cod} -> buscar por código (PathVariable en vez de query param)
    @GetMapping("/{cod}")
    public ResponseEntity<Mono<Producto>> porCodigo(@PathVariable Integer cod) {
        return ResponseEntity.ok(service.productoCodigo(cod));
    }

    // POST /producto/alta -> alta
    @PostMapping(value = "/alta", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> alta(@RequestBody Producto producto) {
        return service.altaProducto(producto).thenReturn(ResponseEntity.ok().build());
    }

    // PUT /producto/{cod}/precio?valor=199.9 -> actualizar precio
    @PutMapping("/{cod}/precio")
    public Mono<ResponseEntity<Producto>> actualizarPrecio(@PathVariable Integer cod,
                                                           @RequestParam("valor") Double precio) {
        return service.actualizarPrecio(cod, precio)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // PATCH /producto/{cod}/stock?delta=-2 -> ajustar stock (+/-)
    @PatchMapping("/{cod}/stock")
    public Mono<ResponseEntity<Producto>> ajustarStock(@PathVariable Integer cod,
                                                       @RequestParam Integer delta) {
        return service.ajustarStock(cod, delta)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // DELETE /producto/{cod} -> eliminar por código
    @DeleteMapping("/{cod}")
    public Mono<ResponseEntity<Producto>> eliminar(@PathVariable Integer cod) {
        return service.eliminarProducto(cod)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // DELETE /producto/por-nombre/{nombre} -> elimina por nombre (devuelve cantidad)
    @DeleteMapping("/por-nombre/{nombre}")
    public Mono<ResponseEntity<Long>> eliminarPorNombre(@PathVariable String nombre) {
        return service.eliminarPorNombre(nombre).map(ResponseEntity::ok);
    }

    // DELETE /producto/por-precio?max=100.0 -> elimina con precio < max (devuelve cantidad)
    @DeleteMapping("/por-precio")
    public Mono<ResponseEntity<Long>> eliminarPorPrecio(@RequestParam("max") Double max) {
        return service.eliminarPorPrecioMenorQue(max).map(ResponseEntity::ok);
    }
}
