package com.wuqinghua.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by wuqinghua on 18/5/1.
 * <p>
 * 一、完成NIO通络通信的三个核心
 * 1.通道(Channel)
 * 2.缓冲区(Buffer)
 * 3.选择器(Selector)
 */
public class TestBlockingNIO {

    @Test
    public void client() throws IOException {
        // 1.获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        FileChannel fileChannel = FileChannel.open(Paths.get("11.jpeg"), StandardOpenOption.READ);

        ByteBuffer buf = ByteBuffer.allocate(1024);


        while (fileChannel.read(buf) != -1) {
            buf.flip();
            socketChannel.write(buf);
            buf.clear();
        }

        socketChannel.shutdownOutput(); // 告诉服务端发送完成


        //接受客户端的返回
        int len = 0;
        while ((len = socketChannel.read(buf))!=-1){
            buf.flip();
            System.out.println(new String(buf.array(),0,len));
            buf.clear();
        }

        fileChannel.close();
        socketChannel.close();
    }


    @Test
    public void server() throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9898));

        SocketChannel socketChannel = serverSocketChannel.accept();

        FileChannel fileChannel = FileChannel.open(Paths.get("out.jpeg"), StandardOpenOption
                .WRITE, StandardOpenOption.CREATE);

        ByteBuffer buf = ByteBuffer.allocate(1024);


        while (socketChannel.read(buf)!=-1){
            buf.flip();
            fileChannel.write(buf);
            buf.clear();
        }


        //发送反馈
        buf.put("服务端接受数据成功！".getBytes());
        buf.flip();
        socketChannel.write(buf);
        socketChannel.shutdownOutput();



        fileChannel.close();
        socketChannel.close();
        serverSocketChannel.close();
    }
}
