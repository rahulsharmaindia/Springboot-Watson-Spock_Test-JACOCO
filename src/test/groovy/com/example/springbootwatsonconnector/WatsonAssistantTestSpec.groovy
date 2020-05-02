package com.example.springbootwatsonconnector


import com.example.springbootwatsonconnector.watson.services.WatsonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

//@SpringBootTest
class WatsonAssistantTest {//extends Specification {

    @Autowired(required = false)
    private WatsonService watsonService

    def "when context is loaded then all expected beans are created"() {
        expect: "the WebController is created"
        watsonService
    }

    def "say hi to watson service"() {
        expect: "response is not null"
        watsonService.testSendMessages("hi")
    }

    /* @Unroll
     def "Should be able to remove from list"() {
         given:
         def list = [1, 2, 3, 4]

         when:
         list.remove(0)

         then:
         list == [2, 3, 4]
     }

     @Unroll
     def "numbers to the power of two"(int a, int b, int c) {
         expect:
         Math.pow(a, b) == c

         where:
         a | b | c
         1 | 2 | 2
         2 | 2 | 4
         3 | 2 | 9
     }*/
}