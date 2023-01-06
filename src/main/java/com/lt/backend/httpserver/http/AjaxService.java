package com.lt.backend.httpserver.http;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class AjaxService implements Service{
    @Override
    public void doPost(Request request, Response response) {
        response.setContentType("application/json");

        response.println("{\"username\":\"wangwu\",\"password\":\"2\"}");
    }
}
