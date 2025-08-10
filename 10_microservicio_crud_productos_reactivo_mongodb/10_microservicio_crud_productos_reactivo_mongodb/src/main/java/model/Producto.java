package model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {
    @Id
    private String id;
    @Indexed(unique = true)
    private Integer codProducto;       // único
    private String nombre;
    private String categoria;
    private Double precioUnitario;
    private Integer stock;
}