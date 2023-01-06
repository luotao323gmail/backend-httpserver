package com.lt.backend.httpserver.http;

import java.util.LinkedHashMap;
import java.util.Map;

public class Request {

    private byte[] htmlRequestByte;



    private Map<String,String> headers = new LinkedHashMap<>();

    public Request(byte[] htmlRequestByte) {
        this.htmlRequestByte = htmlRequestByte;
        buildHeader();
    }

    public void buildHeader(){
        String ss = new String(htmlRequestByte);
        String[] arr = ss.split(System.lineSeparator());
        for (int i = 0; i < arr.length; i++) {
            if(i==arr.length-1){
                continue;
            }
            String s = arr[i];
            String[] split = s.split(":");
            if(split.length>1){
                headers.put(split[0],split[1].trim());
            }else{
                System.out.println("error = " + s);
            }
        }
    }

    public static Request buildByHttpRequestBytes(byte[] bytes){
        String str = new String(bytes);
//        System.out.println("str = " + str);
        Request httpRequest = new Request(bytes);

        return httpRequest;
    }
}
