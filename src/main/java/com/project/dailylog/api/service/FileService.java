package com.project.dailylog.api.service;

import com.project.dailylog.api.domain.File;
import com.project.dailylog.api.exception.FileNotFoundException;
import com.project.dailylog.api.repository.FileRepository;
import com.project.dailylog.api.response.FileResponse;
import com.project.dailylog.api.util.FileUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

  @Value("${file.upload.dir}")
  private String uploadDir;
  private final FileRepository fileRepository;


  public String[] uploadFiles(MultipartFile file) {
    if(file != null){
      String newFileName = FileUtil.generateFileName("POST");
      Path uploadPath = Path.of(uploadDir);
      Path filePath = uploadPath.resolve(newFileName);
      try(InputStream inputStream = file.getInputStream()){
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
      }catch(IOException io){
         throw new RuntimeException("파일 업로드 중 오류가 발생하였습니다.");
      }
      return new String[]{uploadDir, newFileName};
    }
    return null;
  }

  public FileResponse downloadFile(Long fileId) throws IOException {

    File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException());

    Path filePath = Path.of(file.getUploadPath(), file.getRealFileName());
    Resource resource = new UrlResource(filePath.toUri());

    if(resource.exists() && resource.isReadable()){
      return FileResponse.builder()
          .resource(resource)
          .file(file)
          .build();
    }else{
      throw new RuntimeException("파일을 찾을 수 없습니다.");
    }
  }
}
