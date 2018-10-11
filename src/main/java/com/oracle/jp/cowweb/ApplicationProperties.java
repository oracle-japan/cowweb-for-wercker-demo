package com.oracle.jp.cowweb;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jp.gr.java-conf.hhiroshell.cowweb")
class ApplicationProperties {

    private String version;

    String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
