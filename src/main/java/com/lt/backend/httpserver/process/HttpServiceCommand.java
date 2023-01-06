package com.lt.backend.httpserver.process;

import com.lt.backend.httpserver.http.Request;
import com.lt.backend.httpserver.http.Response;
import com.lt.backend.httpserver.http.Service;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class HttpServiceCommand {



    private Semaphore isExecute = new Semaphore(1);

    private byte[] responseBytes;

    private byte[] requestBytes;

    private Service httpService;

    public HttpServiceCommand(byte[] requestBytes, Service httpService) {
        this.requestBytes = requestBytes;
        this.httpService = httpService;
    }

    public void lock(){
        try {
            isExecute.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void execute(){
        try{
            Request httpRequest = Request.buildByHttpRequestBytes(requestBytes);
            Response httpResponse = new Response();
            httpService.doPost(httpRequest,httpResponse);
           this.responseBytes = httpResponse.buildResponse();
        }finally {
            isExecute.release();
        }
    }

    public byte[] getResult(){
        try {
            isExecute.tryAcquire(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseBytes;
    }
}
