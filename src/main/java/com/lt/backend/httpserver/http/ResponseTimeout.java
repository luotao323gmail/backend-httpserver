package com.lt.backend.httpserver.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ResponseTimeout extends Response{

    @Override
    public int getHttpCode() {
        return 501;
    }

    @Override
    public String getHttpMessage() {
        return "timeout";
    }

    @Override
    public byte[] getResponseByteArray() {
        return "time out".getBytes(getCharset());
    }
}
