package com.example.demo.controller;

import com.example.demo.model.Choice;
import com.example.demo.model.Scenario;
import com.example.demo.services.ScenarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/scenarios")
public class ScenarioController {

    @Autowired
    private ScenarioService scenarioService;

    @GetMapping
    public String listScenarios(Model model) {
        List<Scenario> scenarios = scenarioService.findAll();
        model.addAttribute("scenarios", scenarios);
        return "scenario_list";
    }

    @GetMapping("/new")
    public String showNewScenarioForm(Model model) {
        model.addAttribute("scenario", new Scenario());
        return "scenario_form";
    }

    @PostMapping
    public String saveScenario(@ModelAttribute("scenario") Scenario scenario) {
        scenarioService.save(scenario);
        return "redirect:/scenarios";
    }

    @GetMapping("/edit/{id}")
    public String showEditScenarioForm(@PathVariable("id") Long id, Model model) {
        Optional<Scenario> scenario = scenarioService.findById(id);
        if (scenario.isPresent()) {
            model.addAttribute("scenario", scenario.get());
            return "scenario_form";
        } else {
            return "redirect:/scenarios";
        }
    }

    @PostMapping("/update/{id}")
    public String updateScenario(@PathVariable("id") Long id, @ModelAttribute("scenario") Scenario scenario) {
        scenario.setId(id);
        scenarioService.save(scenario);
        return "redirect:/scenarios";
    }

    @GetMapping("/delete/{id}")
    public String deleteScenario(@PathVariable("id") Long id) {
        scenarioService.deleteById(id);
        return "redirect:/scenarios";
    }

    @GetMapping("/view/{id}")
    public String viewScenario(@PathVariable("id") Long id, Model model) {
        Optional<Scenario> scenario = scenarioService.findById(id);
        if (scenario.isPresent()) {
            model.addAttribute("scenario", scenario.get());
            return "scenario_view";
        } else {
            return "redirect:/scenarios";
        }
    }

    @GetMapping("/{id}/addChoice")
    public String showAddChoiceForm(@PathVariable("id") Long id, Model model) {
        Optional<Scenario> scenario = scenarioService.findById(id);
        if (scenario.isPresent()) {
            Choice choice = new Choice();
            choice.setNextScenario(new Scenario());
            model.addAttribute("scenario", scenario.get());
            model.addAttribute("choice", choice);
            return "choice_form";
        } else {
            return "redirect:/scenarios";
        }
    }
    @PostMapping("/{id}/addChoice")
    public String addChoice(@PathVariable("id") Long id, @ModelAttribute("choice") Choice choice) {
        Optional<Scenario> scenarioOpt = scenarioService.findById(id);
        if (scenarioOpt.isPresent()) {
            Scenario scenario = scenarioOpt.get();
            // Save the nextScenario first
            Scenario nextScenario = choice.getNextScenario();
            scenarioService.save(nextScenario);
            // Now save the choice with the saved nextScenario
            choice.setNextScenario(nextScenario);
            scenario.addChoice(choice);
            scenarioService.save(scenario);
        }
        return "redirect:/scenarios/view/" + id;
    }
}
