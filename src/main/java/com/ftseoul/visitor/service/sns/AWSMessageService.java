package com.ftseoul.visitor.service.sns;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.StaffDto;
import com.ftseoul.visitor.encrypt.Seed;

import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Slf4j
@Service
public class AWSMessageService implements SMSService {
    @Value("${aws.accessKey}")
    private String awsAccessKey;

    @Value("${aws.secretKey}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;

    private final Seed seed;

    public CredentialService credentialService;

    private final String messageTemplate = "예약번호: ";

    private final String prefix = "+82";

    private final String QRCodePath = "\nQR코드: https://visitor.dev.42seoul.io/qr/";

    public AWSMessageService(Seed seed) {
        this.seed = seed;
    }

    public static class CredentialService {

        private final String accessKey;
        private final String secretKey;
        private final String awsRegion;


        public CredentialService(String accessKey, String secretKey, String awsRegion) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
            this.awsRegion = awsRegion;
        }

        public AwsCredentialsProvider getAwsCredentials(String accessKey, String secretKey) {
            AwsBasicCredentials awsBasicCredentials
                = AwsBasicCredentials.create(accessKey, secretKey);
            return () -> awsBasicCredentials;
        }

        public SnsClient getSnsClient() {
            return SnsClient.builder()
                .credentialsProvider(getAwsCredentials(accessKey, secretKey))
                .region(Region.of(awsRegion))
                .build();
        }
    }

    @Override
    public void sendMessage(String phoneNumber, Long reserveId, String QRcode) {
        String message = messageTemplate + reserveId.toString() + QRCodePath + QRcode;
        SnsClient snsClient = credentialService.getSnsClient();
        PublishRequest publishRequest = PublishRequest.builder()
            .phoneNumber(prefix.concat(phoneNumber))
            .message(message)
            .build();
        PublishResponse publishResponse = snsClient.publish(publishRequest);
        log.info("message status: " + publishResponse.sdkHttpResponse().statusCode());
        snsClient.close();
        log.info("Sent message Id is {}", publishResponse.messageId());
    }

    @Override
    public void sendMessage(StaffDto staffDto) {
        String message = messageTemplate + (staffDto.getReserveId().toString())
            +"\n일시: " + staffDto.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            + "\n예약자 명단: ";
        List<Visitor> visitors = staffDto.getVisitors();
        long count = visitors.stream().count() - 1;
        String representor = seed.decrypt(visitors.get(0).getName());
        message += representor + "님 외 " + String.valueOf(count) + "명";
        SnsClient snsClient = credentialService.getSnsClient();
        PublishRequest publishRequest = PublishRequest.builder()
            .phoneNumber(prefix.concat(seed.decrypt(staffDto.getPhone())))
            .message(message)
            .build();
        PublishResponse publishResponse = snsClient.publish(publishRequest);
        log.info("message status: " + publishResponse.sdkHttpResponse().statusCode());
        snsClient.close();
        log.info("Sent message Id is {}", publishResponse.messageId());
    }

    @Override
    public void sendMessages(List<Visitor> visitors) {
        visitors
            .forEach(visitor -> sendMessage(
                seed.decrypt(visitor.getPhone()),
                visitor.getReserveId(),
                visitor.getId().toString()
                ));
    }

    @PostConstruct
    public void createCredentialService() {
        this.credentialService = new CredentialService(this.awsAccessKey, this.awsSecretKey, this.awsRegion);
    }
}
