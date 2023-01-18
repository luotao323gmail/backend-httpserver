package com.lt.backend.httpserver.service;

import com.lt.backend.httpserver.http.Request;
import com.lt.backend.httpserver.http.Response;
import com.lt.backend.httpserver.http.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class LoginService implements Service {

    @Override
    public void doGet(Request request, Response response) throws Exception {
        String fileName = "E:\\project\\backend\\backend-httpserver\\src\\main\\webapp\\index.html";
        // 读取文件内容到Stream流中，按行读取
        Stream<String> lines = Files.lines(Paths.get(fileName));
        // 随机行顺序进行数据处理
        lines.forEach(response::println);
    }

    @Override
    public void doPost(Request request, Response response) {
        System.out.println("request = " + request);
        response.setContentType("application/json");
        response.println("{\"username\":\"wangwu1\",\"password\":\"2\"}");
    }
}
