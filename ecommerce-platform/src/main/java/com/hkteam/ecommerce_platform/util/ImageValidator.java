package com.hkteam.ecommerce_platform.util;

import com.hkteam.ecommerce_platform.exception.AppException;
import com.hkteam.ecommerce_platform.exception.ErrorCode;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class ImageValidator {
    private static final long MAX_FILE_SIZE = 1000L * 1024;
    private static final List<String> ACCEPTED_FILE_TYPES = Arrays.asList("jpg", "png", "jpeg", "gif");

    private ImageValidator() {
        throw new IllegalStateException("This is a utility class and cannot be instantiated");
    }

    public static void validateImage(MultipartFile image) {
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new AppException(ErrorCode.FILE_LIMIT_OF_1MB);
        }

        String extension = getFileExtension(image.getOriginalFilename());
        if (!ACCEPTED_FILE_TYPES.contains(extension)) {
            throw new AppException(ErrorCode.ACCEPTED_FILE_TYPES);
        }

        if (image.isEmpty()) {
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }
    }

    private static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
}
