package com.minacontrol.shared.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.core.env.PropertySource;

public class DotenvPropertySource extends PropertySource<Dotenv> {

    public DotenvPropertySource(String name, Dotenv source) {
        super(name, source);
    }

    @Override
    public Object getProperty(String name) {
        return source.get(name);
    }
}
