package com.ssafy.uniqon.controller.startup.qna;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.uniqon.config.SecurityConfig;
import com.ssafy.uniqon.controller.RestDocsTestSupport;
import com.ssafy.uniqon.controller.WithMockCustomUser;
import com.ssafy.uniqon.dto.startup.qna.AnswerRequestDto;
import com.ssafy.uniqon.dto.startup.qna.AnswerUpdateRequestDto;
import com.ssafy.uniqon.dto.startup.qna.StartupQuestionUpdateReqDto;
import com.ssafy.uniqon.exception.ex.CustomException;
import com.ssafy.uniqon.exception.ex.ErrorCode;
import com.ssafy.uniqon.service.startup.qna.StartupAnswerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.ssafy.uniqon.config.RestDocsConfig.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StartupAnswerController.class, excludeFilters = { //!Added!
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
public class StartupAnswerControllerTest extends RestDocsTestSupport {

    private String accessToken = "accessToken";

    @MockBean
    private StartupAnswerService answerService;

    @DisplayName(value = "???????????? answer ??????")
    @WithMockCustomUser
    @Test
    public void ????????????_????????????() throws Exception {

        AnswerRequestDto answerRequestDto = new AnswerRequestDto(1L, "answer");

        mockMvc.perform(
                        post("/app/invest/answer/{startupQuestionId}", 1L)
                                .header("Authorization", "Bearer " + accessToken)
                                .content(objectMapper.writeValueAsString(answerRequestDto))
                                .contentType(MediaType.APPLICATION_JSON)

                ).andExpect(status().isCreated())
                .andDo(
                        restDocs.document(
                                pathParameters(parameterWithName("startupQuestionId").description("???????????? Question ID")),
                                requestFields(
                                        fieldWithPath("parentId").description("???????????? ?????? startupAnswerID" +
                                                "???????????? ???????????? null").optional().attributes(
                                                field("constraints", "")
                                        ),
                                        fieldWithPath("answer").description("????????? ?????? ??????").attributes(
                                                field("constraints", "?????? 50 ??????")
                                        )
                                )
                        )
                );
    }

    @DisplayName(value = "???????????? answer ??????")
    @WithMockCustomUser
    @Test
    public void ????????????_????????????() throws Exception {
        AnswerUpdateRequestDto answerUpdateRequestDto = new AnswerUpdateRequestDto("Answer Update");

        mockMvc.perform(
                        put("/app/invest/answer/{startupAnswerId}", 1L).
                                header("Authorization", "Bearer " + accessToken)
                                .content(objectMapper.writeValueAsString(answerUpdateRequestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(parameterWithName("startupAnswerId").description("???????????? Answer ID")),
                                requestFields(
                                        fieldWithPath("answer").description("????????? ?????? ??????(??????)").type(JsonFieldType.STRING).attributes(field(
                                                "constraints", "?????? 100 ??????"
                                        ))
                                )
                        )
                );
    }

    @DisplayName(value = "???????????? answer ?????? ?????? ?????? ??????")
    @WithMockCustomUser
    @Test
    public void ????????????_????????????_??????_??????_??????() throws Exception {
        AnswerUpdateRequestDto answerUpdateRequestDto = new AnswerUpdateRequestDto("Answer Update");

        doThrow(new CustomException(ErrorCode.INVALID_ACCESS_MEMBER)).when(answerService)
                        .????????????(anyLong(), anyLong(), any(AnswerUpdateRequestDto.class));

        mockMvc.perform(
                        put("/app/invest/answer/{startupAnswerId}", 1L).
                                header("Authorization", "Bearer " + accessToken)
                                .content(objectMapper.writeValueAsString(answerUpdateRequestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().is4xxClientError())
                .andDo(
                        restDocs.document(
                                pathParameters(parameterWithName("startupAnswerId").description("???????????? Answer ID")),
                                requestFields(
                                        fieldWithPath("answer").description("????????? ?????? ??????(??????)").type(JsonFieldType.STRING).attributes(field(
                                                "constraints", "?????? 100 ??????"
                                        ))
                                )
                        )
                );
    }


    @DisplayName(value = "???????????? answer ??????")
    @WithMockCustomUser
    @Test
    public void ????????????_????????????() throws Exception {
        mockMvc.perform(
                        delete("/app/invest/answer/{startupAnswerId}", 1L)
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(parameterWithName("startupAnswerId").description("???????????? answer ID"))
                        )
                );
    }

    @DisplayName(value = "???????????? answer ?????? ?????? ?????? ??????")
    @WithMockCustomUser
    @Test
    public void ????????????_????????????_??????X() throws Exception {

        doThrow(new CustomException(ErrorCode.INVALID_ACCESS_MEMBER)).when(answerService)
                        .????????????(anyLong(), anyLong());

        mockMvc.perform(
                        delete("/app/invest/answer/{startupAnswerId}", 1L)
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().is4xxClientError())
                .andDo(
                        restDocs.document(
                                pathParameters(parameterWithName("startupAnswerId").description("???????????? answer ID"))
                        )
                );
    }
}

