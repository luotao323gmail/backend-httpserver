package com.lt.backend.httpserver.http;

public class LoginService implements Service{
    @Override
    public void doPost(Request request, Response response) {
        response.println("<html><body>Welcome!</body></html>");
    }
}
