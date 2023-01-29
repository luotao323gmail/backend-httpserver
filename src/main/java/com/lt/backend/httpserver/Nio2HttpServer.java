package com.lt.backend.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.lt.backend.httpserver.process.HttpServiceFactory;

public class Nio2HttpServer {

    private static final int DEFAULT_PORT = 18082;
    private static final int TIMEOUT = 5000;

    private final AsynchronousServerSocketChannel server;
    private int limit;

    public Nio2HttpServer() throws IOException {
        server = AsynchronousServerSocketChannel.open();
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        server.bind(new InetSocketAddress(DEFAULT_PORT));
    }

    public void start() throws Exception {
        while (true) {
            Future<AsynchronousSocketChannel> acceptFuture = server.accept();
            AsynchronousSocketChannel asynchronousSocketChannel = acceptFuture.get();
            handleRequest(asynchronousSocketChannel);
            asynchronousSocketChannel.close();
        }
    }

    private void handleRequest(AsynchronousSocketChannel channel) {

        try (AsynchronousSocketChannel acceptedChannel = channel) {
            ByteBuffer buff = ByteBuffer.allocateDirect(8192);
            acceptedChannel.read(buff).get(TIMEOUT, TimeUnit.SECONDS);
            byte[] printRequest = printRequest(buff);
            if (printRequest == null) {  
                return;
            }
            byte[] responseBody = HttpServiceFactory.executeHttpServer(printRequest);
            acceptedChannel.write(ByteBuffer.wrap(responseBody)).get(TIMEOUT, TimeUnit.SECONDS);
           
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

    }

    private byte[] printRequest(ByteBuffer buff) {
        buff.flip();
        int limit = buff.limit();
        if (limit == 0) {
            System.out.println(3212);
            return null;
        }
        byte[] bytes = new byte[limit];
        buff.get(bytes);
        buff.compact();

        // System.out.println(new String(bytes));
        return bytes;
    }

    public static void main(String[] args) throws Exception {
        new Nio2HttpServer().start();
    }
}