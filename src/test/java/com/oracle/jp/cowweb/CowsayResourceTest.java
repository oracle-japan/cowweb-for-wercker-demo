/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.oracle.jp.cowweb;

import com.github.ricksbrown.cowsay.Cowsay;
import io.helidon.microprofile.tests.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.WebTarget;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * A simple test for {@link com.oracle.jp.cowweb.CowsayResource}
 *
 * @author shukawam
 */
@HelidonTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class CowsayResourceTest {

    private final WebTarget target;

    @Inject
    public CowsayResourceTest(WebTarget target) {
        this.target = target;
    }

    @Test
    public void testCowsayDefault() {
        var actual = target
            .path("cowsay/say")
            .request()
            .get(String.class);
        var expected = Cowsay.say(new String[]{"-f", "default", "Moo!"});
        assertEquals(expected, actual, "HTTP Response of /cowsay/say");
    }

    @Test
    public void testCowsayWithMessage() {
        var actual = target
            .path("cowsay/say")
            .queryParam("say", "Hello")
            .request()
            .get(String.class);
        var expected = Cowsay.say(new String[]{"-f", "default", "Hello"});
        assertEquals(expected, actual, "HTTP Response of /cowsay/say?message=Hello");
    }

    @Test
    public void testCowsayWithMessageWithCowfile() {
        var actual = target
            .path("cowsay/say")
            .queryParam("say", "Hello")
            .queryParam("cowfile", "www")
            .request()
            .get(String.class);
        var expected = Cowsay.say(new String[]{"-f", "www", "Hello"});
        assertEquals(expected, actual, "HTTP Response of /cowsay/say?message=Hello&cowfile=www");
    }

    @Test
    public void testCowthinkDefault() {
        var actual = target
            .path("cowsay/think")
            .request()
            .get(String.class);
        var expected = Cowsay.think(new String[]{"-f", "default", "Moo!"});
        assertEquals(expected, actual, "HTTP Response of /cowsay/say");
    }

    @Test
    public void testCowthinkWithMessage() {
        var actual = target
            .path("cowsay/think")
            .queryParam("think", "Hello")
            .request()
            .get(String.class);
        var expected = Cowsay.think(new String[]{"-f", "default", "Hello"});
        assertEquals(expected, actual, "HTTP Response of /cowsay/say?message=Hello");
    }

    @Test
    public void testCowthinkWithMessageWithCowfile() {
        var actual = target
            .path("cowsay/think")
            .queryParam("think", "Hello")
            .queryParam("cowfile", "www")
            .request()
            .get(String.class);
        var expected = Cowsay.think(new String[]{"-f", "www", "Hello"});
        assertEquals(expected, actual, "HTTP Response of /cowsay/say?message=Hello&cowfile=www");
    }

    @Test
    public void testCowwebPing() {
        var actual = target
            .path("cowsay/ping")
            .request()
            .get(String.class);
        var expected = "I'm working...\n";
        assertEquals(expected, actual, "HTTP Response of /cowsay/ping");
    }

}
