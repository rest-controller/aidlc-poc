package com.example.authapi;

import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** A JSON number, boolean or similar must not be accepted as text: it is a malformed request (BR1.4). */
@Configuration
public class JsonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer strictTextCoercion() {
        return builder -> builder.postConfigurer(mapper -> {
            var textual = mapper.coercionConfigFor(LogicalType.Textual);
            textual.setCoercion(CoercionInputShape.Integer, CoercionAction.Fail);
            textual.setCoercion(CoercionInputShape.Float, CoercionAction.Fail);
            textual.setCoercion(CoercionInputShape.Boolean, CoercionAction.Fail);
        });
    }
}
