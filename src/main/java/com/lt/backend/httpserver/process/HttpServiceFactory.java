package com.lt.backend.httpserver.process;

import com.lt.backend.httpserver.http.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class HttpServiceFactory {

    public static final ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);

    public static byte[] executeHttpServer(byte[] httpRequestBytes) {
        Request request = Request.buildByHttpRequestBytes(httpRequestBytes);
        HttpServiceCommand httpServiceCommand = new HttpServiceCommand(request);
        httpServiceCommand.setHttpService(findService(request));
        httpServiceCommand.lock();
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                httpServiceCommand.execute();
            }
        });
        byte[] result = httpServiceCommand.getResult();
        if(result==null){
            return new ResponseTimeout().buildResponse();
        }
        return result;
    }

    private static Service findService(Request request) {
        if (request.getRequestUrl().equals("/ajax")) {
            return new AjaxService();
        } else {
            return new LoginService();
        }
    }
}
