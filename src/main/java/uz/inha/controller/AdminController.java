package uz.inha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.inha.dto.ProductDto;
import uz.inha.entity.Category;
import uz.inha.repo.CategoryRepo;
import uz.inha.repo.ProductRepo;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {
    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminPage() {
        return "admin/admin";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/category")
    public String category(Model model) {
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);
        return "admin/category";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/product")
    public String product(Model model) {
        List<ProductDto> products = productRepo.findAll();
        System.out.println(products);
        model.addAttribute("products", products);
        return "admin/product";
    }

}
