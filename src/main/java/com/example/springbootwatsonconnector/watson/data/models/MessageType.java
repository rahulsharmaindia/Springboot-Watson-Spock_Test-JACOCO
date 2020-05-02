package com.example.springbootwatsonconnector.watson.data.models;

import com.example.springbootwatsonconnector.watson.client.WatsonClient;
import com.example.springbootwatsonconnector.watson.services.WatsonService;
import com.ibm.watson.assistant.v2.model.RuntimeResponseGeneric;

import java.util.function.BiFunction;

public enum MessageType {
    TEXT(WatsonService::parseTextResponse),
    OPTION(WatsonService::parseOptionResponse);

    public final BiFunction<WatsonService, RuntimeResponseGeneric, WatsonResponse> parseAlgo;

    MessageType(BiFunction<WatsonService, RuntimeResponseGeneric, WatsonResponse> parseAlgo) {
        this.parseAlgo = parseAlgo;
    }
}