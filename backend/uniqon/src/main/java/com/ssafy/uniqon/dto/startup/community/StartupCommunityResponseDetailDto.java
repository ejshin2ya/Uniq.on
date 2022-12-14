package com.ssafy.uniqon.dto.startup.community;

import com.querydsl.core.annotations.QueryProjection;
import com.ssafy.uniqon.domain.startup.community.CommunityComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartupCommunityResponseDetailDto {
    private String title;
    private String content;
    private String nickName;
    private String startupName;
    private Integer hit;
    private Integer commentsCount;
    private List<CommunityCommentResponseDto> commentList;
    private LocalDateTime createdDate;

    public StartupCommunityResponseDetailDto(String title, String content, String nickName, String startupName, Integer hit, LocalDateTime createdDate){
        this.title = title;
        this.content = content;
        this.nickName = nickName;
        this.startupName = startupName;
        this.hit = hit;
        this.createdDate = createdDate;
    }
}
