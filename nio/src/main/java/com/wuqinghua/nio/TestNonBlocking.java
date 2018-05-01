package com.wuqinghua.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by wuqinghua on 18/5/1.
 */
public class TestNonBlocking {


    @Test
    public void client() throws IOException, InterruptedException {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false); //非阻塞模式

        socketChannel.connect(new InetSocketAddress("localhost", 9898));
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        while (selector.select() > 0) {
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();

            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buf = ByteBuffer.allocate(1024);

                    int len = 0;
                    while ((len = channel.read(buf)) > 0) {
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();
                    }
                    channel.register(selector, SelectionKey.OP_WRITE);
                } else if (key.isWritable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    doWrite(channel);
                    channel.register(selector, SelectionKey.OP_READ);
                } else if (key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    if (channel.finishConnect()) {
                        channel.register(selector, SelectionKey.OP_READ);
                        doWrite(channel);
                    }

                }
            }
        }

        socketChannel.close();


    }

    private void doWrite(SocketChannel socketChannel) throws IOException, InterruptedException {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        String input = new Date().toString();
        TimeUnit.SECONDS.sleep(1);
        buf.put(input.getBytes());
        buf.flip();
        socketChannel.write(buf);
    }


    @Test
    public void server() throws IOException, InterruptedException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9898));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();


        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        while (selector.select() > 0) {
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();

            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();
                if (key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);

                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    channel.configureBlocking(false);
                    ByteBuffer buf = ByteBuffer.allocate(1024);

                    int len = 0;
                    while ((len = channel.read(buf)) > 0) {
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();
                    }
                    channel.register(selector, SelectionKey.OP_WRITE);
                } else if (key.isWritable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    channel.configureBlocking(false);

                    ByteBuffer buf = ByteBuffer.allocate(1024);

                    TimeUnit.SECONDS.sleep(1);

                    buf.put("welcome".getBytes());
                    channel.write(buf);
                    channel.register(selector, SelectionKey.OP_READ);
                }

            }
        }

        serverSocketChannel.close();
    }

}
