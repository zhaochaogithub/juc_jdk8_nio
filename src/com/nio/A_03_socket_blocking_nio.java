package com.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.jupiter.api.Test;

/**
 * 完成NIO的传输的三要素：
 * 1.通道（Channel）：负责连接
 * 	|--SelectableChannel
 * 		|--SocketChannel
 * 		|--ServerSocketChannel
 * 		|--DatagramChannel
 * 
 * 		|--Pipe.SinkChannel
 * 		|--Pipe.SourceChannel
 * 2.缓冲区（Buffer）：负责存取数据
 * 
 * 3.选择器（Selector）：是SelectableChannel的多路复用器，用于监控SelectableChannel的 IO状况
 */
public class A_03_socket_blocking_nio {

	public static void main(String[] args) {
		
	}	
	
//	阻塞式NIO
	/**
	 * 基于Socket的TCP/IP传输的Client端
	 */
	@Test
	public void client() throws Exception {
		/*
		 * 1.获取通道
		 * 2.分配缓冲区
		 * 3.读写数据
		 */
//		1.获取通道
		SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
		FileChannel fileChannel = FileChannel.open(Paths.get("E:/1.avi"), StandardOpenOption.READ);
//		2.分配缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
//		3.将通道中的数据放入到缓冲区，然后在用socket将缓冲区的数据发送到服务端
		while(fileChannel.read(buffer) != -1) {
			buffer.flip();// 将模式切换为写出操作(即：读缓冲区数据模式)
			socketChannel.write(buffer);
			buffer.clear();// 每次操作之后把position值置为0，limit置为容器最大值
		}
		fileChannel.close();
		socketChannel.close();
	}
	
	@Test
	public void server() throws Exception {
		
//		1.获取通道
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		
//		2.分配缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
//		3.绑定地址
		serverSocketChannel.bind(new InetSocketAddress(8080));
		
//		4.接收数据
		SocketChannel socketChannel = serverSocketChannel.accept();
		
//		5.保存接收到的数据
		FileChannel fileChannel = FileChannel.open(Paths.get("E:/demo.avi"), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
		
		while(socketChannel.read(buffer) != -1) {
			buffer.flip();
			fileChannel.write(buffer);
			buffer.clear();
		}
		fileChannel.close();
		socketChannel.close();
		serverSocketChannel.close();
	}
}
