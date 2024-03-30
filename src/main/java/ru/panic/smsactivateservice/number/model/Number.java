package ru.panic.smsactivateservice.number.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.panic.smsactivateservice.number.model.type.NumberOperator;

import java.util.Date;
import java.util.List;

@Entity(name = "numbers_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Number {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;

    @Column(name = "activation_cost", nullable = false)
    private String activationCost;

    @Column(name = "can_get_another_sms", nullable = false)
    private String canGetAnotherSms;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Column(name = "activation_operator", nullable = false)
    private NumberOperator activationOperator;

    @Column(name = "activation_time", nullable = false)
    private Date activationTime;

    @Column(name = "last_seen_updated", nullable = false)
    private Boolean isLastSeenUpdated;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "number_id")
    private List<Sms> smsList;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Merchant merchant;
}
