package com.lt.backend.httpserver.service;

import com.lt.backend.httpserver.http.Request;
import com.lt.backend.httpserver.http.Response;
import com.lt.backend.httpserver.http.Service;

public class IndexService implements Service {

    @Override
    public void doGet(Request request, Response response) throws Exception {
        response.println("<html><body<h1>Welcome!</h1></body></html>");
    }

    @Override
    public void doPost(Request request, Response response) {
    }
}
