package com.lt.backend.httpserver.process;

import com.lt.backend.httpserver.http.AjaxService;
import com.lt.backend.httpserver.http.LoginService;
import com.lt.backend.httpserver.http.Request;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class HttpServiceFactory {

    public static final ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);

    public static byte[] executeHttpServer(byte[] httpRequestBytes){
        Request request = Request.buildByHttpRequestBytes(httpRequestBytes);
        HttpServiceCommand httpServiceCommand = new HttpServiceCommand(request);
        if(request.getRequestUrl().equals("/ajax")){
            httpServiceCommand.setHttpService(new AjaxService());
        }else{
            httpServiceCommand.setHttpService(new LoginService());
        }

        httpServiceCommand.lock();
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                httpServiceCommand.execute();
            }
        });
        byte[] result = httpServiceCommand.getResult();
        return result;

    }


}
