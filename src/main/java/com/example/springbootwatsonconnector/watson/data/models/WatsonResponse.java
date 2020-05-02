package com.example.springbootwatsonconnector.watson.data.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WatsonResponse {
    private MessageType responseType;
    private String text;
    private List<WatsonOptions> options;
    private String title;
    private String description;

}
