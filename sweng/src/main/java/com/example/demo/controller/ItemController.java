package com.example.demo.controller;

import com.example.demo.model.Inventory;
import com.example.demo.model.Item;
import com.example.demo.services.InventoryService;
import com.example.demo.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public String listItems(Model model) {
        List<Item> items = itemService.findAll();
        model.addAttribute("items", items);
        return "item_list";
    }

    @GetMapping("/new/{inventoryId}")
    public String showNewItemForm(@PathVariable("inventoryId") Long inventoryId, Model model) {
        Optional<Inventory> inventory = inventoryService.findById(inventoryId);
        if (inventory.isPresent()) {
            Item item = new Item();
            item.setInventory(inventory.get());
            model.addAttribute("item", item);
            model.addAttribute("inventoryId", inventoryId);
            return "item_form";
        } else {
            return "redirect:/inventories";
        }
    }

    @PostMapping("/save")
    public String saveItem(@ModelAttribute("item") Item item) {
        itemService.save(item);
        return "redirect:/inventories";
    }
}


