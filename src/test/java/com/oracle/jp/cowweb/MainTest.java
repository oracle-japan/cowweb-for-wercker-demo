/*
 * Copyright (c) 2018, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oracle.jp.cowweb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import java.net.HttpURLConnection;

import com.github.ricksbrown.cowsay.Cowsay;

import io.helidon.webserver.WebServer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MainTest {

    private static WebServer webServer;

    @BeforeAll
    public static void startTheServer() throws Exception {
        webServer = Main.startServer();

        long timeout = 2000; // 2 seconds should be enough to start the server
        long now = System.currentTimeMillis();

        while (!webServer.isRunning()) {
            Thread.sleep(100);
            if ((System.currentTimeMillis() - now) > timeout) {
                Assertions.fail("Failed to start webserver");
            }
        }
    }

    @AfterAll
    public static void stopServer() throws Exception {
        if (webServer != null) {
            webServer.shutdown()
                     .toCompletableFuture()
                     .get(10, TimeUnit.SECONDS);
        }
    }

    @Test
    public void testHelloWorld() throws Exception {
        HttpURLConnection conn;

        conn = getURLConnection("GET", "/cowsay/ping");
        Assertions.assertEquals(200, conn.getResponseCode(), "HTTP response of /cowsay/ping");
        Assertions.assertEquals(CowsayService.pingReplyMessage, readString(conn.getInputStream()), "Content of /cowsay/ping");

        conn = getURLConnection("GET", "/cowsay/say");
        Assertions.assertEquals(200, conn.getResponseCode(), "HTTP response of /cowsay/say");
        Assertions.assertEquals(Cowsay.say(new String[]{"-f", "default", "Moo!"}), readString(conn.getInputStream()), "Content of /cowsay/say");

        conn = getURLConnection("GET", "/health");
        Assertions.assertEquals(200, conn.getResponseCode(), "HTTP response of /health");

        conn = getURLConnection("GET", "/metrics");
        Assertions.assertEquals(200, conn.getResponseCode(), "HTTP response of /metrics");
    }

    private HttpURLConnection getURLConnection(String method, String path) throws Exception {
        URL url = new URL("http://localhost:" + webServer.port() + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Accept", "application/json");
        System.out.println("Connecting: " + method + " " + url);
        return conn;
    }

    private String readString(InputStream inputStream) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        for (int n ; 0 < (n = inputStream.read(buf)) ; ){
            out.write(buf, 0, n);
        }
        out.close();
        return new String(out.toByteArray(), "UTF-8"); // Or whatever encoding
    }
}
