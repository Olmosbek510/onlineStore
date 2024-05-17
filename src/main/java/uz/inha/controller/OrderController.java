package uz.inha.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.inha.dto.BasketProductDto;
import uz.inha.entity.Basket;
import uz.inha.entity.Order;
import uz.inha.repo.OrderRepo;
import uz.inha.service.OrderService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderRepo orderRepo;

    @GetMapping("make")
    public String make(HttpSession session, Principal principal) {
        Object o = session.getAttribute("basket");
        System.out.println("Order made successfully");
        if (o != null) {
            if (principal != null) {
                orderService.makeOrder((Basket) o, principal.getName());
                session.removeAttribute("basket");
            } else {
                return "redirect:/login";
            }
        }
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("{id}")
    public String get(@PathVariable(name = "id") UUID userId, Model model) {
        List<Order> orders = new ArrayList<>();
        if(userId!=null){
            orders = orderRepo.findByUser((UUID) userId);
        }
        model.addAttribute("orders", orders);
        return "order";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping()
    public String get(Model model){
        List<Order> orders = orderRepo.findAll();
        model.addAttribute("orders", orders);
        return "order";
    }

    @GetMapping("/get/{id}")
    public String getOrderDetails(@PathVariable(name = "id") UUID id, Model model){
        List<BasketProductDto> products = orderRepo.getProducts(id);
        Double totalPrice = orderRepo.getTotalPrice(id);
        Order order = orderRepo.findById(id);
        model.addAttribute("products", products);
        model.addAttribute("order", order);
        model.addAttribute("totalPrice", totalPrice);
        return "orderDetail";
    }
}
