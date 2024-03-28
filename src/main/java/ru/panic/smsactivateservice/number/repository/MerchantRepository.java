package ru.panic.smsactivateservice.number.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.panic.smsactivateservice.number.model.Merchant;
import ru.panic.smsactivateservice.number.model.Number;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    boolean existsByApiKey(String apiKey);
    Optional<Merchant> findByApiKey(String apiKey);


    @Query("SELECT m.numbers FROM merchants_table m WHERE m.apiKey = :apiKey")
    Optional<List<Number>> findAllNumberByApiKeyWithFilter(@Param("apiKey") String apiKey);
}
