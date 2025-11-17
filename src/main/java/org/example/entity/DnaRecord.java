package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
//definimos
@Table ( name = "DNA_records", indexes = {
        @Index(name = "index_dna_hash", columnList = "dna_hash", unique = true),
        @Index(name = "index_dna_isMutant", columnList = "is_mutant")
})
public class DnaRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dna_hash", unique = true, nullable = false, length = 64)
    private String dnaHash;

    @Column(name = "is_mutant", nullable = false)
    private boolean isMutant;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
