package com.lt.backend.httpserver;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.lt.backend.httpserver.process.HttpServiceFactory;

public class Nio3HttpServer {
    
    private static final int DEFAULT_PORT = 18083;
    private static final int TIMEOUT = 5000;
    
    private final AsynchronousServerSocketChannel server;
    
    public Nio3HttpServer() throws IOException {
        server = AsynchronousServerSocketChannel.open();
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        server.bind(new InetSocketAddress(DEFAULT_PORT));
    }
    
    public void start() throws Exception {
        while(true) {
            server.accept(null, 
            new CompletionHandler<AsynchronousSocketChannel, Void>() {
            
                public void completed(AsynchronousSocketChannel ch, Void att) {
                    // server.accept(null, this);
                    handleRequest(ch);
                    try {
                        ch.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                public void failed(Throwable exc, Void att) {
                    exc.printStackTrace();
                }
            });
            // Future<AsynchronousSocketChannel> acceptFuture = server.accept();
            // handleRequest(acceptFuture.get());
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
    
    private byte[] printRequest(ByteBuffer buff){
        buff.flip();
        byte[] bytes = new byte[buff.limit()];
        buff.get(bytes);
        buff.compact();

        // System.out.println(new String(bytes));
        return bytes;
    }
    

    public static void main(String[] args) throws Exception {
        new Nio3HttpServer().start();
    }
}