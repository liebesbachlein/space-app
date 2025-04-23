package app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "account")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="AccountSeq")
    @SequenceGenerator(name="AccountSeq", sequenceName="account_seq_id", allocationSize=50)
    Long id;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    BigDecimal balance;

    @Column(nullable = false)
    String cvc;
}
