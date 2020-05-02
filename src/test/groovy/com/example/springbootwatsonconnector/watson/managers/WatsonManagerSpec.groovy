package com.example.springbootwatsonconnector.watson.managers

import com.example.springbootwatsonconnector.watson.data.models.MessageType
import com.example.springbootwatsonconnector.watson.data.models.WatsonRequest
import com.example.springbootwatsonconnector.watson.data.models.WatsonResponse
import com.example.springbootwatsonconnector.watson.services.WatsonService
import spock.lang.Specification
import spock.lang.Subject

class WatsonManagerSpec extends Specification {

    @Subject
    WatsonManager watsonManager = new WatsonManager();

    private WatsonService watsonService = Mock()
    private WatsonRequest watsonRequest = Mock()

    def setup() {
        watsonManager.watsonService = watsonService
    }

    def "getWatsonResponse() should return the same watson response as returned by watsonService"() {
        given:
        watsonService.sendMessage(watsonRequest) >> [new WatsonResponse(MessageType.TEXT, "Response Message", null, null, null)]

        when:
        def wResponse = watsonManager.getWatsonResponse(watsonRequest).get(0)

        then:
        wResponse.getText() == "Response Message"
        wResponse.getResponseType() == MessageType.TEXT
    }

}
