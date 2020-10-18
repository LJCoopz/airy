package co.airy.core.api.admin;

import co.airy.kafka.schema.application.ApplicationCommunicationChannels;
import co.airy.kafka.schema.application.ApplicationCommunicationTags;
import co.airy.kafka.schema.application.ApplicationCommunicationWebhooks;
import co.airy.kafka.test.TestHelper;
import co.airy.kafka.test.junit.SharedKafkaTestResource;
import co.airy.spring.core.AirySpringBootApplication;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AirySpringBootApplication.class)
@TestPropertySource(value = "classpath:test.properties")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class TagsControllerTest {

    @RegisterExtension
    public static final SharedKafkaTestResource sharedKafkaTestResource = new SharedKafkaTestResource();
    private static final ApplicationCommunicationChannels applicationCommunicationChannels = new ApplicationCommunicationChannels();
    private static final ApplicationCommunicationWebhooks applicationCommunicationWebhooks = new ApplicationCommunicationWebhooks();
    private static final ApplicationCommunicationTags applicationCommunicationTags = new ApplicationCommunicationTags();
    private static TestHelper testHelper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll() throws Exception {
        testHelper = new TestHelper(sharedKafkaTestResource,
                applicationCommunicationChannels,
                applicationCommunicationWebhooks,
                applicationCommunicationTags
        );
        testHelper.beforeAll();
    }

    @AfterAll
    static void afterAll() throws Exception {
        testHelper.afterAll();
    }

    @BeforeEach
    void init() throws Exception {
        testHelper.waitForCondition(
                () -> mvc.perform(get("/health")).andExpect(status().isOk()),
                "Application is not healthy"
        );
    }

    @Test
    void createsTag() throws Exception {
        final String name = "awesome-tag";
        final String color = "tag-red";
        final String payload = "{\"name\":\"" + name + "\",\"color\": \"" + color + "\"}";

        testHelper.waitForCondition(() -> mvc.perform(post("/tags.create")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(not(nullValue()))))
                ,"/tags.create failed"
        );
    }

    @Test
    void listTags() throws Exception {
        final String name = "awesome-tag";
        final String color = "tag-red";
        final String payload = "{\"name\":\"" + name + "\",\"color\": \"" + color + "\"}";

        final String createTagResponse = mvc.perform(post("/tags.create")
                .headers(buildHeaders())
                .content(payload))
                .andReturn()
                .getResponse()
                .getContentAsString();

        final JsonNode jsonNode = objectMapper.readTree(createTagResponse);
        final String tagId = jsonNode.get("id").textValue();

        testHelper.waitForCondition(() -> {
            mvc.perform(post("/tags.list")
                    .headers(buildHeaders())
                    .content(payload))
                    .andExpect(jsonPath("$.data.length()", is(1)))
                    .andExpect(jsonPath("$.data[0].id").value(is(tagId)))
                    .andExpect(jsonPath("$.data[0].name").value(is(name)))
                    .andExpect(jsonPath("$.data[0].color").value(is("RED")));
        }, "/tags.list failed");
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        return headers;
    }
}