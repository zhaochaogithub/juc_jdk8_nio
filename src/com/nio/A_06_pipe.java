package com.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.Pipe.SourceChannel;

/**
 * Java NIO 管道是2个线程之间的单向数据连接。Pipe有一个source通道和一个sink通道。数据会被写到sink通道，从source通道读取
 */
public class A_06_pipe {

	public static void main(String[] args) throws IOException {
	
		Pipe pipe = Pipe.open();
		
		SinkChannel sinkChannel = pipe.sink();
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		byteBuffer.put("写入".getBytes());
		byteBuffer.flip();
		sinkChannel.write(byteBuffer);
		
		SourceChannel sourceChannel = pipe.source();
		byteBuffer.flip();
		int len = sourceChannel.read(byteBuffer);
		byteBuffer.clear();
		System.out.println(new String(byteBuffer.array(), 0, len));
		
		sinkChannel.close();
		sourceChannel.close();
	}
}
