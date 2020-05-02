package com.example.springbootwatsonconnector.watson.controllers;

import com.example.springbootwatsonconnector.watson.data.models.WatsonRequest;
import com.example.springbootwatsonconnector.watson.data.models.WatsonResponse;
import com.example.springbootwatsonconnector.watson.managers.WatsonManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    WatsonManager watsonManager;

    @GetMapping("/{sessionId}/{msg}")
    public List<WatsonResponse> getResponse(@PathVariable String sessionId, @PathVariable String msg){
        WatsonRequest watsonRequest = new WatsonRequest(sessionId,msg);
        return watsonManager.getWatsonResponse(watsonRequest);
    }
}
