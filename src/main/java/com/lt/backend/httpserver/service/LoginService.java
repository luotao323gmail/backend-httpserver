package com.lt.backend.httpserver.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.lt.backend.httpserver.http.Request;
import com.lt.backend.httpserver.http.Response;
import com.lt.backend.httpserver.http.Service;

public class LoginService implements Service {

    @Override
    public void doGet(Request request, Response response) throws Exception {
        String html = "<html><body><h1>Welcome!<br/> time is :%s</h1></body></html>";

        // TemplateEngine engine = new TemplateEngine();

        // StringBuilder html = new StringBuilder();
        // String fileName =
        // "E:\\project\\backend\\backend-httpserver\\src\\main\\webapp\\index.html";
        // try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
        // // // 随机行顺序进行数据处理
        // lines.forEach(html::append);
        // }
        // Context context = new Context();
        // context.setVariable("name", "lisi");
        // // 调用引擎，处理模板和数据
        // String result = engine.process(html.toString(), context);

        response.println(String.format(html, new Date().toString()));
        System.out.println("login html");
    }

    @Override
    public void doPost(Request request, Response response) {
        System.out.println("request = " + request);
        response.setContentType("application/json");
        response.println("{\"username\":\"wangwu1\",\"password\":\"2\"}");
    }
}
