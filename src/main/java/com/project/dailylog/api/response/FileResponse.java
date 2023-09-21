package com.project.dailylog.api.response;

import com.project.dailylog.api.domain.File;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.io.Resource;

@Getter
public class FileResponse {
  private File file;
  private Resource resource;

  @Builder
  public FileResponse(File file, Resource resource) {
    this.file = file;
    this.resource = resource;
  }
}
