package com.example.demo.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.rec_repository;
import com.example.demo.models.rectanglemod;

@Controller
public class RectangleController {
    @Autowired
    private rec_repository rectangleService;

    @GetMapping("/rectangle")
    public String getAllRectangles(Model model) {
        System.out.println("Loading all rectangles");
        model.addAttribute("rectangles", rectangleService.findAll());
        return "rectangles/rectangle"; // Ensure this matches the actual path and file name
    }

    @GetMapping("/rectangle/{id}")
    public String getRectangleById(@PathVariable int id, Model model) {
        Optional<rectanglemod> rectangle = rectangleService.findById(id);
        if (rectangle.isPresent()) {
            model.addAttribute("rectangle", rectangle.get());
            return "rectangles/detail";
        } else {
            return "error";
        }
    }

    @GetMapping("/rectangle/new")
    public String createRectangleForm(Model model) {
        model.addAttribute("rectangle", new rectanglemod());
        return "rectangles/add_rec";
    }

    @PostMapping("/rectangle/add")
    public String saveRectangle(@ModelAttribute rectanglemod rectangle) {
        rectangleService.save(rectangle);
        return "redirect:/rectangle";
    }

    @GetMapping("/rectangle/edit/{id}")
    public String editRectangleForm(@PathVariable int id, Model model) {
        Optional<rectanglemod> rectangle = rectangleService.findById(id);
        if (rectangle.isPresent()) {
            model.addAttribute("rectangle", rectangle.get());
            return "edit-rectangle";
        } else {
            return "error";
        }
    }

    @PostMapping("/rectangle/update/{id}")
    public String updateRectangle(@PathVariable int id, @RequestParam Map<String, String> params) {
        System.out.println("Updating rectangle with ID: " + id);
        params.forEach((key, value) -> System.out.println(key + ": " + value));

        Optional<rectanglemod> optionalRectangle = rectangleService.findById(id);
        if (optionalRectangle.isPresent()) {
            rectanglemod rectangle = optionalRectangle.get();
            params.forEach((key, value) -> {
                switch (key) {
                case "name":
                    rectangle.setName(value);
                    break;
                case "width":
                    rectangle.setWidth(Integer.parseInt(value));
                    break;
                case "height":
                    rectangle.setHeight(Integer.parseInt(value));
                    break;
                case "color":
                    rectangle.setColor(value);
                    break;
                }
            });
            rectangleService.save(rectangle);
        }
        return "redirect:/rectangle";
    }

    @GetMapping("/rectangle/delete/{id}")
    public String deleteRectangle(@PathVariable int id) {
        rectangleService.deleteById(id);
        return "redirect:/rectangle";
    }
}
