package app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "space_type")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class SpaceType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SpaceTypeSeq")
    @SequenceGenerator(name="SpaceTypeSeq", sequenceName="space_type_seq_id", allocationSize=50)
    Integer id;

    @Column(unique = true, nullable = false)
    String name;
}
