package org.example.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter @Setter
@Builder
@NoArgsConstructor
@Entity
@Table ( name = "DNAs_record")
public class DnaRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dna_hash", unique = true, nullable = false)
    private String dnaHash;

    @Column(name = "is_mutant", nullable = false)
    private boolean isMutant;
}
