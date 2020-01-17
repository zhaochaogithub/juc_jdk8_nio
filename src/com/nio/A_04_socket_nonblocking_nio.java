package com.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

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
public class A_04_socket_nonblocking_nio {

	public static void main(String[] args) {
		
	}
	
//	阻塞式IO (第2种：使用Selector)
	@Test
	public void client2() throws Exception {
		//1.获取通道
		SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
		//2.切换到非阻塞模式
		socketChannel.configureBlocking(false);
		//3.分配缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		//4.读写数据进行发送
		buffer.put("你好".getBytes());
		buffer.flip();
		socketChannel.write(buffer);
		buffer.clear();
		socketChannel.close();
	}
	
	@Test
	public void server2() throws Exception {
		//1.获取通道
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		//2.切换到非阻塞模式
		serverSocketChannel.configureBlocking(false);
		//3.绑定连接
		serverSocketChannel.bind(new InetSocketAddress(8080));
		//4.获取选择器
		Selector selector = Selector.open();
		//5.将通道注册到选择器上，并监听接收事件
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		//6.通过选择器轮询获取已经准备就绪的事件
		while(selector.select() > 0) {
			for (SelectionKey key : selector.selectedKeys()) {
				if(key.isAcceptable()) {
					//7.获取客户端连接
					SocketChannel socketChannel = serverSocketChannel.accept();
					socketChannel.configureBlocking(false);
					//8.将通道注册到选择器上
					socketChannel.register(selector, SelectionKey.OP_READ);
				}else if(key.isReadable()) {
					//9.获取读就绪的通道
					SocketChannel socketChannel = (SocketChannel) key.channel();
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					int len = 0;
					while((len = socketChannel.read(buffer)) != -1) {
						buffer.flip();
						System.out.println(new String(buffer.array(), 0, len));
						buffer.clear();
					}
				}
				
			}
		}
	}

}
