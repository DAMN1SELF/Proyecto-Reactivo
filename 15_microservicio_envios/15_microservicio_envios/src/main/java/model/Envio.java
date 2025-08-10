package model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(value="envios")
public class Envio {
	@Id
	private int idEnvio;
	@JsonAlias(value="nombre")
	private String producto;
	private LocalDateTime fecha;
	private String direccion;
	private String estado;
}
