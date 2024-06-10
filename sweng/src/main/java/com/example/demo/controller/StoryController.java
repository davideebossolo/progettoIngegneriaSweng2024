package com.example.demo.controller;

import com.example.demo.model.Choice;
import com.example.demo.model.Inventory;
import com.example.demo.model.Scenario;
import com.example.demo.model.Story;
import com.example.demo.services.InventoryService;
import com.example.demo.services.ScenarioService;
import com.example.demo.services.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/stories")
public class StoryController {

    @Autowired
    private StoryService storyService;

    @Autowired
    private ScenarioService scenarioService;

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public String listStories(Model model) {
        model.addAttribute("stories", storyService.findAll());
        return "story_list";
    }

    @GetMapping("/new")
    public String showNewStoryForm(Model model) {
        model.addAttribute("story", new Story());
        model.addAttribute("scenario", new Scenario());
        return "story_form";
    }

    @PostMapping
    public String saveStory(@ModelAttribute("story") Story story, @ModelAttribute("scenario") Scenario scenario) {
        scenarioService.save(scenario);
        story.setStartScenario(scenario);

        // Creazione di un nuovo inventario associato alla storia
        Inventory inventory = new Inventory("Inventory for " + story.getTitle(), 0);
        inventoryService.save(inventory);
        story.setInventory(inventory);

        storyService.save(story);
        return "redirect:/stories";
    }

    @GetMapping("/edit/{id}")
    public String showEditStoryForm(@PathVariable("id") Long id, Model model) {
        Optional<Story> story = storyService.findById(id);
        if (story.isPresent()) {
            model.addAttribute("story", story.get());
            model.addAttribute("scenario", story.get().getStartScenario());
            return "story_form";
        } else {
            return "redirect:/stories";
        }
    }

    @PostMapping("/update/{id}")
    public String updateStory(@PathVariable("id") Long id, @ModelAttribute("story") Story story, @ModelAttribute("scenario") Scenario scenario) {
        scenario.setId(story.getStartScenario().getId());
        scenarioService.save(scenario);
        story.setStartScenario(scenario);
        story.setId(id);
        storyService.save(story);
        return "redirect:/stories";
    }

    @GetMapping("/delete/{id}")
    public String deleteStory(@PathVariable("id") Long id) {
        Optional<Story> story = storyService.findById(id);
        if (story.isPresent()) {
            Story foundStory = story.get();
            if (foundStory.getStartScenario() != null) {
                scenarioService.deleteById(foundStory.getStartScenario().getId());
            }
            if (foundStory.getInventory() != null) {
                inventoryService.deleteById(foundStory.getInventory().getId());
            }
            storyService.deleteById(id);
        }
        return "redirect:/stories";
    }

    @GetMapping("/view/{id}")
    public String viewStory(@PathVariable("id") Long id, Model model) {
        Optional<Story> story = storyService.findById(id);
        if (story.isPresent()) {
            Story foundStory = story.get();
            Scenario rootScenario = foundStory.getStartScenario();
            List<Scenario> scenarios = new ArrayList<>();
            gatherScenarios(rootScenario, scenarios);
            model.addAttribute("story", foundStory);
            model.addAttribute("rootScenario", rootScenario);
            model.addAttribute("scenarios", scenarios);
            return "story_view";
        } else {
            return "redirect:/stories";
        }
    }

    private void gatherScenarios(Scenario scenario, List<Scenario> scenarios) {
        if (scenario != null && !scenarios.contains(scenario)) {
            scenarios.add(scenario);
            for (Choice choice : scenario.getChoices()) {
                gatherScenarios(choice.getNextScenario(), scenarios);
            }
        }
    }
}
