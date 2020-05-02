package com.example.springbootwatsonconnector.watson.client

import com.example.springbootwatsonconnector.SpringbootWatsonConnectorApplication
import com.example.springbootwatsonconnector.watson.configuration.WatsonAssistantConfig
import com.ibm.cloud.sdk.core.http.HttpStatus
import com.ibm.cloud.sdk.core.http.Response
import com.ibm.cloud.sdk.core.http.ServiceCall
import com.ibm.watson.assistant.v2.Assistant
import com.ibm.watson.assistant.v2.model.CreateSessionOptions
import com.ibm.watson.assistant.v2.model.DeleteSessionOptions
import com.ibm.watson.assistant.v2.model.SessionResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest(classes = SpringbootWatsonConnectorApplication.class)
@ActiveProfiles("test")
class WatsonClientSpec extends Specification {

    @Subject
    @Autowired
    WatsonClient watsonClient

    @Autowired
    WatsonAssistantConfig config

    Assistant assistant = Mock()
    Response response = Mock()
    SessionResponse sessionResponse = Mock()
    def validSessionId = "sessionId"

    def setup() {
        watsonClient.assistant = assistant
        ServiceCall<Response> serviceCall = Mock()
        CreateSessionOptions createSessionOptions = new CreateSessionOptions.Builder()
                .assistantId(config.getAssistantId())
                .build()
        DeleteSessionOptions deleteSessionOptions = new DeleteSessionOptions.Builder()
                .assistantId(config.getAssistantId())
                .sessionId(validSessionId)
                .build()

        assistant.createSession(createSessionOptions) >> serviceCall
        assistant.deleteSession(deleteSessionOptions) >> serviceCall
        serviceCall.execute() >> response
        response.getResult() >> sessionResponse
    }

    def "Check Assistant is initialized"() {
        expect: "Assistant is not null"
        watsonClient.createAssistant()
    }

    def "getAssistant should return not null"() {
        expect:
        watsonClient.getAssistant();
    }

    def "createNewSession() returns sessionResponse when response status is 201"() {
        given:
        response.getStatusCode() >> HttpStatus.CREATED

        when:
        def sResponse = watsonClient.createNewSession()

        then:
        sResponse == sessionResponse
    }

    def "createNewSession() throws RuntimeException when response status is not 201"() {
        given:
        response.getStatusCode() >> HttpStatus.BAD_REQUEST

        when:
        watsonClient.createNewSession()

        then:
        thrown(RuntimeException)
    }

    def "deleteSessionById() returns true when session is deleted successfully with status code 200"() {
        given:
        response.getStatusCode() >> HttpStatus.OK

        when:
        def deleted = watsonClient.deleteSessionById(validSessionId)

        then:
        deleted == true
    }

    def "deleteSessionById() returns false when session deletion fails with status code not 200"() {
        given:
        response.getStatusCode() >> HttpStatus.BAD_REQUEST

        when:
        watsonClient.deleteSessionById(validSessionId)

        then:
        thrown(RuntimeException)
    }
}
