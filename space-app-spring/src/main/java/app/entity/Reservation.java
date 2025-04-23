package app.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "reservation")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Reservation {
    @Id
    String id;

    // Owning side
    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    User owner;

    // Owning side
    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = false)
    Space space;

    @Column(nullable = false)
    LocalDate date;

    @Column(nullable = false)
    LocalTime hour;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;
}
