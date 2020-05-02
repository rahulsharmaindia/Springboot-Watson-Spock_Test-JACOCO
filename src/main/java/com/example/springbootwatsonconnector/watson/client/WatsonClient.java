package com.example.springbootwatsonconnector.watson.client;

import com.example.springbootwatsonconnector.watson.configuration.WatsonAssistantConfig;
import com.ibm.cloud.sdk.core.http.HttpStatus;
import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class WatsonClient {

    @Autowired
    WatsonAssistantConfig config;

    @Getter
    private Assistant assistant;

    @PostConstruct
    public void createAssistant() {
        Authenticator authenticator = new IamAuthenticator(config.getApiKey());
        assistant = new Assistant(config.getVersionDate(), authenticator);
        assistant.setServiceUrl(config.getServiceURL());
    }

    public SessionResponse createNewSession() throws RuntimeException {
        CreateSessionOptions createSessionOptions = getCreateSessionOptions();
        Response<SessionResponse> response = assistant.createSession(createSessionOptions).execute();
        if (!asExpected(response.getStatusCode(), HttpStatus.CREATED)) {
            throw new RuntimeException("Session Creation failed with status code -" + response.getStatusCode());
        }
        return response.getResult();
    }

    private CreateSessionOptions getCreateSessionOptions() {
        return new CreateSessionOptions.Builder()
                .assistantId(config.getAssistantId())
                .build();
    }

    public boolean deleteSessionById(String sessionId) {
        DeleteSessionOptions deleteSessionOptions = getDeleteSessionOptions(sessionId);
        Response response = assistant.deleteSession(deleteSessionOptions).execute();
        if (asExpected(response.getStatusCode(),HttpStatus.OK)) {
            return true;
        } else {
            throw new RuntimeException();
        }
    }

    private DeleteSessionOptions getDeleteSessionOptions(String sessionId) {
        return new DeleteSessionOptions.Builder()
                .assistantId(config.getAssistantId())
                .sessionId(sessionId)
                .build();
    }

    public MessageResponse sendMessage(String sessionId, String msg, MessageContext context) {
        MessageOptions messageOptions = getMessageOptions(sessionId, msg, context);
        Response<MessageResponse> response = assistant.message(messageOptions).execute();
        if (!asExpected(response.getStatusCode(), HttpStatus.OK)) {
            throw new RuntimeException("Error while sending message to Watson - " + response.getStatusCode());
        }
        return response.getResult();
    }

    private MessageOptions getMessageOptions(String sessionId, String msg, MessageContext context) {
        // send messages
        MessageInputOptions inputOptions = new MessageInputOptions.Builder()
                .debug(true)
                .returnContext(true)
                .build();
        MessageInput input = new MessageInput.Builder()
                .text(msg)
                .messageType(MessageInput.MessageType.TEXT)
                .options(inputOptions)
                .build();
        return new MessageOptions.Builder()
                .assistantId(config.getAssistantId())
                .sessionId(sessionId)
                .input(input)
                .context(context)
                .build();
    }

    private boolean asExpected(int status, int expectedStatus){
        return status==expectedStatus?true:false;
        //write code to log the response status to debug the problem with watson.
    }


}
