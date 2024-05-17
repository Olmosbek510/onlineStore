package uz.inha.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.inha.entity.Basket;
import uz.inha.entity.BasketProduct;
import uz.inha.entity.OrderProduct;
import uz.inha.entity.User;
import uz.inha.repo.OrderProductRepo;
import uz.inha.repo.OrderRepo;
import uz.inha.repo.UserRepo;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private final OrderProductRepo orderProductRepo;
    public void makeOrder(Basket basket, String userName){
        User user = userRepo.findByEmail(userName);
        List<BasketProduct> products = basket.getProducts();
        UUID orderId = orderRepo.make(user);
        for (BasketProduct product : products) {
            orderProductRepo.save(
                    OrderProduct.builder()
                            .orderId(orderId)
                            .productId(product.getProduct_id())
                            .amount(product.getAmount())
                            .build()
            );
        }
    }
}
