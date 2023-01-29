package com.lt.backend.httpserver.process;

import com.lt.backend.httpserver.http.*;
import com.lt.backend.httpserver.service.AjaxService;
import com.lt.backend.httpserver.service.LoginService;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class HttpServiceFactory {

    public static final ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);

    public static final Map<String, Service> serviceMap = findService();

    public static byte[] executeHttpServer(byte[] httpRequestBytes) {
        Request request = Request.buildByHttpRequestBytes(httpRequestBytes);
        String keyUrl = request.getKeyUrl();
        Service service = serviceMap.get(keyUrl);
        if (service != null) {
            HttpServiceCommand httpServiceCommand = new HttpServiceCommand(request);
            httpServiceCommand.setHttpService(service);
            httpServiceCommand.lock();
            threadPoolExecutor.execute(httpServiceCommand::execute);
            byte[] result = httpServiceCommand.getResult();
            if (result == null) {
                return new ResponseTimeout().buildResponse();
            }
            return result;
        } else {
            return new Response404().buildResponse();
        }
    }

    private static Map<String, Service> findService() {
        Map<String, Service> result = new LinkedHashMap<>();
        Properties properties = new Properties();

        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("servlets.properties"));
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();
                Class<?> aClass = Class.forName(value);
                Constructor<?> constructor = aClass.getConstructor();
                Object o = constructor.newInstance();
                if (o instanceof Service) {
                    result.put(key, (Service) o);
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return result;
    }

    private static void handleException(Throwable e) {
        e.printStackTrace();
        System.exit(1);
    }

    private static Service findService(Request request) {
        if (request.getRequestUrl().equals("/ajax")) {
            return new AjaxService();
        } else {
            return new LoginService();
        }
    }
}
