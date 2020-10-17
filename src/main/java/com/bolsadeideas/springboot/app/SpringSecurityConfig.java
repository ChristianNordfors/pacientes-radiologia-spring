package com.bolsadeideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccessHandler;

// Se habilita seguridad con anotaciones // prePostEnabled es para preAuthorize
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled=true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private LoginSuccessHandler successHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
		
		// Comentadas para poder hacer las validaciones con anotaciones en controllers
//		.antMatchers("/ver/**").hasAnyRole("USER")
//		.antMatchers("/uploads/**").hasAnyRole("USER")
//		.antMatchers("/form/**").hasAnyRole("ADMIN")
//		.antMatchers("/eliminar/**").hasAnyRole("ADMIN")
//		.antMatchers("/factura/**").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		.and()
		    .formLogin()
		    	.successHandler(successHandler)
		    	.loginPage("/login")
		    .permitAll()
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403");
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// AuthenticationManagerBuilder para registrar nuestros usuarios
	// @Autowired en el metodo para poder inyectar el objeto builder
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception{
		
		PasswordEncoder encoder = passwordEncoder();
		
		// Por cada usuario que registremos automaticamente se genera un evento que va a encriptar la contraseña,
		// cada vez que se invoque el metodo password para generar la contraseña del usuario
		// UserBuilder users = User.builder().passwordEncoder(password -> encoder.encode(password));
		
		// Otra forma de hacerlo con caracteristica de java 8
		// :: llamada estatica: automaticamente lo que hace es obtiene el argumento de expresion lambda 
		// en encoder y se lo pasa al metodo encode por lo bajo, de forma implicita
		UserBuilder users = User.builder().passwordEncoder(encoder::encode);
		
		// Creamos usuarios en memoria
		builder.inMemoryAuthentication()
		.withUser(users.username("admin").password("12345").roles("ADMIN", "USER"))
		.withUser(users.username("christian").password("12345").roles("USER"));
		
	}

}
