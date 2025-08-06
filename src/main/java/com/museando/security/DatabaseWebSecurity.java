package com.museando.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity {

    /*
     * @Bean UserDetailsManager users(DataSource dataSource) {
     * JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource); return
     * users; }
     */
    @Bean
    UserDetailsManager usersCustom(DataSource dataSource) {
		JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
		users.setUsersByUsernameQuery("select username,password,estatus from Usuarios u where username=?");
		users.setAuthoritiesByUsernameQuery(
						"select u.username,p.perfil from UsuarioPerfil up " + 
						"inner join Usuarios u on u.id = up.idUsuario "
						+ "inner join Perfiles p on p.id = up.idPerfil "
						+ "where u.username=?");
		return users;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(authorize -> authorize
			    // Recursos estáticos
			    .requestMatchers("/bootstrap/**", "/images/**", "/tinymce/**", "/logos/**","/css/**","/galeria/**","/obras/**").permitAll()

			    // URLs públicas específicas para museos
			    .requestMatchers("/museos/todos/**", "/museos/detalle/**", "/voluntariado").permitAll()

			 // URLs públicas específicas para sala
			    .requestMatchers("/salas/detalle/**", "/obras/detalle/**").permitAll()
			    
			    // Otras URLs públicas
			    .requestMatchers("/", "/bcrypt/*", "/bcrypt/**", "/signup", "/login", "/museos/search", "/acerca").permitAll()

			    .requestMatchers("/mensajes/**").hasAuthority("USUARIO")

			    
			    // URLs protegidas específicas
			    .requestMatchers("/categorias/**").hasAnyAuthority("SUPERVISOR","ADMINISTRADOR")
			    .requestMatchers("/galeria/**").hasAnyAuthority("SUPERVISOR","ADMINISTRADOR")
			    .requestMatchers("/museos/**").hasAnyAuthority("SUPERVISOR","ADMINISTRADOR") // Aquí solo rutas no permitidas arriba
			    .requestMatchers("/obras/**").hasAnyAuthority("SUPERVISOR","ADMINISTRADOR")
			    .requestMatchers("/salas/**").hasAnyAuthority("SUPERVISOR","ADMINISTRADOR")
			    .requestMatchers("/ubicaciones/**").hasAnyAuthority("SUPERVISOR","ADMINISTRADOR")
			    .requestMatchers("/usuarios/**").hasAnyAuthority("ADMINISTRADOR")
			    
			    .requestMatchers("/voluntariado/lista").hasAnyAuthority("ADMINISTRADOR", "SUPERVISOR") // solo admin y supervisor


			    // Cualquier otra URL requiere autenticación
			    .anyRequest().authenticated()
			);

		
		

		// El formulario de Login no requiere autenticacion
		http.formLogin(form -> form
			    .loginPage("/login").permitAll()
			    .failureHandler(new CustomAuthenticationFailureHandler()) // Aquí usas tu clase nueva
			    .successHandler((request, response, authentication) -> {
			        // Si quieres manejar diferentes redirecciones según rol, ponlas aquí
			        // Pero en tu caso rediriges siempre al mismo sitio, así que:
			        response.sendRedirect("/");
			    })
			);


		return http.build();
	}

    @Bean
    PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
	}

}
