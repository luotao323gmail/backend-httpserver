package com.lt.backend.httpserver.http;

import java.util.LinkedHashMap;
import java.util.Map;

public class Request {

    private byte[] htmlContent;

    private byte[] contentBytes;

    private String requestUrl = "";

    private String queryString = "";

    private String method;

    private Map<String, String> headers = new LinkedHashMap<>();

    private Request(byte[] contentBytes) {
        this.contentBytes = contentBytes;
        buildHeader();
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getKeyUrl() {
        return this.method + "." + requestUrl;
    }

    public boolean isMethodGet() {
        return "GET".equalsIgnoreCase(this.method);
    }

    private void extractLine1(String line1) {
        String[] split1 = line1.split("\\s");
        if (split1.length < 2) {
            System.err.println("ERROR:[" + line1+"]");
            System.err.println("ERRORbody :[" + new String(contentBytes)+"]");
            return;
        }
        method = split1[0].toUpperCase();

        String s = split1[1];
        int i = s.indexOf("?");
        if (i > -1) {
            requestUrl = s.substring(0, i);
            queryString = s.substring(i);
        } else {
            requestUrl = split1[1];
        }
    }

    public void buildHeader() {
        String ss = new String(contentBytes);
        String[] arr = ss.split(System.lineSeparator());
        extractLine1(arr[0]);
        for (int i = 1; i < arr.length; i++) {
            if (i == arr.length - 1) {
                continue;
            }
            String s = arr[i];
            String[] split = s.split(":");
            if (split.length > 1) {
                headers.put(split[0], split[1].trim());
            } else {
                System.out.println("error = " + s);
            }
        }

    }

    public static Request buildByHttpRequestBytes(byte[] bytes) {
        Request httpRequest = new Request(bytes);
        return httpRequest;
    }

    @Override
    public String toString() {
        return new String(contentBytes);
    }
}
