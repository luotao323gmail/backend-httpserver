package com.lt.backend.httpserver.service;

import com.lt.backend.httpserver.http.Request;
import com.lt.backend.httpserver.http.Response;
import com.lt.backend.httpserver.http.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class AjaxService implements Service {

    @Override
    public void doGet(Request request, Response httpResponse) throws Exception {

    }

    @Override
    public void doPost(Request request, Response response) {
        response.setContentType("application/json");

        response.println("{\"username\":\"wangwu\",\"password\":\"2\"}");
        // try {
        //     Thread.sleep(4000);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
    }
}
