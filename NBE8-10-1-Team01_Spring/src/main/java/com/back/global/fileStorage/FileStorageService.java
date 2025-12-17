package com.back.global.fileStorage;

import com.back.global.exception.file.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException("파일 저장 디렉토리 생성 실패", ex);
        }
    }

    /**
     * 파일을 서버에 저장하고 저장된 파일명을 반환합니다.
     *
     * @param file 업로드할 파일
     * @return 저장된 파일명 (UUID 기반)
     */
    public String storeFile(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new FileStorageException("파일명이 유효하지 않습니다");
        }
        originalFileName = StringUtils.cleanPath(originalFileName);

        try {
            if (originalFileName.contains("..")) {
                throw new FileStorageException("잘못된 파일 경로입니다");
            }

            // 고유한 파일명 생성
            String fileExtension = "";
            int dotIndex = originalFileName.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = originalFileName.substring(dotIndex);
            }
            String uniqueFileName = UUID.randomUUID() + fileExtension;

            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;
        } catch (IOException ex) {
            throw new FileStorageException("파일 저장에 실패했습니다", ex);
        }
    }

    /**
     * 저장된 파일을 로드하여 Path 객체로 반환합니다.
     *
     * @param fileName 로드할 파일명
     * @return 파일의 Path 객체
     */
    public Path loadFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            if (!Files.exists(filePath)) {
                throw new FileStorageException("파일을 찾을 수 없습니다");
            }
            return filePath;
        } catch (Exception ex) {
            throw new FileStorageException("파일 로드에 실패했습니다", ex);
        }
    }
}

