package com.example.springbootwatsonconnector.watson.managers;

import com.example.springbootwatsonconnector.watson.data.models.WatsonRequest;
import com.example.springbootwatsonconnector.watson.data.models.WatsonResponse;
import com.example.springbootwatsonconnector.watson.services.WatsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatsonManager {

    @Autowired
    WatsonService watsonService;

    public List<WatsonResponse> getWatsonResponse(WatsonRequest request){
        return watsonService.sendMessage(request);
    }

}
