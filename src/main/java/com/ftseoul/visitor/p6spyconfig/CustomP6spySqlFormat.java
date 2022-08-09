package com.ftseoul.visitor.p6spyconfig;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;

import java.util.*;

public class CustomP6spySqlFormat implements MessageFormattingStrategy {

    private final static String EXECUTION = "Execution Time : ";
    private final static String CALL_STACK = "\n\tCall Stack : ";
    private final static String MS = " ms\n";
    private final static String CONNECTION_ID = "\n\n\tConnection ID : ";
    private final static String NEW_LINE = "\n--------------------------------------";


    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = formatSql(category, sql);
        if (sql.trim().isEmpty()){
            return "";
        }
        return sql + createStack(connectionId, elapsed);
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

    private String createStack(int connectionId, long elapsed) {
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

        return String.valueOf(new StringBuffer().append(CONNECTION_ID).append(connectionId)
                .append(EXECUTION).append(elapsed).append(MS)
                .append(CALL_STACK).append(stringBuffer).append("\n")
                .append(NEW_LINE));
    }
}
