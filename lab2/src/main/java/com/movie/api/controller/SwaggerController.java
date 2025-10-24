package com.movie.api.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
public class SwaggerController {

    @GetMapping(value = "/swagger-ui.html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> serveSwaggerUI() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/swagger-ui.html");
        if (resource.exists()) {
            try (InputStream inputStream = resource.getInputStream()) {
                String content = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
                return ResponseEntity.ok(content);
            }
        } else {
            // If not found in classpath, serve the one from project root
            return ResponseEntity.ok("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>API Documentation</title>\n" +
                    "    <meta charset=\"utf-8\"/>\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <h1>Movie Management and Oscar Services API</h1>\n" +
                    "    <p>API documentation is available at the following endpoints:</p>\n" +
                    "    <ul>\n" +
                    "        <li><a href=\"/v3/api-docs\">API Docs (JSON)</a></li>\n" +
                    "        <li><a href=\"/v3/api-docs.yaml\">API Docs (YAML)</a></li>\n" +
                    "    </ul>\n" +
                    "    <p>You can also use tools like Postman or curl to interact with the API directly.</p>\n" +
                    "    <h2>Available Endpoints</h2>\n" +
                    "    <h3>Movie Management Service</h3>\n" +
                    "    <ul>\n" +
                    "        <li>GET /movies - Get all movies with filtering, sorting, and pagination</li>\n" +
                    "        <li>POST /movies - Create a new movie</li>\n" +
                    "        <li>GET /movies/{id} - Get movie by ID</li>\n" +
                    "        <li>PUT /movies/{id} - Update movie by ID</li>\n" +
                    "        <li>DELETE /movies/{id} - Delete movie by ID</li>\n" +
                    "        <li>GET /movies/average-length - Calculate average movie length</li>\n" +
                    "        <li>GET /movies/count-by-operator - Count movies with operator greater than specified</li>\n" +
                    "        <li>GET /movies/filter-by-operator - Get movies with operator greater than specified</li>\n" +
                    "    </ul>\n" +
                    "    <h3>Oscar Service</h3>\n" +
                    "    <ul>\n" +
                    "        <li>GET /oscar/screenwriters/get-loosers - Get screenwriters with no Oscar wins</li>\n" +
                    "        <li>PATCH /oscar/movies/honor-by-length/{min-length}/oscars-to-add - Add Oscars to movies by length</li>\n" +
                    "    </ul>\n" +
                    "</body>\n" +
                    "</html>");
        }
    }
}