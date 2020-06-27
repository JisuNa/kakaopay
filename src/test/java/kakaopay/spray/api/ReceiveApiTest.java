package kakaopay.spray.api;

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
import java.math.BigInteger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "X-USER-ID=20200627000002"
                , "X-ROOM-ID=dev"
                , "TOKEN=1ZO5x-H4sJX-RjCSZ"
                , "amount=10000"
                , "numberOfRecipients=3"
        }
)
@AutoConfigureMockMvc
class ReceiveApiTest {

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
    void receiveCheckTokenExpire() throws Exception{

        token = "1ZO5x-H4sJX-RjCSZ";

        mockMvc.perform(
                post("/api/v1/spray/receive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("TOKEN", token)
                        .header("X-USER-ID", userId)
                        .header("X-ROOM-ID", roomId)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.msg").value("해당 토큰은 만료되었습니다."))
        ;
    }

    @Test
    void receiveCheckOwnSpray() throws Exception{

        BigInteger userId = BigInteger.valueOf(20200627000003L);
        String roomId = "dev";
        token = "1ZO5x-H4sJX-RjCSZ";

        mockMvc.perform(
                post("/api/v1/spray/receive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("TOKEN", token)
                        .header("X-USER-ID", userId)
                        .header("X-ROOM-ID", roomId)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.msg").value("자신이 뿌리기한 건은 받을 수 없습니다."))
        ;
    }

    @Test
    void receiveCheckDuplication() throws Exception{

        BigInteger userId = BigInteger.valueOf(20200627000001L);
        roomId = "dev";
        token = "1ZO5x-H4sJX-RjCSZ";

        mockMvc.perform(
                post("/api/v1/spray/receive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("TOKEN", token)
                        .header("X-USER-ID", userId)
                        .header("X-ROOM-ID", roomId)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.msg").value("이미 받은 내역이 존재합니다."))
        ;
    }

    @Test
    void receiveCheckSameRoom() throws Exception{

        BigInteger userId = BigInteger.valueOf(20200627000007L);
        roomId = "dev";
        token = "1ZO5x-H4sJX-RjCSZ";

        mockMvc.perform(
                post("/api/v1/spray/receive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("TOKEN", token)
                        .header("X-USER-ID", userId)
                        .header("X-ROOM-ID", roomId)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.msg").value("뿌리기한 사람과 같은방에 있어야 합니다."))
        ;
    }

}