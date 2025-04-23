package app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "space")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Space {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SpaceSeq")
    @SequenceGenerator(name="SpaceSeq", sequenceName="space_seq_id", allocationSize=50)
    Integer id;

    @Column(unique = true, nullable = false)
    String name;

    @Column(nullable = false)
    BigDecimal price;

    // Owning side
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="space_type_id", nullable = false)
    SpaceType type;
}



