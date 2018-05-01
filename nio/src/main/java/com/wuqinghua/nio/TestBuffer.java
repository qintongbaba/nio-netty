package com.wuqinghua.nio;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by wuqinghua on 18/5/1.
 *
 * 一、 Buffer(缓冲区)：在Java NIO中负责数据的存取。缓冲区就是数组。用于存储不同数据类型的数据
 *
 * 根据数据类型不同（boolean除外），都提供了相应的缓冲区
 *
 * ByteBuffer
 * CharBuffer
 * ShortBuffer
 * IntBuffer
 * LongBuffer
 * FloatBuffer
 * DoubleBuffer
 *
 * 上述的缓冲区的管理方式几乎一致，通过allocate()获取缓冲区
 *
 *
 * 二、缓冲区存取数据的两个核心方法
 *  put()  : 存入数据到缓冲区中
 *  get()  : 获取缓冲区中的数据
 *
 *
 * 三、缓冲区中的4个核心属性
 *  capacity  : 容量，表示缓冲区中最大存取数据的容量。一旦声明不能改变。
 *  limit     : 界限，表示缓冲区可以操作数据的大小。（limit后数据不能读写的）
 *  position  : 位置，表示缓冲区下一个可操作的数据的位置
 *
 *  position<=limit<=capacity
 *
 *  mark : 标记，可以记录当前position的位置。可以通过reset()恢复到mark的位置
 *
 *  0<=mark<=position<=limit<=capacity
 *
 *
 *  四、直接缓冲区与非直接缓冲区
 *
 *  非直接缓冲区：通过allocate()方法分配缓冲区，将缓冲区建立在JVM的内存中
 *  直接缓冲区 ： 通过allocateDirect()方法分配直接缓冲区，将缓冲区建立在物理内存中。可以提高效率
 *
 *
 *
 */
public class TestBuffer {


    @Test
    public void test02(){
        String str = "abcde";
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put(str.getBytes());


        buf.flip();

        byte[] dst = new byte[buf.limit()];
        buf.get(dst,0,2);
        System.out.println("-----------------get()-----------------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        buf.mark();
        buf.get(dst,0,2);
        System.out.println("-----------------get()-----------------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        buf.reset();
        System.out.println("-----------------reset()-----------------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());



    }



    @Test
    public void  test01(){

        String str = "abcde";

        //1. 分配一个指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        System.out.println("-----------------allocate()-----------------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());


        //2. 利用put方法存入数据到缓冲区中
        buf.put(str.getBytes());
        System.out.println("-----------------put()-----------------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //3. 利用flip()进行读写切换
        buf.flip();
        System.out.println("-----------------flip()-----------------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        // 4.利用get进行读取缓冲区中的数据
        System.out.println("-----------------get()-----------------------");
        byte[] bytes = new byte[buf.limit()];
        buf.get(bytes);
        System.out.println(new String(bytes,0,bytes.length));
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());


        //5.rewind()  重读模式
        buf.rewind();
        System.out.println("-----------------rewind()-----------------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());


        //6.clear   清空缓冲区
        buf.clear();
        System.out.println("-----------------clear()-----------------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());
    }




}
