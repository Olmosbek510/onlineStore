package uz.inha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.inha.entity.Category;
import uz.inha.repo.CategoryRepo;

import java.util.UUID;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryRepo categoryRepo;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("add")
    public String add() {
        return "admin/addCategory";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("add")
    public String addCat(@ModelAttribute Category category) {
        categoryRepo.add(category);
        return "redirect:/admin/category";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("edit/{id}")
    public String edit(@PathVariable(name = "id") UUID id, Model model) {
        Category byId = categoryRepo.findById(id);
        model.addAttribute("category", byId);
        return "/admin/editCategory";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("edit/{id}")
    public String editPost(@PathVariable(name = "id") UUID id, @ModelAttribute Category category) {
        categoryRepo.edit(id, category);
        return "redirect:/admin/category";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("delete/{id}")
    public String delete(@PathVariable(name = "id") UUID uuid){
        categoryRepo.deleteById(uuid);
        return "redirect:/admin/category";
    }
}
