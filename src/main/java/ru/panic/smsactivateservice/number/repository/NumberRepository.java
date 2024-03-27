package ru.panic.smsactivateservice.number.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.panic.smsactivateservice.number.model.Number;

@Repository
public interface NumberRepository extends JpaRepository<Number, Long> {
}
