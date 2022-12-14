package com.ssafy.uniqon.controller.startup.community;

import com.ssafy.uniqon.config.SecurityConfig;
import com.ssafy.uniqon.controller.RestDocsTestSupport;
import com.ssafy.uniqon.dto.startup.community.CommunityCommentRequestDto;
import com.ssafy.uniqon.dto.startup.community.CommunityCommentRequestModifyDto;
import com.ssafy.uniqon.service.startup.community.CommunityCommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.ssafy.uniqon.config.RestDocsConfig.field;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommunityCommentController.class, excludeFilters = { //!Added!
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class CommunityCommentControllerTest extends RestDocsTestSupport {

    private String accessToken = "accessToken";

    @MockBean
    private CommunityCommentService communityCommentService;

    @DisplayName(value = "???????????? ???????????? ?????? ??????")
    @Test
    public void ????????????_????????????_??????_??????() throws Exception {
        CommunityCommentRequestDto commentRequestDto = new CommunityCommentRequestDto(1L, "content");

        mockMvc.perform(
                        post("/app/invest/community-comments/{communityId}/comment", 1L)
                                .header("Authorization", "Bearer " + accessToken)
                                .content(objectMapper.writeValueAsString(commentRequestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(parameterWithName("communityId").description("communityID")),
                                requestFields(
                                        fieldWithPath("parentId").description("???????????? ?????? ?????? ?????? communityId" +
                                                "???????????? ?????? ?????? null").attributes(
                                                field("constraints", "")
                                        ),
                                        fieldWithPath("content").description("???????????? ?????? ??????").attributes(
                                                field("constraints", "?????? 100 ??????")
                                        )
                                )
                        ));
    }

    @DisplayName(value = "???????????? ???????????? ?????? ??????")
    @Test
    public void ????????????_????????????_??????_??????() throws Exception {
        CommunityCommentRequestModifyDto commentRequestModifyDto = new CommunityCommentRequestModifyDto("content");

        mockMvc.perform(
                        put("/app/invest/community-comments/{communityId}/{commentId}", 1L, 1L)
                                .header("Authorization", "Bearer " + accessToken)
                                .content(objectMapper.writeValueAsString(commentRequestModifyDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(parameterWithName("communityId").description("communityID"),
                                        parameterWithName("commentId").description("commentId")
                                ),
                                requestFields(
                                        fieldWithPath("content").description("???????????? ?????? ??????").attributes(
                                                field("constraints", "?????? 100 ??????")
                                        ).optional()
                                )
                        )
                );
    }

    @DisplayName(value = "????????????_????????????_??????_??????")
    @Test
    public void ????????????_????????????_??????_??????() throws Exception {
        mockMvc.perform(
                        delete("/app/invest/community-comments/{communityId}/{commentId}", 1L, 1L)
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(parameterWithName("communityId").description("communityID"),
                                        parameterWithName("commentId").description("commentId")
                                )
                        )
                );
    }
}