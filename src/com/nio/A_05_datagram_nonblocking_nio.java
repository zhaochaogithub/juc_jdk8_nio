package com.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import org.junit.jupiter.api.Test;

public class A_05_datagram_nonblocking_nio {

	public static void main(String[] args) {
		
	}
	
	@Test
	public void send() throws Exception {
		
		DatagramChannel channel = DatagramChannel.open();
		channel.configureBlocking(false);
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put("随便发送".getBytes());
		
		buffer.flip();
		channel.send(buffer, new InetSocketAddress("127.0.0.1", 8080));
		buffer.clear();
		
		channel.close();
	}
	
	@Test
	public void receive() throws Exception {
		
		DatagramChannel channel = DatagramChannel.open();
		channel.configureBlocking(false);
		channel.bind(new InetSocketAddress(8080));
		Selector selector = Selector.open();
		channel.register(selector, SelectionKey.OP_READ);
		while(selector.select() > 0) {
			for (SelectionKey sk : selector.selectedKeys()) {
				if(sk.isReadable()) {
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					channel.receive(buffer);
					buffer.flip();
					System.out.println(new String(buffer.array(), 0, buffer.limit()));
					buffer.clear();
				}
			}
		}
		channel.close();
	}
}
