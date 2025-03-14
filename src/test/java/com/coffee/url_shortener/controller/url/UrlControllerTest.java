package com.coffee.url_shortener.controller.url;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.coffee.url_shortener.controller.dto.AliasRes;
import com.coffee.url_shortener.controller.dto.FullUrlReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.datafaker.Faker;

public class UrlControllerTest extends BaseControllerTest {

    private final String GENERATE_ALIAS_PATH = "/new";
    private final String GENERATE_QR_CODE_PATH = "/qr";
    private final String REDIRECT_PATH = "/";

    final ObjectMapper mapper = new ObjectMapper();
    final Faker faker = new Faker();

    @Test
    void shouldGenerateShortUrl() throws Exception {
        FullUrlReq req = new FullUrlReq(faker.internet().url());

        String json = mapper.writeValueAsString(req);

        mvc.perform(MockMvcRequestBuilders.post(GENERATE_ALIAS_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.alias").exists());
    }

    @Test
    void shouldFailedToGenerateShortUrl() throws Exception {
        FullUrlReq req = new FullUrlReq(faker.internet().domainName());

        String json = mapper.writeValueAsString(req);

        mvc.perform(MockMvcRequestBuilders.post(GENERATE_ALIAS_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists());
    }

    @Test
    void shouldRedirect() throws Exception {
        FullUrlReq req = new FullUrlReq(faker.internet().url());

        String alias = getUrlAlias(req);

        mvc.perform(MockMvcRequestBuilders.get(getAliasUrl(alias)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(req.getUrl()));
    }

    @Test
    void shouldNotFoundRedirect() throws Exception {
        FullUrlReq req = new FullUrlReq(faker.internet().url());

        String alias = new StringBuilder(getUrlAlias(req)).reverse().toString();

        mvc.perform(MockMvcRequestBuilders.get(getAliasUrl(alias)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists());
    }

    private String getUrlAlias(FullUrlReq req) throws JsonProcessingException, Exception  {
        String json = mapper.writeValueAsString(req);

        MvcResult res = mvc.perform(MockMvcRequestBuilders.post(GENERATE_ALIAS_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();
        System.out.print(res.getResponse().getContentAsString());

        AliasRes alias = mapper.readValue(res.getResponse().getContentAsString(), AliasRes.class);

        return alias.getAlias();
    }

    @Test
    void shouldGenerateQrCode() throws Exception {
        FullUrlReq req = new FullUrlReq(faker.internet().url());

        String json = mapper.writeValueAsString(req);

        mvc.perform(MockMvcRequestBuilders.post(GENERATE_QR_CODE_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_PNG));
    }

    private String getAliasUrl(String alias) {
        return REDIRECT_PATH + alias;
    }
}
