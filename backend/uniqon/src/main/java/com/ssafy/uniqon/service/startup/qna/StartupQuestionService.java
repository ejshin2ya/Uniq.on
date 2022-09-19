package com.ssafy.uniqon.service.startup.qna;

import com.ssafy.uniqon.domain.startup.Startup;
import com.ssafy.uniqon.domain.member.Member;
import com.ssafy.uniqon.domain.startup.qna.StartupQuestion;
import com.ssafy.uniqon.dto.response.CursorResult;
import com.ssafy.uniqon.dto.startup.qna.AnswerParentResponseDto;
import com.ssafy.uniqon.dto.startup.qna.StartupQuestionReqDto;
import com.ssafy.uniqon.dto.startup.qna.StartupQuestionResDto;
import com.ssafy.uniqon.dto.startup.qna.StartupQuestionUpdateReqDto;
import com.ssafy.uniqon.exception.ex.CustomException;
import com.ssafy.uniqon.exception.ex.ErrorCode;
import com.ssafy.uniqon.repository.startup.qna.StartupQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.ssafy.uniqon.exception.ex.ErrorCode.QUESTION_NOT_FOUND;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StartupQuestionService {

    private final StartupQuestionRepository startupQuestionRepository;
    private final StartupAnswerService startupAnswerService;

    @Transactional
    public Long 질문등록(Long memberId, StartupQuestionReqDto startupQuestionReqDto) {
        Member member = new Member();
        member.changeId(memberId);

        Startup startup = new Startup();
        startup.changeId(startupQuestionReqDto.getStartupId());

        StartupQuestion startupQuestion = StartupQuestion.builder()
                .member(member)
                .question(startupQuestionReqDto.getQuestion())
                .startup(startup)
                .build();

        startupQuestionRepository.save(startupQuestion);
        return startupQuestion.getId();
    }

    public List<StartupQuestionResDto> 질문조회(Long memberId, Long startupId) {
//        List<StartupQuestion> startupQuestionList = startupQuestionRepository.findAllByStartupId(startupId);
//        List<StartupQuestionResDto> startupQuestionResDtoList = startupQuestionList.stream()
//                .map(startupQuestion -> new StartupQuestionResDto(startupQuestion))
//                .collect(Collectors.toList());
//
//        startupQuestionResDtoList.forEach(startupQuestionResDto -> {
//            List<AnswerParentResponseDto> answerParentResponseDtoList = startupAnswerService.댓글조회(startupQuestionResDto.getStartupQuestionId());
//            startupQuestionResDto.changeParentResponseDto(answerParentResponseDtoList);
//        });
        List<StartupQuestionResDto> startupQuestionResDtoList = startupQuestionRepository.findStartupQuestionResDtoList(memberId, startupId);

        return startupQuestionResDtoList;
    }

    public CursorResult<StartupQuestionResDto> 질문조회페이징(Long memberId, Long startupId, Long cursorId, Pageable page) {

        List<StartupQuestionResDto> questions = getQuestions(memberId, startupId, cursorId, page);
        Long lastIdOfList = questions.isEmpty() ?
                null : questions.get(questions.size() - 1).getStartupQuestionId();

        return new CursorResult(questions, hasNext(startupId, lastIdOfList), lastIdOfList);
    }

    private List<StartupQuestionResDto> getQuestions(Long memberId, Long startupId, Long cursorId, Pageable page) {
        return cursorId == null ?
                this.startupQuestionRepository.findQuestionListDtoPage(memberId, startupId, page) :
                this.startupQuestionRepository.findQuestionListDtoLessPage(memberId, startupId, cursorId, page);
    }

    private Boolean hasNext(Long startupId, Long cursorId) {
        if (cursorId == null) return false;
        return this.startupQuestionRepository.existsByIdLessThan(startupId, cursorId);
    }

    /** 테스트 용으로 하나 만듬 */
    public StartupQuestionResDto 질문하나조회(Long startupQuestionId) {
        StartupQuestion startupQuestion = startupQuestionRepository.findById(startupQuestionId).orElseThrow(
                () -> new CustomException(QUESTION_NOT_FOUND)
        );
        StartupQuestionResDto StartupQuestionResDto = new StartupQuestionResDto(startupQuestion);
        return StartupQuestionResDto;
    }

    @Transactional
    public void 질문수정(StartupQuestionUpdateReqDto startupQuestionUpdateReqDto) {
        StartupQuestion startupQuestion = startupQuestionRepository.findById(startupQuestionUpdateReqDto.getStartupQuestionId())
                .orElseThrow(() -> new CustomException(QUESTION_NOT_FOUND));
        startupQuestion.update(startupQuestionUpdateReqDto);
    }

    @Transactional
    public void 질문삭제(Long startupQuestionId) {
        startupQuestionRepository.deleteById(startupQuestionId);
    }
}