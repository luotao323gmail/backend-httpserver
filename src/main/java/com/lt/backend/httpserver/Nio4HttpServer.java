package com.lt.backend.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.Consumer;

import com.lt.backend.httpserver.process.HttpServiceFactory;

public class Nio4HttpServer {

    private static final int DEFAULT_PORT = 80;

    public static void server(Consumer<Throwable> error) throws IOException {
        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        server.bind(new InetSocketAddress(DEFAULT_PORT));

        server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel socket, Void attachment) {
                server.accept(null, this);
                ByteBuffer buffer = ByteBuffer.allocate(2048);
                socket.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer attachment) {

                        if (result < 0) {
                            closeSocket(socket,error);
                            return;
                        }
                        byte[] request = new byte[result];
                        buffer.flip();
                        buffer.get(request);
                        byte[] responseBody = HttpServiceFactory.executeHttpServer(request);
                        ByteBuffer out = ByteBuffer.wrap(responseBody);
                        socket.write(out, out, new CompletionHandler<Integer, ByteBuffer>() {
                            @Override
                            public void completed(Integer result, ByteBuffer attachment) {
                                if (result == 0) {
                                    error.accept(new IOException("Write Error"));
                                    return;
                                }
                                closeSocket(socket,error);
                            }

                            @Override
                            public void failed(Throwable exc, ByteBuffer attachment) {
                                error.accept(exc);
                            }
                        });
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        error.accept(exc);
                    }
                });
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                error.accept(exc);
            }
        });


    }

    private static void closeSocket(AsynchronousSocketChannel socket,Consumer<Throwable> error){
        try {
            socket.close();
        } catch (IOException e) {
            error.accept(e);
        }
    }

    public static void main(String[] args) throws Exception {
        server(Throwable::printStackTrace);
        while (true) {
            Thread.sleep(1);
        }
    }
}