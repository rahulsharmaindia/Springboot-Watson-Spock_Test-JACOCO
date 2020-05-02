package com.example.springbootwatsonconnector.watson.services;

import com.example.springbootwatsonconnector.watson.client.WatsonClient;
import com.example.springbootwatsonconnector.watson.configuration.WatsonAssistantConfig;
import com.example.springbootwatsonconnector.watson.data.models.*;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.watson.assistant.v2.model.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
public class WatsonService {

    @Autowired
    private WatsonClient client;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    WatsonAssistantConfig config;

    private Map<String, SessionObject> sessionCache = new ConcurrentHashMap<>();

    public String createSession() {
        SessionResponse response;
        try {
            response = client.createNewSession();
        } catch (RuntimeException re) {
            log.error("Session creation failed", re);
            return null;
        }
        return response.getSessionId();
    }

    public boolean deleteSession(String sessionId) {
        try {
            return client.deleteSessionById(sessionId);
        } catch (RuntimeException re) {
            log.error("Session deletion failed", re);
            return false;
        }
    }

    public List<WatsonResponse> sendMessage(WatsonRequest request) {
        MessageResponse messageResponse = null;
        String sfSessionId = request.getSfSessionId();
        SessionObject sessionObject = sessionCache.get(sfSessionId);
        String sessionId;
        MessageContext context;
        if (sessionObject == null) {
            sessionId = createSession();
            context = new MessageContext.Builder().build();
        } else {
            sessionId = sessionObject.isExpired(config.getSessionTimeout()) ? createSession() : sessionObject.getSessionId();
            context = sessionObject.getContext();
        }
        try {
            messageResponse = client.sendMessage(sessionId, request.getMessage(), context);
            sessionCache.put(sfSessionId, new SessionObject(sessionId, messageResponse.getContext()));
        } catch (NotFoundException e) {
            log.error("Request failed due to - " + e.getMessage());
            retrySendMessage(request, sfSessionId);
        } catch (RuntimeException e) {
            log.error("Error while getting response from watson with message - " + e.getMessage());
            return Collections.emptyList();
        }
        return processResponse(messageResponse);
    }

    private void retrySendMessage(WatsonRequest request, String sfSessionId) {
        sessionCache.remove(sfSessionId);
        sendMessage(request);
    }

    private List<WatsonResponse> processResponse(MessageResponse messageResponse) {
        List<WatsonResponse> responses = new ArrayList<>();
        List<RuntimeResponseGeneric> genericResponses = messageResponse.getOutput().getGeneric();
        for (RuntimeResponseGeneric generic : genericResponses) {
            if (generic.responseType().equals(RuntimeResponseGeneric.ResponseType.TEXT)) {
                WatsonResponse watsonResponse = parseWatsonResponse(MessageType.TEXT, generic);
                responses.add(watsonResponse);
            } else if (generic.responseType().equals(RuntimeResponseGeneric.ResponseType.OPTION)) {
                WatsonResponse watsonResponse = parseWatsonResponse(MessageType.OPTION, generic);
                responses.add(watsonResponse);
            }
        }
        return responses;
    }

    public WatsonResponse parseWatsonResponse(MessageType type, RuntimeResponseGeneric response) {
        return type.parseAlgo.apply(this, response);
    }

    public WatsonResponse parseOptionResponse(RuntimeResponseGeneric resp) {
        WatsonResponse response = new WatsonResponse();
        modelMapper.typeMap(DialogNodeOutputOptionsElement.class, WatsonOptions.class)
                .addMappings(mapper -> mapper.map(src -> src.getValue().getInput().text(), WatsonOptions::setValue));
        Type listType = new TypeToken<List<WatsonOptions>>() {
        }.getType();
        List<WatsonOptions> watsonOptionsList = modelMapper.map(resp.options(), listType);
        response.setTitle(resp.title());
        response.setDescription(resp.description());
        response.setOptions(watsonOptionsList);
        response.setResponseType(MessageType.OPTION);
        return response;
    }

    public WatsonResponse parseTextResponse(RuntimeResponseGeneric resp) {
        WatsonResponse response = new WatsonResponse();
        response.setResponseType(MessageType.TEXT);
        response.setText(resp.text());
        response.setTitle(resp.title());
        response.setDescription(resp.description());
        return response;
    }


}
