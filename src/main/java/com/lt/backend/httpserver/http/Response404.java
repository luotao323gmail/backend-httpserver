package com.lt.backend.httpserver.http;

public class Response404 extends Response{

    @Override
    public int getHttpCode() {
        return 404;
    }

    @Override
    public String getHttpMessage() {
        return "NOT FOUND";
    }

    @Override
    public byte[] getResponseByteArray() {
        return getHttpMessage().getBytes(getCharset());
    }
}
