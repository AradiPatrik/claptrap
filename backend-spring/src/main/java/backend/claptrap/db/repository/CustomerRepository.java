package emilfekete.claptrap.backend.db.repository;

import emilfekete.claptrap.backend.db.entity.Customer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {

  @Query("SELECT * FROM customer WHERE last_name = :lastname")
  Flux<Customer> findByLastName(String lastName);

}
