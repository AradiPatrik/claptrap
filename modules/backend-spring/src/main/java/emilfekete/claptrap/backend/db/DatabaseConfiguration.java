package emilfekete.claptrap.backend.db;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
class DatabaseConfiguration extends AbstractR2dbcConfiguration {

  @Bean
  public ConnectionFactory connectionFactory() {
    return new PostgresqlConnectionFactory(
      PostgresqlConnectionConfiguration.builder()
        .host("localhost")
        .port(5432)
        .database("claptrap")
        .username("postgres")
        .password("admin")
        .build()
    );
  }
}
