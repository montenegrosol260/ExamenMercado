package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MutantService {
    private final DnaRecordRepository dnaRecordRepository;
    private final MutantDetector mutantDetector;
}
