package emilfekete.claptrap.backend.db;

import emilfekete.ReactiveDemoApplication;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.client.SSLMode;
import io.r2dbc.spi.ConnectionFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@EnableR2dbcRepositories
class DatabaseConfiguration extends AbstractR2dbcConfiguration {

  private static final Logger log = LoggerFactory.getLogger(ReactiveDemoApplication.class);

  @Bean
  public ConnectionFactory connectionFactory() {
    URI dbUri = getDatabaseUri();

    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];
    log.info(dbUri.toASCIIString());

    PostgresqlConnectionFactory factory = new PostgresqlConnectionFactory(
      PostgresqlConnectionConfiguration.builder()
        .host(dbUri.getHost())
        .port(dbUri.getPort())
        .database(dbUri.getPath().substring(1))
        .username(username)
        .password(password)
        .enableSsl()
        .sslMode(SSLMode.REQUIRE)
        .build()
    );

    log.info(factory.toString());

    return factory;
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
