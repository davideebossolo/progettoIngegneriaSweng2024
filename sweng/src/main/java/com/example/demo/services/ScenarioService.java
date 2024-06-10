package com.example.demo.services;

import com.example.demo.model.Choice;
import com.example.demo.model.Scenario;
import com.example.demo.repository.ScenarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ScenarioService {

    @Autowired
    private ScenarioRepository scenarioRepository;

    public List<Scenario> findAll() {
        return scenarioRepository.findAll();
    }

    public Optional<Scenario> findById(Long id) {
        return scenarioRepository.findById(id);
    }

    public Scenario save(Scenario scenario) {
        return scenarioRepository.save(scenario);
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Scenario> scenario = scenarioRepository.findById(id);
        if (scenario.isPresent()) {
            // Rimuovi tutte le scelte associate
            for (Choice choice : scenario.get().getChoices()) {
                choice.setNextScenario(null);
            }
            scenarioRepository.deleteById(id);
        }
    }
}
