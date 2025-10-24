package com.minacontrol;

import com.minacontrol.shared.config.DotenvPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class })
@OpenAPIDefinition(info = @Info(title = "MinaControl Pro API", version = "1.0", description = "API para la gestión de operaciones mineras"))
@ComponentScan(basePackages = {"com.minacontrol", "com.minacontrol.testsetup"})
@PropertySource(value = "classpath:.env", factory = DotenvPropertySourceFactory.class)
public class MinaControlApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinaControlApiApplication.class, args);
    }

}
