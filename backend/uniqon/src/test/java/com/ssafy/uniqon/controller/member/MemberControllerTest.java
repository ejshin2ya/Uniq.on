package com.ssafy.uniqon.controller.member;

import com.ssafy.uniqon.config.SecurityConfig;
import com.ssafy.uniqon.controller.RestDocsTestSupport;
import com.ssafy.uniqon.controller.WithMockCustomUser;
import com.ssafy.uniqon.controller.auth.AuthController;
import com.ssafy.uniqon.domain.member.MemberType;
import com.ssafy.uniqon.dto.member.*;
import com.ssafy.uniqon.exception.ex.CustomException;
import com.ssafy.uniqon.exception.ex.CustomValidationException;
import com.ssafy.uniqon.exception.ex.ErrorCode;
import com.ssafy.uniqon.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.ssafy.uniqon.config.RestDocsConfig.field;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class, excludeFilters = { //!Added!
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class MemberControllerTest extends RestDocsTestSupport {

    private String accessToken = "accessToken";

    @MockBean
    private MemberService memberService;

    @DisplayName(value = "?????? ????????? ??????")
    @WithMockCustomUser
    @Test
    public void ??????_?????????_??????() throws Exception {

        MemberUpdateDto memberUpdateDto = new MemberUpdateDto("nickname");

        String requestDtoJson = objectMapper.writeValueAsString(memberUpdateDto);

        MockMultipartFile request = new MockMultipartFile("memberUpdateDto", "jsondata",
                "application/json", requestDtoJson.getBytes(StandardCharsets.UTF_8));

        MockMultipartFile memberProfile = new MockMultipartFile("file", "member_profile.jpeg",
                "image/jpeg", "<<jpeg data>>".getBytes(StandardCharsets.UTF_8));


        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.fileUpload("/app/member");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        mockMvc.perform(
                        builder.file(request).file(memberProfile)
                                .header("Authorization", "Bearer " + accessToken)

                ).andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestParts(
                                        partWithName("file").description("?????? ????????? ?????????"),
                                        partWithName("memberUpdateDto").description("?????? ????????? ?????? DTO")
                                ),
                                requestPartFields("memberUpdateDto",
                                        fieldWithPath("nickName").description("nickname").attributes(field("constraints", "???????????? 3~30????????? ?????????."))
                                )
                        ));
    }

    @DisplayName(value = "?????? ????????? ?????? ??????(?????? ????????? ??????)")
    @WithMockCustomUser
    @Test
    public void ??????_?????????_??????_???????????????_??????() throws Exception {

        MemberUpdateDto memberUpdateDto = new MemberUpdateDto("nickname");

        String requestDtoJson = objectMapper.writeValueAsString(memberUpdateDto);

        MockMultipartFile request = new MockMultipartFile("memberUpdateDto", "jsondata",
                "application/json", requestDtoJson.getBytes(StandardCharsets.UTF_8));

        MockMultipartFile memberProfile = new MockMultipartFile("file", "member_profile.jpeg",
                "image/jpeg", "<<jpeg data>>".getBytes(StandardCharsets.UTF_8));


        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.fileUpload("/app/member");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        doThrow(new CustomException(ErrorCode.FILE_UPLOAD_ERROR)).when(memberService)
                .memberUpdate(anyLong(), any(MemberUpdateDto.class), any(MultipartFile.class));

        mockMvc.perform(
                        builder.file(request).file(memberProfile)
                                .header("Authorization", "Bearer " + accessToken)

                ).andExpect(status().is4xxClientError())
                .andDo(
                        restDocs.document(
                                requestParts(
                                        partWithName("file").description("?????? ????????? ?????????"),
                                        partWithName("memberUpdateDto").description("?????? ????????? ?????? DTO")
                                ),
                                requestPartFields("memberUpdateDto",
                                        fieldWithPath("nickName").description("nickname").attributes(field("constraints", "???????????? 3~30????????? ?????????."))
                                )
                        ));
    }

    @DisplayName(value = "?????? ????????? ?????? ??????")
    @WithMockCustomUser
    @Test
    public void ??????_?????????_??????_??????() throws Exception {

        MemberUpdateDto memberUpdateDto = new MemberUpdateDto("ab");

        String requestDtoJson = objectMapper.writeValueAsString(memberUpdateDto);

        MockMultipartFile request = new MockMultipartFile("memberUpdateDto", "jsondata",
                "application/json", requestDtoJson.getBytes(StandardCharsets.UTF_8));

        MockMultipartFile memberProfile = new MockMultipartFile("file", "member_profile.jpeg",
                "image/jpeg", "<<jpeg data>>".getBytes(StandardCharsets.UTF_8));


        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.fileUpload("/app/member");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        doThrow(new CustomValidationException("????????? ?????? ??????", new HashMap<>())).when(memberService)
                .memberUpdate(anyLong(), any(MemberUpdateDto.class), any(MultipartFile.class));

        mockMvc.perform(
                        builder.file(request).file(memberProfile)
                                .header("Authorization", "Bearer " + accessToken)

                ).andExpect(status().is4xxClientError())
                .andDo(
                        restDocs.document(
                                requestParts(
                                        partWithName("file").description("?????? ????????? ?????????"),
                                        partWithName("memberUpdateDto").description("?????? ????????? ?????? DTO")
                                ),
                                requestPartFields("memberUpdateDto",
                                        fieldWithPath("nickName").description("nickname").attributes(field("constraints", "???????????? 3~30????????? ?????????."))
                                )
                        ));
    }

    @DisplayName(value = "?????? ????????? ??????")
    @WithMockCustomUser
    @Test
    public void ??????_?????????_??????() throws Exception {
        MemberProfileDto memberProfileDto = MemberProfileDto.builder()
                .walletAddress("0X1234")
                .nickName("nickName")
                .email("email@naver.com")
                .profileImage("profileImage")
                .memberType(MemberType.USER)
                .build();

        given(memberService.memberDetail(1L)).willReturn(memberProfileDto);

        mockMvc.perform(
                get("/app/member")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @DisplayName(value = "?????? ??????")
    @WithMockCustomUser
    @Test
    public void ??????_??????() throws Exception {
        mockMvc.perform(
                delete("/app/member")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @DisplayName(value = "??????????????? ?????? ??????")
    @WithMockCustomUser
    @Test
    public void ???????????????_??????_??????() throws Exception {

        MemberFavStartupDto favStartupDto = MemberFavStartupDto.builder().memberId(2L).startupId(1L).
                startupName("????????????A").title("title").profileImage("profileImage").
                description("description").nftTargetCount(10).nftReserveCount(5)
                .nftPrice(new Double(2)).dueDate(LocalDateTime.now().plusDays(2))
                .planPaper("planPaper").planPaperImg("planPaperImg").roadMap("roadMap")
                .nftImage("nftImage").isFav(Boolean.TRUE).build();

        MemberFavStartupDto favStartupDto2 = MemberFavStartupDto.builder().memberId(3L).startupId(2L).
                startupName("????????????B").title("title2").profileImage("profileImage").
                description("description2").nftTargetCount(10).nftReserveCount(5)
                .nftPrice(new Double(2)).dueDate(LocalDateTime.now().plusDays(2))
                .planPaper("planPaper").planPaperImg("planPaperImg").roadMap("roadMap")
                .nftImage("nftImage").isFav(Boolean.TRUE).build();

        List<MemberFavStartupDto> favStartupDtoList = Arrays.asList(favStartupDto, favStartupDto2);

        given(memberService.findMemberFavStartup(1L)).willReturn(favStartupDtoList);

        mockMvc.perform(
                get("/app/member/mypage/favstartup")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @DisplayName(value = "???????????????(?????????) ?????? ????????? ???????????? ??????")
    @WithMockCustomUser
    @Test
    public void ???????????????_?????????_?????????_????????????_??????() throws Exception {
        MemberInvestedStartupDto memberInvestedStartupDto = MemberInvestedStartupDto.builder()
                .memberId(1L).startupId(1L).startupName("startupName").title("title").description("description").planPaperImg("profileImage")
                .nftTargetCount(10).nftReserveCount(5).nftPrice(new Double(2)).dueDate(LocalDateTime.now().plusDays(1))
                .planPaper("planPaper").planPaperImg("planPaperImg").roadMap("roadMap").nftImage("nftImage")
                .nftDescription("nftDescription").build();

        MemberInvestedStartupDto memberInvestedStartupDto2 = MemberInvestedStartupDto.builder()
                .memberId(2L).startupId(2L).startupName("startupName").title("title").description("description").profileImage("profileImage")
                .nftTargetCount(10).nftReserveCount(5).nftPrice(new Double(2)).dueDate(LocalDateTime.now().plusDays(1))
                .planPaper("planPaper").planPaperImg("planPaperImg").roadMap("roadMap").nftImage("nftImage")
                .nftDescription("nftDescription").build();

        List<MemberInvestedStartupDto> memberInvestedStartupDtoList = Arrays.asList(memberInvestedStartupDto, memberInvestedStartupDto2);

        given(memberService.findInvestedStartup(any(Long.class))).willReturn(memberInvestedStartupDtoList);

        mockMvc.perform(
                get("/app/member/mypage/invest")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

    }

    @DisplayName(value = "???????????????(????????????) ?????? ?????? ??????")
    @WithMockCustomUser
    @Test
    public void ???????????????_????????????_??????_??????_??????() throws Exception {
        StartupInvestedListDto startupInvestedListDto = StartupInvestedListDto.builder().memberId(1L).startupId(1L).startupName("startupName").title("title")
                .description("description").nftTargetCount(10).nftReserveCount(5).nftPrice(new Double(2)).profileImage("profileImage")
                .dueDate(LocalDateTime.now().plusDays(1)).planPaper("planPaper").planPaperImg("planPaperImg")
                .roadMap("roadMap").nftImage("nftImage").nftDescription("nftDescription").build();

        StartupInvestedListDto startupInvestedListDto2 = StartupInvestedListDto.builder().memberId(1L).startupId(2L).startupName("startupName").title("title")
                .description("description").nftTargetCount(10).nftReserveCount(5).nftPrice(new Double(2)).profileImage("profileImage")
                .dueDate(LocalDateTime.now().plusDays(1)).planPaper("planPaper").planPaperImg("planPaperImg")
                .roadMap("roadMap").nftImage("nftImage").nftDescription("nftDescription").build();

        List<StartupInvestedListDto> startupInvestedListDtoList = Arrays.asList(startupInvestedListDto, startupInvestedListDto2);

        given(memberService.findStartupInvestedList(1L)).willReturn(startupInvestedListDtoList);

        mockMvc.perform(
                get("/app/member/mypage/startup")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @DisplayName(value = "??????????????? ?????? NFT ??????")
    @WithMockCustomUser
    @Test
    public void ???????????????_??????_NFT_??????() throws Exception {

        MemberOwnNftDto memberOwnNftDto = MemberOwnNftDto.builder().startupId(1L).nftDescription("nftDescription").nftImage("nftImage")
                .price(new Double(2)).metaData("metaData").startupName("startupName").build();
        MemberOwnNftDto memberOwnNftDto2 = MemberOwnNftDto.builder().startupId(2L).nftDescription("nftDescription").nftImage("nftImage")
                .price(new Double(2)).metaData("metaData").startupName("startupName").build();

        List<MemberOwnNftDto> memberOwnNftDtoList = Arrays.asList(memberOwnNftDto, memberOwnNftDto2);

        given(memberService.findMemberOwnNftList(any(Long.class))).willReturn(memberOwnNftDtoList);

        mockMvc.perform(
                get("/app/member/mypage/own-nft")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}