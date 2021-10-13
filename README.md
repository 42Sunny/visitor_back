# 42Visitor_server
#### 스웨거 문서 주소: http://localhost:8080/swagger-ui.html

### AWS SMS 기능 사용 시 설정
#### ~/.aws/credentials
```
[default]
aws_access_key_id=
aws_secret_access_key=
```

#### ./src/main/resources/application-local.properties
```
cloud.aws.region.static= 선택 region
cloud.aws.stack.auto=false
aws.accessKey=
aws.secretKey=
aws.region= 해당 region
```

### 데이터베이스 설정
#### ./src/main/resources/application-local.properties
```
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/ftvisitor?characterEncoding=UTF-8&serverTimezone=Asia/Seoul
spring.datasource.username=계정
spring.datasource.password=비밀번호
```

### 에러 로그 슬렉 웹훅
#### ./src/main/resources/application-local.properties
```
#Web Hook
log.slack-config.enabled=true
log.slack-config.web-hook-url = 웹훅URL
log.slack-config.channel=체널
log.level=ERROR (전송용 로그레벨 설정)
```

log.slack-config.enabled의 값을 True / False로 설정함으로서
설정한 log.level 이상에 해당하는 로그를 생성시 등록된 슬렉 체널로 전송할지를 선택 할 수 있습니다.

### 암호화 Seed 설정
#### ./src/main/resources/application-local.properties
```
# Should be 16 letters
encrypt.seed=abcdefghij123456
encrypt.seed.init=abcdefghij123456
```
개인정보는 데이터베이스에 암호화되어서 저장됩니다.
암호화 되는 KEY를 지정을 해주셔야 하며, 16글자로 지정해주셔야합니다.

### 단축URL 서비스 API Key
#### ./src/main/resources/application-local.properties
```
api-key=apikey
```
해당 서비스는 별도의 단축 URL 서비스를 구축해서 사용하고 있습니다.
단축 URL 서비스를 사용하지 않으시려면
ShortUrlService의 파일을 삭제하거나 Bean 등록을 하지않고
ReserveController 파일에서
saveReserve 메서드의 다음부분을 제거하시고
```java
List<ShortUrlResponseDto> shortUrlList = shortUrlService.createShortUrls(visitors, staffReserveInfo);
        List<ShortUrlResponseDto> visitorShortUrls = shortUrlService.filterVisitorShortUrls(shortUrlList);
        ShortUrlResponseDto staffShortUrl = shortUrlService.filterStaffShortUrls(shortUrlList);

        visitorShortUrls.forEach(v -> smsService.sendMessage(v.getId(),visitorService.createSMSMessage(v.getValue())));
        smsService.sendMessage(seed.decrypt(staff.getPhone()), staffService.createSaveSMSMessage(visitors, reserve.getDate(), staffShortUrl.getValue()));
        log.info("Send message to Staff and Visitors");
```
updateReserve 메서드의 다음부분을 제거하시고 사용하시면 됩니다.
```java
 List<ShortUrlResponseDto> shortUrlList = shortUrlService.createShortUrls(visitors, staffReserveInfo);
        List<ShortUrlResponseDto> visitorShortUrls = shortUrlService.filterVisitorShortUrls(shortUrlList);
        ShortUrlResponseDto staffShortUrl = shortUrlService.filterStaffShortUrls(shortUrlList);

        visitorShortUrls.forEach(v -> smsService.sendMessage(v.getId(),visitorService.createSMSMessage(v.getValue())));
        smsService.sendMessage(seed.decrypt(staff.getPhone()), staffService.createModifySMSMessage(staffShortUrl.getValue()));
        log.info("Send text messages to visitors and staff");
```
