package com.ghtk.auction.controller.product;

import com.ghtk.auction.dto.request.uploadImage.UploadImage;
import com.ghtk.auction.dto.response.ApiResponse;
import com.ghtk.auction.service.ImageService;
import com.ghtk.auction.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/images")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ImageController {
    final ImageService imageService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<String>> uploadImages(
            @RequestParam("files") MultipartFile files,
            @RequestParam("name") String name) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(imageService.uploadImage(name, files)));
    }

    @PostMapping("/listImage")
    public ResponseEntity<ApiResponse<List<String>>> uploadlistImages(@RequestParam("files") List<MultipartFile> files) throws IOException{
        List<String> urls = new ArrayList<>();
             urls = imageService.uploadListImages(files);

        return ResponseEntity.ok(ApiResponse.success(urls));
    }
}
