package com.ghtk.auction.component;

import com.cloudinary.Cloudinary;
import com.ghtk.auction.dto.request.uploadImage.UploadImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Getter
@Slf4j
public class CloudinaryComponent {
    private final Cloudinary cloudinary;
    private final ExecutorService executor = Executors.newFixedThreadPool(6);

    public String uploadFile(String name, MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", toSlug(name)))
                .get("url")
                .toString();
    }
    public String uploadFileNew(MultipartFile file) throws IOException {
        return cloudinary.uploader()
                .upload(file.getBytes(),
                        Map.of("public_id", toSlug(file.getOriginalFilename())))
                .get("url")
                .toString();
    }

    public List<String> uploadListFile(List<MultipartFile> files){
        if(files == null){
            return Collections.emptyList();
        }
        List<CompletableFuture<String>> futures = files.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> uploadSingleImageSafe(file),executor))
                .collect(Collectors.toList());

        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    private String uploadSingleImageSafe(MultipartFile file){
        try {
            if (file == null || file.isEmpty()) {
                System.err.println("File rỗng: ");
                return null;
            }
            log.info("Uploading by thread: {} " , Thread.currentThread().getName());

            return uploadFileNew(file);
        } catch (IOException e) {
            System.err.println("Lỗi upload '" + file.getOriginalFilename() + "': " + e.getMessage());
            return null;
        }
    }

    private String toSlug(String name) {
        // Chuyển về chữ thường
        String slug = name.toLowerCase();

        // Xóa các ký tự không phải chữ cái và số, thay thế bằng dấu cách
        slug = slug.replaceAll("[^a-z0-9\\s-]", "");

        // Thay thế các khoảng trắng và dấu gạch ngang bằng dấu gạch ngang
        slug = slug.replaceAll("\\s+", "-");

        // Loại bỏ các dấu gạch ngang thừa
        slug = slug.replaceAll("-+", "-");

        // Loại bỏ dấu gạch ngang ở đầu và cuối
        slug = slug.replaceAll("^-|-$", "");

        return slug;
    }
}
