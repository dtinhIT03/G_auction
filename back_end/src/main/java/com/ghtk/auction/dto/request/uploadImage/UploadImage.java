package com.ghtk.auction.dto.request.uploadImage;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class UploadImage {
    String name;
    MultipartFile file;
}
