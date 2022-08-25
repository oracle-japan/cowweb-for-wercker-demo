package com.oracle.jp.cowweb;

import com.github.ricksbrown.cowsay.Cowsay;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import java.util.*;
import java.util.logging.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Remote cowsay invoker using Helidon.
 *
 * @author shukawam
 */
@Path("/cowsay")
@RequestScoped
public class CowsayResource {

    private static final Logger LOGGER = Logger.getLogger(CowsayResource.class.getName());

    private final String defaultMessage;
    private final String defaultCowfile;
    private final String pingReplyMessage;

    @Inject
    public CowsayResource(@ConfigProperty(name = "cowweb.message") String message, @ConfigProperty(name = "cowweb.cowfile") String cowfile, @ConfigProperty(name = "cowweb.ping-message") String pingReplyMessage) {
        this.defaultMessage = message;
        this.defaultCowfile = cowfile;
        this.pingReplyMessage = pingReplyMessage;
    }

    /**
     * Return cowsay's 'say' message.
     *
     * @param message
     * @param cowfile
     * @return Cowsay's 'say' message.
     */
    @GET
    @Path("/say")
    public String say(@QueryParam("say") Optional<String> message, @QueryParam("cowfile") Optional<String> cowfile) {
        var env = message.map(m -> System.getenv(m));
        var params = new String[]{"-f", cowfile.orElse(defaultCowfile), env.orElse(message.orElse(defaultMessage))};
        return Cowsay.say(params);
    }

    /**
     * Return cowsay's 'think' message.
     *
     * @param message
     * @param cowfile
     * @return Return cowsay's 'think' message.
     */
    @GET
    @Path("/think")
    public String think(@QueryParam("think") Optional<String> message, @QueryParam("cowfile") Optional<String> cowfile) {
        var env = message.map(m -> System.getenv(m));
        var params = new String[]{"-f", cowfile.orElse(defaultCowfile), env.orElse(message.orElse(defaultMessage))};
        return Cowsay.think(params);
    }

    /**
     * Send back a fixed String.
     *
     * @return Send back a fixed String.
     */
    @GET
    @Path("/ping")
    public String ping() {
        System.out.println("I'm working...");
        return "I'm working...";
    }
}
