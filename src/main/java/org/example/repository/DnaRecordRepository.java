package org.example.repository;

import org.example.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {
    //evitamos nullPointerException con Optional
    Optional<DnaRecord> findByDnaHash(String dnaHash);
}
