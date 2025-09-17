package com.example.ecs_cicd.controller;

import com.example.ecs_cicd.dto.PhotoUploadRequest;
import com.example.ecs_cicd.model.Photo;
import com.example.ecs_cicd.repository.PhotoRepository;
import com.example.ecs_cicd.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;
    private final PhotoRepository photoRepository;

    @GetMapping("/")
    public String gallery(Model model) {
        List<Photo> images = photoRepository.findAll();
        model.addAttribute("images", images);
        return "gallery";
    }

    @PostMapping("/upload")
    public String uploadPhoto(@RequestParam("file") MultipartFile file,
                              @RequestParam("description") String description) throws Exception {
        PhotoUploadRequest request = new PhotoUploadRequest();
        request.setDescription(description);
        photoService.uploadPhoto(file, request);
        return "redirect:/";
    }
}
