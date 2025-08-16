package com.br.messaging_api.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SwaggerConfigTest {

    @Test
    void openAPI_shouldCreateOpenAPIBean() {
        SwaggerConfig config = new SwaggerConfig();
        OpenAPI openAPI = config.openAPI();

        assertNotNull(openAPI);

        // verifica info
        Info info = openAPI.getInfo();
        assertNotNull(info);
        assertEquals("REST API - Templates", info.getTitle());
        assertEquals("API para envio de email", info.getDescription());
        assertEquals("v1", info.getVersion());

        // verifica license
        License license = info.getLicense();
        assertNotNull(license);
        assertEquals("Apache 2.0", license.getName());
        assertEquals("https://www.apache.org/licenses/LICENSE-2.0", license.getUrl());
    }
}
