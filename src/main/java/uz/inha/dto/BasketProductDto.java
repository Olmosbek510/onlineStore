package uz.inha.dto;

import java.util.UUID;

public record BasketProductDto(UUID id, int amount, String product, String category, double productPrice, String base64Photo) {
}
