package uz.inha.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProduct {
    private UUID id;
    private UUID productId;
    private int amount;
    private UUID orderId;
}
