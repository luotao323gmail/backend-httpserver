package com.lt.backend.httpserver.process;

import com.lt.backend.httpserver.http.Request;
import com.lt.backend.httpserver.http.Response;
import com.lt.backend.httpserver.http.Service;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class HttpServiceCommand {



    private Semaphore isExecute = new Semaphore(1);

    private byte[] responseBytes;


    private Request request;

    private Service httpService;



    public Service getHttpService() {
        return httpService;
    }

    public HttpServiceCommand(Request request) {
        this.request = request;
    }

    public void setHttpService(Service httpService) {
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
            Response httpResponse = new Response();
            try {
                if(request.isMethodGet()){
                    httpService.doGet(request,httpResponse);
                }else{
                    httpService.doPost(request,httpResponse);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.responseBytes = httpResponse.buildResponse();
        }finally {
            isExecute.release();
        }
    }

    public byte[] getResult(){
        try {
            boolean b = isExecute.tryAcquire(2, TimeUnit.SECONDS);
            if(!b){
                System.out.println("timeout");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseBytes;
    }
}
