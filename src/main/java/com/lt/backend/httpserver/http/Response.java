package com.lt.backend.httpserver.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Response {

    public static final String CRLF = "\r\n";

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);

    public void println(String str){
        printWriter.println(str);
    }

    public Charset getCharset(){
       return StandardCharsets.UTF_8;
    }

    public byte[] buildResponse(){

        printWriter.flush();
        printWriter.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK");
        sb.append(CRLF);
        sb.append("Date:Sun, 21 Apr 2013 15:12:46 GMT");
        sb.append(CRLF);
        sb.append("Server:Apache");
        sb.append(CRLF);
        sb.append("Connection:Close");
        sb.append(CRLF);
        sb.append("Content-Type:text/html; charset=").append(getCharset().toString());
        sb.append(CRLF);
        sb.append("Content-Length: ").append(bytes.length);
        sb.append(CRLF);
        sb.append(CRLF);
        try {
            byteArrayOutputStream.write(sb.toString().getBytes(getCharset()));
            byteArrayOutputStream.write(bytes); // 内容为Hello, world
            byteArrayOutputStream.write(CRLF.getBytes(getCharset()));
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
