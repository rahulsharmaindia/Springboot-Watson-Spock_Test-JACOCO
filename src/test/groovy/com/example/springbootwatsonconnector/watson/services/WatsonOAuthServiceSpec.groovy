/*
package com.example.springbootwatsonconnector.watson.services

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
class WatsonOAuthServiceSpec extends Specification {

    @Subject
    @Autowired
    WatsonOAuthService authService

    @Autowired
    WatsonAssistantConfig config

    Assistant assistant =Mock()
    Response response = Mock()
    SessionResponse sessionResponse = Mock()
    def validSessionId = "sessionId"

    def setup() {
        authService.assistant = assistant
        ServiceCall<Response>serviceCall = Mock()
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
            authService.initialiseAssistant()
    }


    def "createSession() returns sessionId when session is created successfully"() {
        given:
            response.getStatusCode() >> HttpStatus.CREATED
            1 * sessionResponse.getSessionId() >> validSessionId

        when:
            def sessionId = authService.createSession()

        then:
            sessionId == validSessionId
    }

    def "createSession() returns null when session creation failed for any reason"() {
        given:
            response.getStatusCode() >> HttpStatus.BAD_REQUEST

        when:
            def sessionId = authService.createSession()

        then:
            sessionId == null
    }

    def "deleteSession() returns true when session is deleted successfully"(){
        given:
            response.getStatusCode() >> HttpStatus.OK

        when:
            def deleted = authService.deleteSession(validSessionId)

        then:
            deleted == true
    }
    def "deleteSession() returns false when session deletion fails"(){
        given:
            response.getStatusCode() >> HttpStatus.BAD_REQUEST

        when:
            def deleted = authService.deleteSession(validSessionId)

        then:
            deleted == false
    }
}
*/
