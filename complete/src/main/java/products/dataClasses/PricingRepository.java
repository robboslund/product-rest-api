package products.dataClasses;

import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingRepository extends CrudRepository<Pricing, String> {}
