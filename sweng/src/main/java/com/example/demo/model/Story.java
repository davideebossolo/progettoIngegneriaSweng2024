package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "story")
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Scenario startScenario;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Inventory inventory;

    public Story() {}

    public Story(String title, Scenario startScenario, Inventory inventory) {
        this.title = title;
        this.startScenario = startScenario;
        this.inventory = inventory;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Scenario getStartScenario() {
        return startScenario;
    }

    public void setStartScenario(Scenario startScenario) {
        this.startScenario = startScenario;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
