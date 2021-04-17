package emilfekete;

import emilfekete.claptrap.backend.db.entity.Customer;
import emilfekete.claptrap.backend.db.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = InfrastructureConfiguration.class)
public class DatabaseTest {

  @Autowired
  CustomerRepository customerRepo;

  @Test
  public void getCustomerById() {
    Customer testCustomer = new Customer(0L, "Patrik", "Aradi");
    Customer customer = customerRepo
      .findByLastName("Aradi").take(1).blockFirst();

    assertEquals(testCustomer, customer);
  }
}
