package uz.inha.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.inha.entity.Attachment;
import uz.inha.entity.AttachmentContent;
import uz.inha.entity.Category;
import uz.inha.entity.Product;
import uz.inha.repo.AttachmentContentRepo;
import uz.inha.repo.AttachmentRepo;
import uz.inha.repo.CategoryRepo;
import uz.inha.repo.ProductRepo;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("product")
@RequiredArgsConstructor
public class ProductController {
    private final CategoryRepo categoryRepo;
    private final AttachmentRepo attachmentRepo;
    private final AttachmentContentRepo attachmentContentRepo;
    private final ProductRepo productRepo;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("add")
    public String add(Model model) {
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);
        return "admin/addProduct";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SneakyThrows
    @PostMapping("add")
    public String addPost(@RequestParam(name = "file") MultipartFile file, @RequestParam("name") String name,
                          @RequestParam("categoryId") UUID catId, @RequestParam("price") String price) {
        Attachment attachment = Attachment.builder()
                .type(file.getContentType())
                .build();
        UUID attachmentId = attachmentRepo.sava(attachment);
        AttachmentContent attachmentContent = AttachmentContent.builder()
                .attachmentId(attachmentId)
                .content(file.getBytes())
                .build();
        attachmentContentRepo.save(attachmentContent);
        Product product = Product.builder()
                .name(name)
                .categoryId(catId)
                .photoId(attachmentId)
                .price(Double.parseDouble(price))
                .build();
        productRepo.save(product);
        return "redirect:/admin/product";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") UUID id, Model model) {
        productRepo.findById(id);
        Product product = productRepo.findById(id);
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "/admin/editProduct";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SneakyThrows
    @PostMapping("/edit")
    public String editPost(@RequestParam("id") UUID id, @RequestParam(name = "file", required = false) MultipartFile file, @RequestParam("name") String name,
                           @RequestParam("categoryId") UUID catId, @RequestParam("price") String price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(Double.parseDouble(price));
        product.setCategoryId(catId);
        UUID removalPhotoId = null;
        if (!file.isEmpty()) {
            Attachment attachment = Attachment.builder()
                    .type(file.getContentType())
                    .build();
            UUID attachmentId = attachmentRepo.sava(attachment);
            AttachmentContent attachmentContent = AttachmentContent.builder()
                    .attachmentId(attachmentId)
                    .content(file.getBytes())
                    .build();
            attachmentContentRepo.save(attachmentContent);
            removalPhotoId = product.getPhotoId();
            product.setPhotoId(attachmentId);
        } else {
            product.setPhotoId(productRepo.findById(id).getPhotoId());
            System.out.println("PhotoId: " + product.getPhotoId());
        }
        productRepo.edit(id, product);
        if (removalPhotoId != null) {
            attachmentRepo.remove(removalPhotoId);
        }
        return "redirect:/admin/product";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") UUID id) {
        productRepo.deleteById(id);
        return "redirect:/admin/product";
    }
}
