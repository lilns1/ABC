package org.example.abc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CorsProperties corsProperties;

    @Autowired
    public WebConfig(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> origins = corsProperties.getAllowedOrigins();
        boolean allowCredentials = corsProperties.isAllowCredentials();

        var mapping = registry.addMapping("/**");

        if (origins == null || origins.isEmpty()) {
            // No origins configured -> default to no CORS
            return;
        }

        // If configured origins contain a literal "*" pattern and credentials are allowed,
        // use allowedOriginPatterns to avoid IllegalArgumentException.
        boolean containsWildcard = origins.stream().anyMatch(s -> s.equals("*") || s.contains("*"));

        if (containsWildcard) {
            mapping.allowedOriginPatterns(origins.toArray(new String[0]));
        } else {
            mapping.allowedOrigins(origins.toArray(new String[0]));
        }

        mapping.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(allowCredentials)
                .maxAge(3600);
    }
}
