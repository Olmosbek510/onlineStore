package uz.inha.dto;


import java.util.Base64;
import java.util.UUID;

public record ProductDto(UUID id,
                         String name,
                         double price,
                         String category,
                         UUID categoryId,
                         UUID photoId,
                         byte[] photo) {
    public String getPhoto(){
        return Base64.getEncoder().encodeToString(this.photo);
    }
}
