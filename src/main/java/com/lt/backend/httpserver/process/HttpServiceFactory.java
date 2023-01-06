package com.lt.backend.httpserver.process;

import com.lt.backend.httpserver.http.LoginService;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class HttpServiceFactory {

    public static final ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);

    public static byte[] executeHttpServer(byte[] httpRequestBytes){
        HttpServiceCommand httpServiceCommand = new HttpServiceCommand(httpRequestBytes,new LoginService());
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
