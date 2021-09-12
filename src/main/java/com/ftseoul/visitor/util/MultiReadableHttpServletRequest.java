package com.ftseoul.visitor.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;

public class MultiReadableHttpServletRequest extends HttpServletRequestWrapper {

    private ByteArrayOutputStream reusableBytes;

    public MultiReadableHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (reusableBytes == null) {
            reuseInputStream();
        }
        return new reusableServletInputStream();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private void reuseInputStream() throws IOException {
        reusableBytes = new ByteArrayOutputStream();
        IOUtils.copy(super.getInputStream(), reusableBytes);
    }


    public class reusableServletInputStream extends ServletInputStream {
        private ByteArrayInputStream input;

        public reusableServletInputStream() {
            input = new ByteArrayInputStream(reusableBytes.toByteArray());
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {

        }

        @Override
        public int read() throws IOException {
            return input.read();
        }
    }
}
