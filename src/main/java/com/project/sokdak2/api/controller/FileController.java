package com.project.sokdak2.api.controller;

import com.project.sokdak2.api.response.FileResponse;
import com.project.sokdak2.api.service.FileService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileController {

  private final FileService fileService;

  @GetMapping("/download/{fileId}")
  public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) throws IOException {

    FileResponse fileResponse = fileService.downloadFile(fileId);
    HttpHeaders header = new HttpHeaders();
    header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    header.setContentDisposition(ContentDisposition.attachment().filename(fileResponse.getFile().getOriginalFileName()).build());

    return ResponseEntity.ok()
        .headers(header)
        .body(fileResponse.getResource());
  }
}
