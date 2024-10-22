package com.github.nan.web.demos.web;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author NanNan Wang
 */
@RestController
public class DownloadDemoController {

    @GetMapping("/download/{filename}")
    public ResponseEntity<FileSystemResource> downloadFile(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get("/Users/nanan/Desktop/" + filename); // 替换为实际文件路径
        FileSystemResource file = new FileSystemResource(filePath);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString());
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }


    @GetMapping("/download/byte/{filename}")
    public ResponseEntity<byte[]> downloadFile2(@PathVariable String filename) {
        Supplier<Optional<byte[]>> fileContentSupplier = () -> {
            Path filePath = Paths.get("/Users/nanan/Desktop/" + filename); // 替换为实际文件路径

            if (!Files.exists(filePath)) {
                return Optional.empty();
            }

            try {
                byte[] fileBytes = Files.readAllBytes(filePath);
                return Optional.of(fileBytes);
            } catch (IOException e) {
                // 记录日志或进行其他处理
                System.err.println("Error reading file: " + e.getMessage());
                return Optional.empty();
            }
        };
        // 如果文件内容为空，返回 404 响应
        return fileContentSupplier.get().map(fileBytes -> {
            HttpHeaders headers = new HttpHeaders();
            try {
                String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString());
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename);
            } catch (UnsupportedEncodingException e) {
                System.err.println("Error encoding filename: " + e.getMessage());
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(fileBytes.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileBytes);
        }).orElseGet(() -> ResponseEntity.notFound().build()); // 文件不存在时返回404
    }

}
