package com.ssafy.uniqon.repository.startup.qna;

import com.ssafy.uniqon.dto.startup.qna.AnswerParentResponseDto;
import com.ssafy.uniqon.dto.startup.qna.StartupQuestionResDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StartupQuestionRepositoryCustom {

    List<StartupQuestionResDto> findStartupQuestionResDtoList(Long startupId);

    List<StartupQuestionResDto> findQuestionListDtoPage(Long startupId, Pageable page);

    List<StartupQuestionResDto> findQuestionListDtoLessPage(Long startupId, Long cursorId, Pageable page);


    List<AnswerParentResponseDto> findAnswerParentResponseDtoList(Long startupQuestionId);
}
