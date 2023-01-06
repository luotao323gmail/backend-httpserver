package com.lt.backend.httpserver.http;

public class Request {

    public static Request buildByHttpRequestBytes(byte[] bytes){
        String str = new String(bytes);
        System.out.println("str = " + str);
        Request httpRequest = new Request();

        return httpRequest;
    }
}
