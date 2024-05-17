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
public class Product {
    private UUID id;
    private String name;
    private double price;
    private UUID categoryId;
    private UUID photoId;

    private Boolean isAdded = false;
    private String base64Photo;
}
