/*
package com.example.springbootwatsonconnector.watson.services;

import com.example.springbootwatsonconnector.watson.client.WatsonClient;
import com.example.springbootwatsonconnector.watson.configuration.WatsonAssistantConfig;
import com.ibm.cloud.sdk.core.http.HttpStatus;
import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.DeleteSessionOptions;
import com.ibm.watson.assistant.v2.model.SessionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WatsonOAuthService {

    @Autowired
    WatsonAssistantConfig config;
    @Autowired
    WatsonClient client;

    public String createSession() {
        Response<SessionResponse> response;
        try {
            response = client.createNewSession();
        } catch (RuntimeException re) {
            log.error("Session creation failed", re);
            return null;
        }
        return response.getResult().getSessionId();
    }




    public boolean deleteSession(String sessionId) {
        try {
            return client.deleteSessionById(sessionId);
        } catch (RuntimeException re) {
            log.error("Session deletion failed", re);
            return false;
        }
    }
}
*/
