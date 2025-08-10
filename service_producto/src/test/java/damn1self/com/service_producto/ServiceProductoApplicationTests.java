package damn1self.com.service_producto;

import damn1self.com.service_producto.model.Producto;
import damn1self.com.service_producto.service.ProductoService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import javax.sound.sampled.Port;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class ServiceProductoApplicationTests {

    @Autowired
    ProductoService productoService;

    @Test
    @Order(1)
    void testProductoCategoria() {
        StepVerifier.create(productoService.productosCategoria("Tecnologia"))
                .expectNextMatches(p->p.getNombre().equals("Laptop Lenovo"))
                .expectNextMatches(p->p.getNombre().equals("Smartphone Samsung"))
                //.expectNextMatches(p->p.getNombre().equals("Laptop Super"))
                .verifyComplete();
    }

    @Test
    @Order(2)
    void testEliminarProducto() {
        StepVerifier.create(productoService.eliminarProducto(103))
                .expectNextMatches(p->p.getNombre().equals("Mouse Logitech"))
                .verifyComplete();
    }

    @Test
    @Order(4)
    void testAltaProducto() {
        Producto pr= new Producto(250,"ptest","cat1",10,2);
        StepVerifier.create(productoService.altaProducto(pr))
                .expectComplete()
                .verify();
    }

    @Test
    @Order(3)
    void testCatalogo() {
        StepVerifier.create(productoService.catalogo())
                .expectNextCount(4)
                .verifyComplete();
    }
}
