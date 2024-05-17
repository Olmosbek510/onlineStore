package uz.inha.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uz.inha.dto.BasketProductDto;
import uz.inha.dto.ProductDto;
import uz.inha.entity.Basket;
import uz.inha.entity.BasketProduct;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BasketRepo {
    private final ProductRepo productRepo;
    public List<BasketProductDto> findAll(Basket basket){
        List<BasketProduct> products = basket.getProducts();
        List<ProductDto> productDtos = productRepo.findAll();
        List<BasketProductDto> basketProductDtos = new ArrayList<>();
        for (BasketProduct product : products) {
            basketProductDtos.add(new BasketProductDto(
                    product.getProduct_id(),
                    product.getAmount(),
                    productRepo.findById(product.getProduct_id()).getName(),
                    productDtos.stream().filter(productDto -> productDto.id().equals(product.getProduct_id())).findFirst().get().category(),
                    productRepo.findById(product.getProduct_id()).getPrice(),
                    productDtos.stream().filter(productDto -> productDto.id().equals(product.getProduct_id())).findFirst().get().getPhoto()
                    ));
        }
        return basketProductDtos;
    }
}
