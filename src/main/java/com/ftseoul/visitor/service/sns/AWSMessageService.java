package com.ftseoul.visitor.service.sns;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.ShortUrlDto;
import com.ftseoul.visitor.dto.ShortUrlResponseListDto;
import com.ftseoul.visitor.dto.StaffDto;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.service.ShortUrlService;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
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

    private final String prefix = "+82";

    private final String domain = "https://dev.vstr.kr";

    private final ShortUrlService shortUrlService;

    public AWSMessageService(Seed seed, ShortUrlService shortUrlService) {
        this.seed = seed;
        this.shortUrlService = shortUrlService;
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
    public void sendMessage(String phoneNumber, String value) {
        String message = "[이노베이션아카데미]\n"
            +"아래 링크 QR을 출입 시 제시해주세요\n"
            +domain + "/" + value;
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
        String message = "[방문 신청]"
            +"\n일시: " + staffDto.getDate().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"))
            +"\n예약확인 : " + "https://visitor.dev.42seoul.io/";
//            +"\n장소: " + staffDto.getPlace()
//            +"\n목적: " + staffDto.getVisitorPurpose()
//            + "\n예약자 명단: ";
        List<Visitor> visitors = staffDto.getVisitors();
        if (visitors != null && visitors.size() > 0) {
            long count = visitors.stream().count() - 1;
            String representor = seed.decrypt(visitors.get(0).getName());
//        message += representor + "님 외 " + String.valueOf(count) + "명";
        }
        SnsClient snsClient = credentialService.getSnsClient();
        PublishRequest publishRequest = PublishRequest.builder()
            .phoneNumber(prefix.concat(staffDto.getPhone()))
            .message(message)
            .build();
        PublishResponse publishResponse = snsClient.publish(publishRequest);
        log.info("message status: " + publishResponse.sdkHttpResponse().statusCode());
        snsClient.close();
        log.info("Sent message Id is {}", publishResponse.messageId());
    }

    @Override
    public void sendMessages(List<Visitor> visitors, LocalDateTime date) {

        List<ShortUrlDto> shortUrlDtoList = visitors
            .stream()
                .map(v -> new ShortUrlDto(seed.encrypt(v.getId().toString())
                    , seed.decrypt(v.getPhone())
                    , null))
                    .collect(Collectors.toList());

        ShortUrlResponseListDto shortUrlLists = shortUrlService.createUrls(shortUrlDtoList);

        shortUrlLists.getUrlResponseList().forEach(shorUrl ->
            sendMessage(shorUrl.getId(), shorUrl.getValue()));
    }

    @PostConstruct
    public void createCredentialService() {
        this.credentialService = new CredentialService(this.awsAccessKey, this.awsSecretKey, this.awsRegion);
    }
}
