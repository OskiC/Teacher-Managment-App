package org.example.lab5git.service;

import lombok.RequiredArgsConstructor;
import org.example.lab5git.model.Rate;
import org.example.lab5git.repository.RateRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RateService {
    private final RateRepository rateRepository;

    public Rate addRate(Rate rate) {
        return rateRepository.save(rate);
    }

    public List<Rate> getAllRates() {
        return rateRepository.findAll();
    }
}
