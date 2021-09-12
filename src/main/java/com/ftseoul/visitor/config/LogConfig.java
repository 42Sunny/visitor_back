package com.ftseoul.visitor.config;

import ch.qos.logback.classic.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "log")
public class LogConfig {
    private Level level;
    private SlackConfig slackConfig;

    public static class SlackConfig {
        private boolean enabled;
        private String webHookUrl;
        private String channel;

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean value) {
            enabled = value;
        }

        public String getWebHookUrl() {
            return this.webHookUrl;
        }

        public void setWebHookUrl(String webHookUrl) {
            this.webHookUrl = webHookUrl;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getChannel() {
            return this.channel;
        }
    }

    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public SlackConfig getSlackConfig() {
        return this.slackConfig;
    }

    public void setSlackConfig(SlackConfig slackConfig) {
        this.slackConfig = slackConfig;
    }
}
