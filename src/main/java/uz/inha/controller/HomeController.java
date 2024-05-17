package uz.inha.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.inha.entity.Basket;
import uz.inha.entity.BasketProduct;
import uz.inha.entity.Category;
import uz.inha.entity.Product;
import uz.inha.repo.CategoryRepo;
import uz.inha.repo.ProductRepo;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    @GetMapping("/")
    public String get(HttpSession session, @RequestParam(name = "categoryId", required = false) UUID categoryId, @RequestParam(name = "all", required = false) String all, Model model) {
        if(session.getAttribute("category")!=null && categoryId == null){
            categoryId = (UUID) session.getAttribute("category");
        }
        session.setAttribute("category", categoryId);
        List<Category> categories = categoryRepo.findAll();
        List<Product> products = categoryId != null ? productRepo.findByCategory(categoryId) : productRepo.findAllActualProduct();
        if (Objects.equals(all,"true")) {
            session.removeAttribute("category");
            products = productRepo.findAllActualProduct();
        }
        Basket basket = Objects.requireNonNullElse((Basket) session.getAttribute("basket"), new Basket());
        for (Product product : products) {
            Optional<BasketProduct> productOptional = basket.getProducts().stream().filter(item -> item.getProduct_id().equals(product.getId())).findFirst();
            if(productOptional.isPresent()){
                product.setIsAdded(true);
            }
        }
        model.addAttribute("basketSize", basket.getProducts().size());
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "home";
    }
}
