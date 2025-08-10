package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Pedido {
	private int codProducto;
	private String nombre;
	private int unidades;
	private String direccion;
}
