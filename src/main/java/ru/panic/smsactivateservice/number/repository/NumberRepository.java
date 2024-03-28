package ru.panic.smsactivateservice.number.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.panic.smsactivateservice.number.model.Merchant;
import ru.panic.smsactivateservice.number.model.Number;

import java.util.List;
import java.util.Optional;

@Repository
public interface NumberRepository extends JpaRepository<Number, Long> {
    @Query("SELECT n.isLastSeenUpdated FROM numbers_table n WHERE n.id = :id")
    Boolean findIsLastSeenUpdatedById(@Param("id") long id);

    @Query("UPDATE numbers_table SET isLastSeenUpdated = :isLastSeenUpdated WHERE id = :id")
    @Modifying
    void updateIsLastSeenUpdatedById(@Param("isLastSeenUpdated") boolean isLastSeenUpdated,
                                     @Param("id") long id);

    @Query("SELECT n FROM numbers_table n WHERE n.merchant = :merchant AND n.status = :firstStatus OR " +
            "n.merchant = :merchant AND n.status = :secondStatus ORDER BY n.activationTime DESC")
    Optional<List<Number>> findAllByMerchantAndStatusOrMerchantAndStatusOrderByActivationTimeDesc(
            @Param("firstStatus") String firstStatus,
            @Param("secondStatus") String secondStatus,
            @Param("merchant") Merchant merchant);
}
