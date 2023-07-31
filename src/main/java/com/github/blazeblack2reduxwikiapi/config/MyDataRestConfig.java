package com.github.blazeblack2reduxwikiapi.config;

import com.github.blazeblack2reduxwikiapi.model.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {
    private String allowedOrigins = "http://localhost:3000";
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod[] unsupportedActions = {
                HttpMethod.POST,
                HttpMethod.PATCH,
                HttpMethod.DELETE,
                HttpMethod.PUT
        };

        config.exposeIdsFor(Ability.class);
        config.exposeIdsFor(BaseStats.class);
        config.exposeIdsFor(Move.class);
        config.exposeIdsFor(Pokemon.class);
        config.exposeIdsFor(PokemonAbility.class);
        config.exposeIdsFor(PokemonMove.class);
        config.exposeIdsFor(Type.class);
        config.exposeIdsFor(Sprite.class);

        disableHttpMethods(Ability.class, config, unsupportedActions);
        disableHttpMethods(BaseStats.class, config, unsupportedActions);
        disableHttpMethods(Move.class, config, unsupportedActions);
        disableHttpMethods(Pokemon.class, config, unsupportedActions);
        disableHttpMethods(PokemonAbility.class, config, unsupportedActions);
        disableHttpMethods(PokemonMove.class, config, unsupportedActions);
        disableHttpMethods(Type.class, config, unsupportedActions);
        disableHttpMethods(Sprite.class, config, unsupportedActions);

//        Configure cors mapping
        cors.addMapping(config.getBasePath()+"/**")
                .allowedOrigins(allowedOrigins);
    }

    private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config, HttpMethod[] unsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(unsupportedActions)))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedActions));
    }


}
