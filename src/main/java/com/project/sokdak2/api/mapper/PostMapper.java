package com.project.sokdak2.api.mapper;

import com.project.sokdak2.api.response.PostResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * author         : choi
 * date           : 2024-03-07
 */

@Mapper
public interface PostMapper {
    List<PostResponse> selectAllPosts();
}
