package com.cognitive.ceppoc.commons.props;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonProps {

    private String bootstrapServers;

    private String topic;

    private String clientId;

    private String groupId;

    @Autowired
    public CommonProps(
            @Value("${kafka.bootstrap.servers}") String bootstrapServers,
            @Value("${kafka.topic}") String topic,
            @Value("${spring.application.name}") String clientId,
            @Value("${kafka.groupId}") String groupId) {
        this.bootstrapServers = bootstrapServers;
        this.topic = topic;
        this.clientId = clientId;
        this.groupId = groupId;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getTopic() {
        return topic;
    }

    public String getClientId() {
        return clientId;
    }

    public String getGroupId() {
        return groupId;
    }
}
