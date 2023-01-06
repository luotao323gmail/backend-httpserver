package com.lt.backend.httpserver.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Response {

    public static final String CRLF = System.lineSeparator();

    private int httpCode = 200;

    private String httpMessage = "ok";
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);

    public void println(String str) {
        printWriter.println(str);
    }

    private String contentType = "text/html";

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getHttpMessage() {
        return httpMessage;
    }

    public byte[] getResponseByteArray() {
        printWriter.flush();
        printWriter.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }

    public byte[] buildResponse() {
        byte[] bodyArr = getResponseByteArray();
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 " + getHttpCode() + " " + getHttpMessage());
        sb.append(CRLF);
        sb.append("Date:Sun, 21 Apr 2013 15:12:46 GMT");
        sb.append(CRLF);
        sb.append("Server:Apache");
        sb.append(CRLF);
        sb.append("Connection:Close");
        sb.append(CRLF);
        sb.append("Content-Type:").append(getContentType());
        sb.append(CRLF);
        sb.append("Content-Length: ").append(bodyArr.length);
        sb.append(CRLF);
        sb.append(CRLF);
        byte[] headerArr = sb.toString().getBytes(getCharset());
        ByteBuffer byteBuffer = ByteBuffer.allocate(headerArr.length + bodyArr.length + CRLF.getBytes().length);
        byteBuffer.put(headerArr);
        byteBuffer.put(bodyArr);
        byteBuffer.put(CRLF.getBytes(getCharset()));
        return byteBuffer.array();
    }
}
