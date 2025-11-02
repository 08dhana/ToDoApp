package com.example.ToDoApp.controller;

import com.example.ToDoApp.model.ToDo;
import com.example.ToDoApp.service.ToDoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/todos")
public class ToDoController {

    private final ToDoService service;

    public ToDoController(ToDoService service) {
        this.service = service;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "all") String filter, Model model) {
        model.addAttribute("todos", service.listAll(filter));
        model.addAttribute("filter", filter);
        return "todos/list";
    }

    @GetMapping("/new")
    public String create(Model model) {
        if (!model.containsAttribute("todo")) {
            model.addAttribute("todo", new ToDo());
        }
        model.addAttribute("action", "Create");
        return "todos/form";
    }

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("todo") ToDo todo,
            BindingResult result,
            @RequestParam(defaultValue = "all") String filter,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.todo", result);
            redirectAttributes.addFlashAttribute("todo", todo);
            return "redirect:/todos/new";
        }

        service.save(todo);
        return "redirect:/todos?filter=" + filter;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return service.get(id)
                .map(todo -> {
                    model.addAttribute("todo", todo);
                    model.addAttribute("action", "Update");
                    return "todos/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Todo not found");
                    return "redirect:/todos";
                });
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         @RequestParam(defaultValue = "all") String filter,
                         RedirectAttributes redirectAttributes) {

        service.delete(id);
        return "redirect:/todos?filter=" + filter;
    }

    @PostMapping("/done/{id}")
    public String done(@PathVariable Long id,
                       @RequestParam(defaultValue = "all") String filter,
                       RedirectAttributes redirectAttributes) {

        service.markDone(id);
        return "redirect:/todos?filter=" + filter;
    }
}