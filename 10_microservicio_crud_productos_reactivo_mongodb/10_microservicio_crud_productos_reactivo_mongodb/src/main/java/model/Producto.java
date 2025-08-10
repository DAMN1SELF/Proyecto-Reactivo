package model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "productos")
public class Producto {
	private int id;
	private String nombre;
	private String categoria;
	private double precioUnitario;
	private int stock;
}
