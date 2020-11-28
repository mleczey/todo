package com.mleczey.todo.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.AllArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
class OpenApiConfiguration {

  private final BuildProperties buildProperties;

  @Bean
  OpenAPI openApi() {
    return new OpenAPI()
        .info(new Info()
            .title(buildProperties.getName())
            .version(buildProperties.getVersion())
            .description(buildProperties.getArtifact() + ":" + buildProperties.getGroup())
            .license(new License().name("Apache License 2.0").url("https://choosealicense.com/licenses/apache-2.0/")));
  }
}
