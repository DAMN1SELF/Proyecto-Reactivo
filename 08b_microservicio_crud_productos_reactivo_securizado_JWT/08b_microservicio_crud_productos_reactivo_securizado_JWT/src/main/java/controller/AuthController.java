package controller;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import model.Credentials;
import reactor.core.publisher.Mono;

@RestController
public class AuthController {
	@Value("${clave}")
	String CLAVE;
	private static final long TIEMPO_VIDA=85_000_000;
	
	MapReactiveUserDetailsService detailsService;

	public AuthController(MapReactiveUserDetailsService detailsService) {
		this.detailsService = detailsService;
	}
	@PostMapping(value="login", consumes = MediaType.APPLICATION_JSON_VALUE,produces=MediaType.TEXT_PLAIN_VALUE)
	public Mono<ResponseEntity<String>> login(@RequestBody Credentials credentials){
		//si el usuario es válido genera un token con su información y se la envía al cliente
				//para que éste la utilice en las llamadas a los recursos
				return detailsService.findByUsername(credentials.getUser())
						.filter(details -> credentials.getPwd().equals(details.getPassword()))
			            .map(details -> new ResponseEntity<>(getToken(details),HttpStatus.OK))
			            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
	}
	//genera el token y lo envía al cliente
		private String getToken(UserDetails details) {
			//en el body del token se incluye el usuario 
			//y los roles a los que pertenece, además
			//de la fecha de caducidad y los datos de la firma
			return Jwts.builder()
					.subject(details.getUsername()) //usuario
					.issuedAt(new Date())
					.claim("authorities",details.getAuthorities().stream() //roles
									.map(GrantedAuthority::getAuthority)
									.collect(Collectors.toList()))
					.expiration(new Date(System.currentTimeMillis() + TIEMPO_VIDA)) //fecha caducidad
					.signWith(Keys.hmacShaKeyFor(CLAVE.getBytes()))//clave y algoritmo para firma
					.compact(); //generación del token
			
		}
}
