package app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "session")
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String token;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String cvc;

    @Column(nullable = false)
    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    Status status;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    Instant createdAt;

    public enum Status {
        SUCCESS, FAIL, PENDING, CREATED
    }

    public String getStatusName() {
        return status.name();
    }
}
