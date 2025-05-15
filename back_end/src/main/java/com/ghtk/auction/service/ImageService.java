package com.ghtk.auction.service;

import com.ghtk.auction.dto.request.uploadImage.UploadImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    String uploadImage(String name, MultipartFile file) throws IOException;
    List<String> uploadListImages(List<MultipartFile> files);

    String normalizeImageUrls(String iamges);

    String restoreImageUrls(String images);
}
