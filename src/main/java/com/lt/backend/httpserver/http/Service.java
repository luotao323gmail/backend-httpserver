package com.lt.backend.httpserver.http;

public interface Service {

     void doGet(Request request, Response httpResponse)throws Exception;

     void doPost(Request request, Response response)throws Exception;
}
