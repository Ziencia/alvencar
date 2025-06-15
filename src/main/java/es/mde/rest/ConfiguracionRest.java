package es.mde.rest;

import java.net.URI;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.mde.entidades.*;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.RepositorySearchesResource;

@Configuration
public class ConfiguracionRest {

    @Bean
    public RepresentationModelProcessor<?> addSearchLinks(RepositoryRestConfiguration config) {
        Map<Class<?>, Class<?>> controllersRegistrados = Map.of(
                TransaccionConId.class, TransaccionController.class,
                VehiculoConId.class, VehiculoController.class,
                ClienteConId.class, ClienteConId.class,
                FacturaConId.class, FacturaController.class);

        return resource -> {
            if (resource instanceof RepositorySearchesResource searchResource) {
                var domainType = searchResource.getDomainType();
                var controller = controllersRegistrados.get(domainType);

                if (controller != null) {
                    try {
                        URI uriController = linkTo(controller).toUri();
                        String controllerPath = config.getBasePath() + uriController.getPath();

                        for (var method : controller.getDeclaredMethods()) {
                            if (method.isAnnotationPresent(ResponseBody.class)
                                    && method.isAnnotationPresent(GetMapping.class)) {
                                String pathMetodo = String.join("", method.getAnnotation(GetMapping.class).value());
                                URI fullUri = new URI(
                                        uriController.getScheme(), uriController.getUserInfo(),
                                        uriController.getHost(), uriController.getPort(),
                                        controllerPath + pathMetodo, null, null);

                                String requestParams = Arrays.stream(method.getParameters())
                                        .filter(p -> p.isAnnotationPresent(RequestParam.class))
                                        .map(p -> {
                                            var nombre = p.getAnnotation(RequestParam.class).value();
                                            return nombre.isEmpty() ? p.getName() : nombre;
                                        })
                                        .collect(Collectors.joining(","));

                                Link link = Link.of(
                                        URLDecoder.decode(fullUri.toString(), "UTF-8") + "{?" + requestParams + "}",
                                        method.getName());

                                searchResource.add(link);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return searchResource;
            }
            return resource;
        };
    }

    /**
     * Ver tambien <a href=
     * "https://docs.spring.io/spring-data/rest/docs/current/reference/html/#customizing-sdr.configuring-cors">
     * Configuring CORS</a> para configuracion Data Rest adicional heredada con
     * {@link org.springframework.web.bind.annotation.CrossOrigin}.
     * 
     * @return bean del tipo {@link CorsFilter} permitiendo cualquier solicitud
     */
    @Bean
    CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    /*
     * @Bean
     * public ObjectMapper getObjectMapper() {
     * 
     * ObjectMapper mapper = new ObjectMapper();
     * mapper.addMixIn(Cliente.class, MixIns.Clientes.class);
     * mapper.addMixIn(Libro.class, MixIns.Libros.class);
     * mapper.addMixIn(Cuaderno.class, MixIns.Cuadernos.class);
     * 
     * return mapper;
     * }
     */

}