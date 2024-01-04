package com.project.sokdak2.api.response;

import com.project.sokdak2.api.domain.common.File;
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
