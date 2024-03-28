package ru.panic.smsactivateservice.number.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "sms_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "received_at", nullable = false)
    private String receivedAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Number number;
}
