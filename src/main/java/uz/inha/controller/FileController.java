package uz.inha.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.inha.entity.AttachmentContent;
import uz.inha.repo.AttachmentContentRepo;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final AttachmentContentRepo attachmentContentRepo;

    @GetMapping("/get/{id}")
    public synchronized void get(@PathVariable(name = "id") UUID id, HttpServletResponse response) {
        AttachmentContent attachmentContent = attachmentContentRepo.findByAttachmentId(id);
        System.out.println(attachmentContent.getAttachmentId());
        try {
            response.getOutputStream().write(attachmentContent.getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
