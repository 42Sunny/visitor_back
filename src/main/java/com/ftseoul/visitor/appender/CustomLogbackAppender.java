package com.ftseoul.visitor.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.ftseoul.visitor.config.LogConfig;
import com.ftseoul.visitor.dto.payload.ErrorLog;
import com.ftseoul.visitor.util.MDCUtil;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;

public class CustomLogbackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private final LogConfig logConfig;

    public CustomLogbackAppender(LogConfig logConfig) {
        this.logConfig = logConfig;
    }

    @Override
    protected void append(ILoggingEvent loggingEvent) {
        if (loggingEvent.getLevel().isGreaterOrEqual(logConfig.getLevel())) {
            ErrorLog errorLog = getErrorLog(loggingEvent);
            if (logConfig.getSlackConfig().isEnabled()) {
                sendToSlack(errorLog);
            }
        }
    }

    private void sendToSlack(ErrorLog errorLog) {
        SlackApi slackApi = new SlackApi(logConfig.getSlackConfig().getWebHookUrl());
        List<SlackField> fields = new ArrayList<>();

        SlackField message = new SlackField();
        message.setTitle("에러내용");
        message.setValue(errorLog.getMessage());
        message.setShorten(false);
        fields.add(message);

        SlackField path = new SlackField();
        path.setTitle("요청 URL");
        path.setValue(errorLog.getPath());
        path.setShorten(false);
        fields.add(path);

        SlackField date = new SlackField();
        date.setTitle("발생시간");
        date.setValue(errorLog.getErrorDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
        date.setShorten(true);
        fields.add(date);

        SlackField headerInfo = new SlackField();
        headerInfo.setTitle("Http Header 정보");
        headerInfo.setValue(MDCUtil.toPrettyJson(errorLog.getHeaderMap()));
        headerInfo.setShorten(false);
        fields.add(headerInfo);

        SlackField bodyParams = new SlackField();
        bodyParams.setTitle("Http Body Parameter 정보");
        String bodyParameterInfo = MDCUtil.toPrettyJson(errorLog.getParameterMap());
        if (bodyParameterInfo == null || bodyParameterInfo.equals("{ }")) {
            bodyParameterInfo = "없음";
        }
        bodyParams.setValue(bodyParameterInfo);
        bodyParams.setShorten(false);
        fields.add(bodyParams);

        SlackField bodyContent = new SlackField();
        bodyContent.setTitle("Body 내용");
        String contents = MDCUtil.get(MDCUtil.BODY_CONTENT_MDC);
        if (contents == null || contents.equals("{ }")) {
            contents = "없음";
        }
        bodyContent.setValue(contents);
        bodyContent.setShorten(false);
        fields.add(bodyContent);

        String title = "[방문 예약 서비스 에러]";

        SlackAttachment slackAttachment = new SlackAttachment();
        slackAttachment.setFallback("Error");
        slackAttachment.setColor("danger");
        slackAttachment.setFields(fields);
        slackAttachment.setTitle(title);
        slackAttachment.setText(errorLog.getTrace());

        SlackMessage slackMessage = new SlackMessage("");
        slackMessage.setChannel("#" + logConfig.getSlackConfig().getChannel());
        slackMessage.setAttachments(Collections.singletonList(slackAttachment));

        slackApi.call(slackMessage);


    }

    private ErrorLog getErrorLog(ILoggingEvent loggingEvent) {
        ErrorLog errorLog = new ErrorLog();
        errorLog.setServiceName("방문 예약 서비스");
        errorLog.setMessage(loggingEvent.getFormattedMessage());
        errorLog.setHeaderMap(MDCUtil.get(MDCUtil.HEADER_MAP_MDC));
        errorLog.setParameterMap(MDCUtil.get(MDCUtil.PARAMETER_MAP_MDC));
        errorLog.setPath(MDCUtil.get(MDCUtil.REQUEST_URI_MDC));

        if (loggingEvent.getThrowableProxy() != null) {
            errorLog.setTrace(getStackTrace(loggingEvent.getThrowableProxy().getStackTraceElementProxyArray()));
        }

        return errorLog;
    }

    public String getStackTrace(StackTraceElementProxy[] stackTraceElements) {
        if (stackTraceElements == null || stackTraceElements.length == 0 ) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (StackTraceElementProxy stackTraceElement : stackTraceElements) {
            sb.append(stackTraceElement.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
