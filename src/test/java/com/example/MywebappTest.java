package com.example;

import org.junit.jupiter.api.Test;
import rife.test.MockConversation;

import static org.junit.jupiter.api.Assertions.*;

public class MywebappTest {
    @Test
    void verifyRoot() {
        var m = new MockConversation(new MywebappSite());
        assertEquals(m.doRequest("/").getStatus(), 302);
    }

    @Test
    void verifyHello() {
        var m = new MockConversation(new MywebappSite());
        assertEquals("Hello Mywebapp", m.doRequest("/hello")
            .getTemplate().getValue("title"));
    }
}
