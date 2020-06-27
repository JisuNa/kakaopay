package kakaopay.spray.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kakaopay.spray.dto.SprayDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "X-USER-ID=20200627000002"
                , "X-ROOM-ID=a"
                , "TOKEN=1ZO5x-H4sJX-RjCSZ"
                , "amount=10000"
                , "numberOfRecipients=4"
        }
)
@AutoConfigureMockMvc
class SprayControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Value("${X-USER-ID}") private String userId;
    @Value("${X-ROOM-ID}") private String roomId;
    @Value("${TOKEN}") private String token;
    @Value("${amount}") private String amount;
    @Value("${numberOfRecipients}") private String numberOfRecipients;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @Transactional
    void spray() throws Exception {

        SprayDTO sprayDTO = new SprayDTO();
        sprayDTO.setAmount(Integer.parseInt(amount));
        sprayDTO.setNumberOfRecipients(Integer.parseInt(numberOfRecipients));

        mockMvc.perform(
                post("/api/v1/spray")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", userId)
                        .header("X-ROOM-ID", roomId)
                        .content(mapper.writeValueAsString(sprayDTO))
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                ;
    }

    @Test
    @Transactional
    void receive() throws Exception{

        mockMvc.perform(
                post("/api/v1/spray/receive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("TOKEN", token)
                        .header("X-USER-ID", userId)
                        .header("X-ROOM-ID", roomId)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.amount").isNotEmpty())
        ;
    }

    @Test
    void checkSpray() throws Exception{
        mockMvc.perform(
                get("/api/v1/spray")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("TOKEN", token)
                        .header("X-USER-ID", userId)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data").isNotEmpty())
        ;
    }
}