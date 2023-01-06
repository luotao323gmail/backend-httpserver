package com.lt.backend.httpserver.http;

import java.util.LinkedHashMap;
import java.util.Map;

public class Request {

    private byte[] htmlRequestByte;

    private String requestUrl = "";

    private Map<String,String> headers = new LinkedHashMap<>();

    public Request(byte[] htmlRequestByte) {
        this.htmlRequestByte = htmlRequestByte;
        buildHeader();
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public void buildHeader(){
        String ss = new String(htmlRequestByte);
        String[] arr = ss.split(System.lineSeparator());
        String s1 = arr[0];
        String[] split1 = s1.split("\\s");
        requestUrl = split1[1];
        for (int i = 1; i < arr.length; i++) {
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
        System.out.println("headers = " + headers);
    }

    public static Request buildByHttpRequestBytes(byte[] bytes){
        String str = new String(bytes);
//        System.out.println("str = " + str);
        Request httpRequest = new Request(bytes);

        return httpRequest;
    }
}
