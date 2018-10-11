package com.oracle.jp.cowweb;

import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class CowsayControllerIT {

    private static final String TARGET_RESOURCES_ROOT =
            "http://localhost:8080/cowsay/";

    @Test
    public void pingTest() {
        WebTarget target =
                ClientBuilder.newClient().target(TARGET_RESOURCES_ROOT).path("ping");
        AtomicReference<String> response =
                new AtomicReference<>(target.request().get(String.class));

        assertEquals("I'm working...", response.get());
    }

}
