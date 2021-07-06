package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.config.AWSConfig;
import com.ftseoul.visitor.service.CredentialService;
import com.ftseoul.visitor.sns.TopicTest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TopicController {

    private final AWSConfig awsConfig;
    private final CredentialService credentialService;
    private TopicTest topicTest;

    private ResponseStatusException getResponseStatusException(SnsResponse response) {
        return new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.sdkHttpResponse().statusText().get()
        );
    }

    @PostMapping("/createTopic")
    public ResponseEntity<String> createTopic(final String topicName) {
        System.out.println(awsConfig.getAwsAccessKey());
        System.out.println(awsConfig.getAwsSecretKey());
        CreateTopicRequest createTopicRequest =
                CreateTopicRequest.builder()
                        .name(topicName)
                        .build();

        SnsClient snsClient = credentialService.getSnsClient();
        final CreateTopicResponse createTopicResponse = snsClient.createTopic(createTopicRequest);

        if (!createTopicResponse.sdkHttpResponse().isSuccessful()) {
            throw getResponseStatusException(createTopicResponse);
        }
        log.info("topic name = " + createTopicResponse.topicArn());
        log.info("topic list = " + snsClient.listTopics());
        snsClient.close();
        return new ResponseEntity<>("Topic Creating Success", HttpStatus.OK);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestParam final String endpoint,
                                            @RequestParam final String topicArn) {
        final SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                .protocol("sms")
                .topicArn(topicArn)
                .endpoint(endpoint)
                .build();
        SnsClient snsClient = credentialService.getSnsClient();
        final SubscribeResponse subscribeResponse = snsClient.subscribe(subscribeRequest);
        if (!subscribeResponse.sdkHttpResponse().isSuccessful()) {
            throw getResponseStatusException(subscribeResponse);
        }
        log.info("topicArn to subscribe = " + subscribeResponse.subscriptionArn());
        log.info("subscription list = " + snsClient.listSubscriptions());
        snsClient.close();
        return new ResponseEntity<>("TOPIC Subscribe Success", HttpStatus.OK);
    }

    @PostMapping("/publish")
    public String publish(@RequestParam String topicArn, @RequestBody Map<String, Object> message) {
        SnsClient snsClient = credentialService.getSnsClient();
        final PublishRequest publishRequest = PublishRequest.builder()
                .topicArn(topicArn)
                .subject("spring boot test sms")
                .message(message.toString())
                .build();
        PublishResponse publishResponse = snsClient.publish(publishRequest);
        log.info("message status: " + publishResponse.sdkHttpResponse().statusCode());
        snsClient.close();
        return "Sent MESSAGE ID = " + publishResponse.messageId();
    }
}
