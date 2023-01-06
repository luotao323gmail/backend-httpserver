package com.lt.backend.httpserver.http;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class LoginService implements Service{
    @Override
    public void doPost(Request request, Response response) {
        try {
            String fileName = "E:\\project\\backend\\backend-httpserver\\src\\main\\webapp\\index.html";
            // 读取文件内容到Stream流中，按行读取
            Stream<String> lines = Files.lines(Paths.get(fileName));
            // 随机行顺序进行数据处理
            lines.forEach(response::println);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
