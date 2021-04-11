package emilfekete.claptrap.backend.db;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@EnableR2dbcRepositories
class DatabaseConfiguration extends AbstractR2dbcConfiguration {

  @Bean
  public ConnectionFactory connectionFactory() {
    URI dbUri = getDatabaseUri();

    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];

    return new PostgresqlConnectionFactory(
      PostgresqlConnectionConfiguration.builder()
        .host(dbUri.getHost())
        .port(dbUri.getPort())
        .database(dbUri.getPath().substring(1))
        .username(username)
        .password(password)
        .build()
    );
  }

  @NotNull
  private URI getDatabaseUri() {
    URI dbUri = null;
    try {
      dbUri = new URI(System.getenv("DATABASE_URL"));
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    return dbUri;
  }
}
