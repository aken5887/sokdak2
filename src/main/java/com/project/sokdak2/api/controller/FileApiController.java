package com.project.sokdak2.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * author         : choi
 * date           : 2024-03-27
 */
@RestController
@RequestMapping("/tui-editor")
public class FileApiController {

    @Value("${file.upload.dir}")
    private String uploadDir;

    /**
     * 에디터 이미지 업로드
     * @param image
     * @return
     */
    @PostMapping("/image-upload")
    public String uploadEditorImage(@RequestParam final MultipartFile image) {
        if(image.isEmpty()) return "";

        String originalFileName = image.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String saveFileName = uuid+"."+ ext;
        String fileFullPath = Paths.get(uploadDir+"/toast-ui", saveFileName).toString();

        File dir = new File(uploadDir+"/toast-ui");
        if(!dir.exists()){
            dir.mkdirs();
        }

        try {
            File uploadFile = new File(fileFullPath);
            image.transferTo(uploadFile);
            return saveFileName;
        } catch (IOException e) {
            return "error";
        }
    }
    /**
     * 디스크에 업로드된 파일을 byte[]로 전환
     */
    @GetMapping(value="/image-print", produces={MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] printEditorImage(@RequestParam final String filename){
        String fileFullPath = Paths.get(uploadDir+"/toast-ui", filename).toString();

        File uploadFile = new File(fileFullPath);
        if(!uploadFile.exists()) throw new RuntimeException();

        try{
            byte[] imageBytes = Files.readAllBytes(uploadFile.toPath());
            return imageBytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
