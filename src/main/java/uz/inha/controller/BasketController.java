package uz.inha.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.inha.dto.BasketProductDto;
import uz.inha.entity.Basket;
import uz.inha.entity.BasketProduct;
import uz.inha.repo.BasketRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketRepo basketRepo;

    @GetMapping("/add/{id}")
    public String add(HttpSession session, @PathVariable("id") UUID productId) {
        Object category = session.getAttribute("category");
        Object basketOpt = session.getAttribute("basket");
        Object o = session.getAttribute("basketSize");
        Basket basket;
        if (basketOpt != null) {
            basket = (Basket) basketOpt;
            basket.getProducts().add(new BasketProduct(productId, 1));
        } else {
            basket = new Basket();
            basket.getProducts().add(new BasketProduct(productId, 1));
        }
        session.setAttribute("basket", basket);
        basket.getProducts().forEach(System.out::println);
        System.out.println((Integer) o);
        System.out.println("------");
        return category != null ? "redirect:/?categoryId=" + category : "redirect:/";
    }

    @GetMapping()
    public String basket(HttpSession session, Model model) {
        Object o = session.getAttribute("basket");
        List<BasketProductDto> basketProductDtos = new ArrayList<>();
        double totalPrice = 0;
        if (o != null) {
            basketProductDtos = basketRepo.findAll((Basket) o);
            totalPrice = basketProductDtos.stream().mapToDouble(value -> value.productPrice() * value.amount()).sum();
        }
        model.addAttribute("basketProducts", basketProductDtos);
        model.addAttribute("totalPrice", totalPrice);
        return "basket";
    }

    @GetMapping("increase/{id}")
    public String increase(HttpSession session, @PathVariable("id") UUID id) {
        Object object = session.getAttribute("basket");
        Basket basket;
        if (object != null) {
            basket = (Basket) object;
            BasketProduct basketProduct1 = basket.getProducts().stream().filter(basketProduct -> basketProduct.getProduct_id().equals(id)).findFirst().get();
            basketProduct1.setAmount(basketProduct1.getAmount() + 1);
        }
        return "redirect:/basket";
    }


    @GetMapping("decrease/{id}")
    public String decrease(HttpSession session, @PathVariable("id") UUID id) {
        Object object = session.getAttribute("basket");
        Basket basket;
        if (object != null) {
            basket = (Basket) object;
            BasketProduct basketProduct1 = basket.getProducts().stream().filter(basketProduct -> basketProduct.getProduct_id().equals(id)).findFirst().get();
            if (basketProduct1.getAmount() >= 1) {
                basketProduct1.setAmount(basketProduct1.getAmount() - 1);
                if (basketProduct1.getAmount() == 0) {
                    basket.getProducts().remove(basketProduct1);
                }
            }
        }
        return "redirect:/basket";
    }

    @GetMapping("/clear")
    public String clearBasket(HttpSession session) {
        session.removeAttribute("basket");
        return "redirect:/basket";
    }
}
