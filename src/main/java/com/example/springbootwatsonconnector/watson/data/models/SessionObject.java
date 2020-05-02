package com.example.springbootwatsonconnector.watson.data.models;

import com.ibm.watson.assistant.v2.model.MessageContext;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionObject {

    private MessageContext context;
    private String sessionId;
    private long addedTime;

    public SessionObject(String sessionId, MessageContext context) {
        this.sessionId = sessionId;
        this.context = context;
        this.addedTime = System.currentTimeMillis();
    }

    /**
     * @param timeout timeout in milliseconds
     * @return true if session is expired else returns false
     * IBM Cloud - The current session lasts for as long a user interacts with the assistant, and then up to 60 minutes of inactivity for Plus or Premium plans (5 minutes for Lite or Standard plans).
     */
    public boolean isExpired(long timeout) {
        return (System.currentTimeMillis() - this.addedTime) > timeout;
    }
}
