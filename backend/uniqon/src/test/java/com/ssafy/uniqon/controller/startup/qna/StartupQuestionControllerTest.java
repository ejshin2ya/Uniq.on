package com.ssafy.uniqon.controller.startup.qna;

import com.ssafy.uniqon.config.SecurityConfig;
import com.ssafy.uniqon.controller.RestDocsTestSupport;
import com.ssafy.uniqon.controller.WithMockCustomUser;
import com.ssafy.uniqon.controller.startup.StartupController;
import com.ssafy.uniqon.dto.response.CursorResult;
import com.ssafy.uniqon.dto.startup.StartupDetailResponseDto;
import com.ssafy.uniqon.dto.startup.qna.*;
import com.ssafy.uniqon.exception.ex.CustomException;
import com.ssafy.uniqon.exception.ex.ErrorCode;
import com.ssafy.uniqon.service.startup.qna.StartupQuestionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ssafy.uniqon.config.RestDocsConfig.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StartupQuestionController.class, excludeFilters = { //!Added!
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class StartupQuestionControllerTest extends RestDocsTestSupport {

    private String accessToken = "accessToken";
    @MockBean
    private StartupQuestionService startupQuestionService;

    @DisplayName(value = "???????????? ?????? ??????")
    @WithMockCustomUser
    @Test
    public void ????????????_??????_??????() throws Exception {

        StartupQuestionReqDto question = StartupQuestionReqDto.builder().question("???????????? question").build();
        String startupQuestionReqDto = objectMapper.writeValueAsString(question);

        given(startupQuestionService.????????????(1L, 1L, question)).willReturn(null);

        mockMvc.perform(
                        post("/app/invest/question/{startupId}", 1L)
                                .header("Authorization", "Bearer " + accessToken)
                                .content(startupQuestionReqDto)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andDo(
                        restDocs.document(
                                pathParameters(parameterWithName("startupId").description("???????????? ID")),
                                requestFields(
                                        fieldWithPath("question").description("??????????????? ?????? ??????").type(JsonFieldType.STRING).attributes(field(
                                                "constraints", "?????? 100 ??????"
                                        ))
                                )
//                                responseFields(
//                                        fieldWithPath("status").description("status"),
//                                        fieldWithPath("message").description("message"),
//                                        fieldWithPath("data").description("data")
//                                )
                        )
                );
    }

    @DisplayName(value = "???????????? question ?????? ?????????")
    @WithMockCustomUser
    @Test
    public void ????????????_??????_??????_?????????() throws Exception {

        List<AnswerParentResponseDto> answerParentResponseDtoList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            AnswerParentResponseDto parentResponseDto = AnswerParentResponseDto.builder()
                    .startupAnswerId(i + 1L)
                    .memberId(4L)
                    .nickname("????????????A")
                    .answer("?????? " + Integer.toString(i))
                    .createDate(LocalDateTime.now())
                    .parentId(null)
                    .myAnswer(Boolean.FALSE)
                    .build();
            answerParentResponseDtoList.add(parentResponseDto);
        }

        AnswerChildrenResponseDto answerParentResponseDto1 = AnswerChildrenResponseDto.builder()
                .startupAnswerId(4L)
                .memberId(1L)
                .nickname("?????????0")
                .answer("?????? Test")
                .createDate(LocalDateTime.now())
                .parentId(1L)
                .myAnswer(Boolean.TRUE)
                .build();
        answerParentResponseDtoList.get(0).getAnswerChildren().add(answerParentResponseDto1);

        AnswerChildrenResponseDto answerParentResponseDto2 = AnswerChildrenResponseDto.builder()
                .startupAnswerId(5L)
                .memberId(4L)
                .nickname("????????????A")
                .answer("?????? Test2")
                .createDate(LocalDateTime.now())
                .parentId(1L)
                .myAnswer(Boolean.TRUE)
                .build();
        answerParentResponseDtoList.get(0).getAnswerChildren().add(answerParentResponseDto2);

        List<StartupQuestionResDto> startupQuestionResDtoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            StartupQuestionResDto startupQuestionResDto = StartupQuestionResDto.builder()
                    .nickname("?????????" + Integer.toString(i))
                    .memberId(i + 1L)
                    .createDate(LocalDateTime.now())
                    .startupQuestionId(i + 1L)
                    .question("??????" + Integer.toString(i))
                    .myQuestion(Boolean.FALSE)
                    .build();
            if (i == 0) {
                startupQuestionResDto.setMyQuestion(Boolean.TRUE);
                startupQuestionResDto.setAnswerParentResponseDtoList(answerParentResponseDtoList);
            }
            startupQuestionResDtoList.add(startupQuestionResDto);
        }

        CursorResult cursorResult = new CursorResult(startupQuestionResDtoList, Boolean.FALSE, 1L);
        given(startupQuestionService.?????????????????????(1L, 1L, 4L, PageRequest.of(0, 3)))
                .willReturn(cursorResult);

        mockMvc.perform(
                        get("/app/invest/question/{startupId}/page?cursorId=4&size=3", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(parameterWithName("startupId").description("???????????? ID")),
                        requestParameters(
                                parameterWithName("cursorId").description("cursorID"),
                                parameterWithName("size").description("size")
                        )
//                        responseFields(
//                                fieldWithPath("status").description("status"),
//                                fieldWithPath("message").description("message"),
//                                fieldWithPath("data.cursorId").description("cursorId"),
//                                fieldWithPath("data.hasNext").description("hasNext"),
//                                fieldWithPath("data.values.[].nickname").description("nickname"),
//                                fieldWithPath("data.values.[].memberId").description("memberId"),
//                                fieldWithPath("data.values.[].createDate").description("createDate"),
//                                fieldWithPath("data.values.[].startupQuestionId").description("startupQuestionId"),
//                                fieldWithPath("data.values.[].question").description("question"),
//                                fieldWithPath("data.values.[].myQuestion").description("myQuestion"),
//                                fieldWithPath("data.values.[].answerParentResponseDtoList").description("answerParentResponseDtoList"),
//                                fieldWithPath("data.values.[].answerParentResponseDtoList.[].startupAnswerId").description("startupAnswerId"),
//                                fieldWithPath("data.values.[].answerParentResponseDtoList.[].memberId").description("memberId"),
//                                fieldWithPath("data.values.[].answerParentResponseDtoList.[].nickname").description("nickname"),
//                                fieldWithPath("data.values.[].answerParentResponseDtoList.[].answer").description("answer"),
//                                fieldWithPath("data.values.[].answerParentResponseDtoList.[].createDate").description("createDate"),
//                                fieldWithPath("data.values.[].answerParentResponseDtoList.[].parentId").description("parentId"),
//                                fieldWithPath("data.values.[].answerParentResponseDtoList.[].myAnswer").description("myAnswer"),
//                                fieldWithPath("data.values.[].answerParentResponseDtoList.[].answerChildren").description("answerChildren"),
//                                fieldWithPath("data.values.[].answerParentResponseDtoList.[].answerChildren.[].startupAnswerId").description("startupAnswerId"),
//                                fieldWithPath("data.values.[].answerParentResponseDtoList.[].answerChildren.[].memberId").description("memberId"),
//                                fieldWithPath("data.values.[].answerParentResponseDtoList.[].answerChildren.[].nickname").description("nickname"),
//                                fieldWithPath("data.values.[].answerParentResponseDtoList.[].answerChildren.[].answer").description("answer"),
//                                fieldWithPath("data.values.[].answerParentResponseDtoList.[].answerChildren.[].createDate").description("createDate"),
//                                fieldWithPath("data.values.[].answerParentResponseDtoList.[].answerChildren.[].parentId").description("parentId"),
//                                fieldWithPath("data.values.[].answerParentResponseDtoList.[].answerChildren.[].myAnswer").description("myAnswer")
//                        )
                ));
    }

    @DisplayName(value = "???????????? question ??????")
    @WithMockCustomUser
    @Test
    public void ????????????_??????_??????() throws Exception {
        StartupQuestionUpdateReqDto questionUpdateReqDto = new StartupQuestionUpdateReqDto("update");

        mockMvc.perform(
                        put("/app/invest/question/{startupQuestionId}", 1L).
                                header("Authorization", "Bearer " + accessToken)
                                .content(objectMapper.writeValueAsString(questionUpdateReqDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(parameterWithName("startupQuestionId").description("???????????? question ID")),
                                requestFields(
                                        fieldWithPath("question").description("??????????????? ?????? ??????(?????????)").type(JsonFieldType.STRING).attributes(field(
                                                "constraints", "?????? 100 ??????"
                                        ))
                                )
                        )
                );
    }

    @DisplayName(value = "???????????? question ?????? ?????? ?????? ??????")
    @WithMockCustomUser
    @Test
    public void ????????????_??????_??????_??????X() throws Exception {
        StartupQuestionUpdateReqDto questionUpdateReqDto = new StartupQuestionUpdateReqDto("update");

        doThrow(new CustomException(ErrorCode.INVALID_ACCESS_MEMBER)).when(startupQuestionService)
                        .????????????(anyLong(), anyLong(), any(StartupQuestionUpdateReqDto.class));

        mockMvc.perform(
                        put("/app/invest/question/{startupQuestionId}", 1L).
                                header("Authorization", "Bearer " + accessToken)
                                .content(objectMapper.writeValueAsString(questionUpdateReqDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().is4xxClientError())
                .andDo(
                        restDocs.document(
                                pathParameters(parameterWithName("startupQuestionId").description("???????????? question ID")),
                                requestFields(
                                        fieldWithPath("question").description("??????????????? ?????? ??????(?????????)").type(JsonFieldType.STRING).attributes(field(
                                                "constraints", "?????? 100 ??????"
                                        ))
                                )
                        )
                );
    }

    @DisplayName(value = "???????????? question ??????")
    @WithMockCustomUser
    @Test
    public void ????????????_??????_??????() throws Exception {
        mockMvc.perform(
                        delete("/app/invest/question/{startupQuestionId}", 1L)
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(parameterWithName("startupQuestionId").description("startupQuestionID"))
                        )
                );
    }

    @DisplayName(value = "???????????? question ???????????? ?????? ??????")
    @WithMockCustomUser
    @Test
    public void ????????????_??????_??????_??????X() throws Exception {

        doThrow(new CustomException(ErrorCode.INVALID_ACCESS_MEMBER)).when(startupQuestionService)
                        .????????????(anyLong(), anyLong());

        mockMvc.perform(
                        delete("/app/invest/question/{startupQuestionId}", 1L)
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().is4xxClientError())
                .andDo(
                        restDocs.document(
                                pathParameters(parameterWithName("startupQuestionId").description("startupQuestionID"))
                        )
                );
    }
}