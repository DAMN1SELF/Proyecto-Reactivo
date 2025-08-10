package inicio;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfig {
	private AuthManager authenticationManager;
	private SecurityContextRepository securityContextRepository;
	
	public SecurityConfig(AuthManager authenticationManager, SecurityContextRepository securityContextRepository) {
		this.authenticationManager = authenticationManager;
		this.securityContextRepository = securityContextRepository;
	}
	@Bean
	public MapReactiveUserDetailsService users() throws Exception{
		List<UserDetails> users=List.of(
				User.withUsername("user1")
				.password("user1")
				.roles("USERS")
				.build(),
				User.withUsername("admin")
				.password("admin")
				.roles("USERS","ADMIN")
				.build(),
				User.withUsername("user2")
				.password("user2")
				.roles("OPERATOR")
				.build()
				);
		return new MapReactiveUserDetailsService(users);
	}
	@Bean
	public SecurityWebFilterChain filter(ServerHttpSecurity http) throws Exception{
		return http.csrf(c->c.disable())
		.authenticationManager(authenticationManager)
        .securityContextRepository(securityContextRepository)
		.authorizeExchange(auth->
					auth.pathMatchers(HttpMethod.POST, "/alta").hasAnyRole("ADMIN")
					.pathMatchers(HttpMethod.DELETE,"/eliminar/**").hasAnyRole("ADMIN","OPERATOR")
					.pathMatchers("/productos/**").authenticated()
					.anyExchange().permitAll()
				)
		.build();
	}
	
}
