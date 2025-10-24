package com.minacontrol.shared.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;

public class DotenvPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        Dotenv dotenv = Dotenv.load();
        return new DotenvPropertySource("dotenv", dotenv);
    }
}
