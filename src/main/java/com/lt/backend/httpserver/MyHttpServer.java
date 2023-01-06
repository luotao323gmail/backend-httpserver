package com.lt.backend.httpserver;

import com.lt.backend.httpserver.http.Response;
import com.lt.backend.httpserver.process.HttpServiceFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


/**
 * 一个最简单的HTTP服务器。 <br>
 * 不管浏览器请求什么，都返回Hello, world
 */
public class MyHttpServer {
    public static final int DEFAULT_PORT = 28080;

    public static final String CRLF = "\r\n";


    public void exeute() throws IOException {
        ServerSocketChannel serverChannel = null;
        Selector selector = null;
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(DEFAULT_PORT));
            serverChannel.configureBlocking(false);
            selector = Selector.open();
            // 只需要注册ACCEPT操作
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            int n = selector.select();
            if (n < 1) {
                continue;
            }

            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                try {
                    if (key.isAcceptable()) {
                        handleAccetable(key, selector);
                    }
                    if (key.isReadable()) {
                        handleReadable(key);
                        // 重要！！ 将SelectionKey切换为写模式123
                        key.interestOps(SelectionKey.OP_WRITE);
                    }
                    if (key.isWritable()) {
                        handleWritable(key);
                        // 处理完响应操作，才能关掉客户端的SocketChannel
                        key.channel().close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    key.cancel();
                }
            }
        }
    }

    private void handleAccetable(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();
        InetSocketAddress socketAddress = (InetSocketAddress) client.getRemoteAddress();
        String ip = socketAddress.getAddress().getHostAddress();
//        logger.info("Accepted connection from " + ip + ":" + socketAddress.getPort());
        client.configureBlocking(false);
        // 只需要注册READ操作
        SelectionKey clientKey = client.register(selector, SelectionKey.OP_READ);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        clientKey.attach(byteBuffer);
    }

    private void handleReadable(SelectionKey key) throws IOException {
//        logger.info("handleReadable");

        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
        byteBuffer.rewind();
        int n = client.read(byteBuffer);
        if (n < 1) {
            return;
        }
        // 打印HTTP请求报文出来
        byteBuffer.flip();
        byte[] array = byteBuffer.array();
        byte[] responseBody = HttpServiceFactory.executeHttpServer(array);
       key.attach(responseBody);
    }

    private void handleWritable(SelectionKey key) throws IOException {
//        logger.info("handleWritable");
        SocketChannel client = (SocketChannel) key.channel();
        client.configureBlocking(false);
        Object response = key.attachment();

        if (response instanceof byte[]) {
            byte[] responseBytes = (byte[]) response;
            ByteBuffer out = ByteBuffer.allocate(responseBytes.length);
            out.put(responseBytes);
            out.flip();
            client.write(out);
        }

    }

    public static void main(String[] args) throws Exception {
        new MyHttpServer().exeute();
        System.in.read();
    }
}
