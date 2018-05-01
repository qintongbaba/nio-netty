package com.wuqinghua.nio;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by wuqinghua on 18/5/1.
 * <p>
 * <p>
 * 一、通道(Channel):用于源节点与目标节点的链接。在NIO中负责缓冲区的数据传输。Channel本身不存取数据，需要配合缓冲区使用
 * <p>
 * <p>
 * 二、通道的主要实现类
 * java.nio.channels.Channel
 * |-- FileChannel
 * |-- SocketChannel
 * |-- ServerSocketChannel
 * |-- DatagramChannel
 * <p>
 * 三、获取通道
 * 1. Java针对支持通道的类提供了getChannel()方法
 * FileInputStream / FileOutputSteam
 * RandomAccessFile
 * Socket
 * ServerSocket
 * DatagramSocket
 * <p>
 * 2. 针对各个通道提供了一个静态方法open
 * <p>
 * 3. 通过Files工具类newByteChannel()
 *
 * 四、通道之间的传输
 *
 * transferTo()
 * transferFrom()
 */
public class TestChannel {



    // 通道之间的
    @Test
    public void test03() throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("11.jpeg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("out.jpeg"), StandardOpenOption
                .WRITE, StandardOpenOption.CREATE);

        inChannel.transferTo(0,inChannel.size(),outChannel);
        outChannel.close();
        inChannel.close();
    }




    //使用直接缓冲区完成文件的复制
    @Test
    public void test02() throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("11.jpeg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("out.jpeg"), StandardOpenOption
                .WRITE, StandardOpenOption.CREATE);

        MappedByteBuffer buf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());

        outChannel.write(buf);


        outChannel.close();
        inChannel.close();
    }


    // 利用通道完成文件的复制
    @Test
    public void test01() throws IOException {
        FileInputStream in = new FileInputStream(new File("11.jpeg"));
        FileOutputStream out = new FileOutputStream(new File("out.jpeg"));

        // 获取通道
        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();


        // 数据的传输
        ByteBuffer buf = ByteBuffer.allocate(1024);

        // 读写数据
        int readCount = inChannel.read(buf);
        while (readCount != -1) {

            buf.flip();
            outChannel.write(buf);

            buf.clear();
            readCount = inChannel.read(buf);
        }

        outChannel.close();
        inChannel.close();

        out.close();
        in.close();
    }
}
