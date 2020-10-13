package co.airy.core.api.auth;

import co.airy.core.api.auth.dao.UserDAO;
import co.airy.core.api.auth.dto.User;
import co.airy.spring.core.AirySpringBootApplication;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
@SpringBootTest(properties = {
        "db.debug=true"
}, classes = AirySpringBootApplication.class)
@AutoConfigureMockMvc
@FlywayDataSource
public class UsersControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void userSignup() throws Exception {
        final String firstName = "grace";
        final String email = "grace@airy.co";

        final String requestContent = "{\"email\":\"" + email + "\",\"first_name\":\"" + firstName + "\"," +
                "\"last_name\":\"grace\",\"password\":\"trustno1\"}";

        final String responseString = mvc.perform(post("/users.signup")
                .content(requestContent)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final JsonNode jsonNode = objectMapper.readTree(responseString);
        final String id = jsonNode.get("id").textValue();

        User user = userDAO.findById(UUID.fromString(id));

        assertThat(user.getEmail(), equalTo(email));
    }
}