package com.example.springbootwatsonconnector.watson.data.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WatsonRequest {
    private String sfSessionId;
    private String message;
}
