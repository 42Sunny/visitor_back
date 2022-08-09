package com.ftseoul.visitor.p6spyconfig;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class CustomP6spySqlFormat implements MessageFormattingStrategy {

    private static final String EXECUTION = "Execution Time : ";
    private static final String CALL_STACK = "\n\tCall Stack : ";
    private static final String MS = " ms\n";
    private static final String CONNECTION_ID = "\n\n\tConnection ID : ";
    private static final String NEW_LINE = "\n--------------------------------------";
    private static final String QUERY_FILE_NAME = "../p6spy-%s.log";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private final Logger logger = LoggerFactory.getLogger(CustomP6spySqlFormat.class);

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = formatSql(category, sql);
        if (sql.trim().isEmpty()){
            return "";
        }
        StringBuffer sb = new StringBuffer(sql).append(createStack(connectionId, elapsed));
        writeFile(String.valueOf(sb));
        return String.valueOf(sb);
    }

    private void writeFile(String finalSql){

        CompletableFuture.runAsync(() -> {
            File file = new File(String.format(QUERY_FILE_NAME, LocalDate.now()));
            try{
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                bw.write(LocalDateTime.now().format(FORMATTER));
                bw.write(finalSql.toUpperCase() + "\n");
            }catch (IOException e){
                logger.error("[ERROR] :: exception", e);
            }
        });
    }

    private String formatSql(String category, String sql){
        if (sql == null || sql.trim().equals("")) return sql;

        if (Category.STATEMENT.getName().equals(category)) {
            String tmpSql = sql.trim().toLowerCase(Locale.ROOT);
            if (tmpSql.startsWith("create") || tmpSql.startsWith("alter") || tmpSql.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            }else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
            sql = "|\nHeFormatSql(p6spy sql, Hibernate format):" + sql;

        }
        return sql;
    }

    private StringBuffer createStack(int connectionId, long elapsed) {
        Stack<String> callStack = new Stack<>();
        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();

        Arrays.stream(stackTraceElements)
                .filter(stackTraceElement -> stackTraceElement.toString().startsWith("com.ftseoul.visitor"))
                .forEach(stackTraceElement ->
                        callStack.push(String.valueOf(stackTraceElement)));

        StringBuffer stringBuffer = new StringBuffer();
        int order = 1;
        while (!callStack.isEmpty()){
            stringBuffer.append("\n\t\t").append(order++)
                    .append('.').append(callStack.pop());
        }

        return new StringBuffer().append(CONNECTION_ID).append(connectionId)
                .append(EXECUTION).append(elapsed).append(MS)
                .append(CALL_STACK).append(stringBuffer).append("\n")
                .append(NEW_LINE);
    }
}
