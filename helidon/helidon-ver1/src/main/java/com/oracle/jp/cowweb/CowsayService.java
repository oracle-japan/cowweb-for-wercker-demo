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

import java.util.Optional;

import com.github.ricksbrown.cowsay.Cowsay;

import io.helidon.common.http.Http;
import io.helidon.config.Config;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

/**
 * A simple service to greet you. Examples:
 *
 * Get default greeting message:
 * curl -X GET http://localhost:8080/greet
 *
 * Get greeting message for Joe:
 * curl -X GET http://localhost:8080/greet/Joe
 *
 * Change greeting
 * curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Howdy"}' http://localhost:8080/greet/greeting
 *
 * The message is returned as a JSON object
 */

public class CowsayService implements Service {

    /**
     * The config value for the key {@code greeting}.
     */
    //private final AtomicReference<String> greeting = new AtomicReference<>();

    //private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());
    public static final String pingReplyMessage = "I'm working...\n";

    CowsayService(Config config) {
        //greeting.set(config.get("app.greeting").asString().orElse("Ciao"));
    }

    /**
     * A service registers itself by updating the routine rules.
     * @param rules the routing rules.
     */
    @Override
    public void update(Routing.Rules rules) {
        rules
            .get("/ping", this::getDefaultMessageHandler)
            .get("/{verb}", this::getHandler);
    }

    private void getDefaultMessageHandler(ServerRequest request, ServerResponse response) {
        System.out.print(pingReplyMessage);
        response.send(pingReplyMessage);
    }

    private void getHandler(ServerRequest request, ServerResponse response) {

        String message = request.queryParams().first("message").orElse("Moo!");
        String cowfile = request.queryParams().first("cowfile").orElse("default");
        //System.out.println(String.format("message => %s", message));
        //System.out.println(String.format("image => %s", image));

        String[] params = new String[]{"-f", cowfile, message};

        Optional<String> verb = Optional.ofNullable(request.path().param("verb"));
        if(!verb.isPresent()){
            response.status(Http.Status.BAD_REQUEST_400).send();
        }
        verb.ifPresent(v -> {
            if(v.equalsIgnoreCase("say")) response.send(Cowsay.say(params));
            else if(v.equalsIgnoreCase("think")) response.send(Cowsay.think(params));
            else response.status(Http.Status.BAD_REQUEST_400).send();
        });
    }
}