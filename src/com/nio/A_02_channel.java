package com.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Channel的介绍及使用
 */
public class A_02_channel {

	/**
	 * Channel(通道) ： 用于源节点与目标节点进行连接的，它负责NIO中缓冲区数据的传输，本身不存储数据，它和Buffer一起使用
	 * 
	 * java.nio.channels.Channel接口对应的实现类：
	 * 	|--FileCHannel
	 * 	|--SocketCHannel
	 *	|--ServerSocketChannel
	 *	|--DatagramChannel
	 * 
	 * 获取Channel的方法：
	 * 1.getChannl()
	 * 	本地IO：
	 * 		FileInputStream/FileOutputStream	返回的FileChannel
	 * 		RandomAccessFile ： 可以直接跳到文件的任意位置来读写数据。 例如断点传输
	 *  网络IO：
	 *  Socket
	 *  ServerSocket
	 *  DatagramSocket
	 * 2.XXXChannel的静态方法open()
	 * 3.Files工具类的newByteChannel()
	 */
	public static void main(String[] args) throws Exception {
		test5();
	}
	
	/**
	 * Charset编码与解码
	 */
	public static void test5() throws Exception {
//		Map<String, Charset> map = Charset.availableCharsets();
//		for (String key : map.keySet()) {
//			System.out.println(key + "=" + map.get(key));
//		}
		
		CharBuffer charBuffer = CharBuffer.allocate(1024);
		charBuffer.put("测试编码与解码");// 写入缓冲区数据
		
		Charset charset = Charset.forName("GBK");
		
		//切换模式，从缓冲区读取数据（写入多少，读取多少）
		charBuffer.flip();
		//编码
		CharsetEncoder encoder = charset.newEncoder();
		ByteBuffer byteBuffer = encoder.encode(charBuffer);
		byteBuffer.clear(); //重置所有值，目的是为了可以读取的时候操作所有的值
		
		//解码
		CharsetDecoder decoder = charset.newDecoder();
		CharBuffer charBuffer2 = decoder.decode(byteBuffer);
		System.out.println(charBuffer2.toString());
	}
	
	/**
	 * 分散（scatter）与聚集（gather）
	 * 分散：将通道中的数据分散到各个缓冲区当中
	 * 聚集：将各个缓冲区中的数据聚集到通道中
	 */
	public static void test4() throws Exception {
//		1.获取通道
		RandomAccessFile inAccessFile = new RandomAccessFile("E:/1.avi", "rw");
		FileChannel inFileChannel = inAccessFile.getChannel();
		RandomAccessFile outAccessFile = new RandomAccessFile("E:/3.avi", "rw");
		FileChannel outFileChannel = outAccessFile.getChannel();
		
//		2.将通道中的数据分散的数据到各个缓冲区
//		 2.1创建多个缓冲区
		int limit = (int)(inAccessFile.length() / 2);
		ByteBuffer byteBuffer1 = ByteBuffer.allocate(limit);
		ByteBuffer byteBuffer2 = ByteBuffer.allocate(limit + (int)(inAccessFile.length() % 2));
//		 2.2分散到各个缓冲区域
		ByteBuffer[] bufs = new ByteBuffer[] {byteBuffer1, byteBuffer2};
		inFileChannel.read(bufs);
		
//		3.聚合各个缓冲区的数据到通道中并写出
//		 3.1切换数据模式为写
		for (ByteBuffer byteBuffer : bufs) {
			byteBuffer.flip();
		}
		outFileChannel.write(bufs);
		
		inFileChannel.close();
		outFileChannel.close();
		inAccessFile.close();
		outAccessFile.close();
	}
	
	/**
	 * 直接缓冲区
	 * TransferFrom()
	 * TransferTo()
	 */
	public static void test3() throws Exception {
//		1.获取通道
		FileChannel inChannel = FileChannel.open(Paths.get("E:/1.avi"), StandardOpenOption.READ);
//		StandardOpenOption.CREATE_NEW : 存在了会报异常
//		StandardOpenOption.CREATE     : 存在了会覆盖，不存在则新增
		FileChannel outChannel = FileChannel.open(Paths.get("E:/2.avi"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
		
//		2.读写数据
//		inChannel.transferTo(0, inChannel.size(), outChannel);
//		或者
		outChannel.transferFrom(inChannel, 0, inChannel.size());
		
		inChannel.close();
		outChannel.close();
	}
	
	/**
	 * Channel的open()方法，内存映射文件，直接缓冲区操作
	 */
	public static void test2() throws Exception {
//		ByteBuffer buf = ByteBuffer.allocateDirect(1024);
//		1.获取通道
		FileChannel inChannel = FileChannel.open(Paths.get("E:/1.avi"), StandardOpenOption.READ);
//		StandardOpenOption.CREATE_NEW : 存在了会报异常
//		StandardOpenOption.CREATE     : 存在了会覆盖，不存在则新增
		FileChannel outChannel = FileChannel.open(Paths.get("E:/2.avi"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
		
//		2.打开通道
		MappedByteBuffer inMappedByteBuffer = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
		MappedByteBuffer outMappedByteBuffer = outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());
		
//		3.通过通道读写数据
		byte[] b = new byte[inMappedByteBuffer.limit()];
		inMappedByteBuffer.get(b);
		outMappedByteBuffer.put(b);
		
		inChannel.close();
		outChannel.close();
	}
	
	/**
	 * FileInputStream/FileOutputStream 模式的getChannel()获取通道，进行非直接缓冲区操作
	 */
	public static void test1() throws Exception {
		FileInputStream in = new FileInputStream("E:/IP地址.bmp");
		FileOutputStream out = new FileOutputStream("E:/IP地址2.bmp");
		
		// 获取通道
		FileChannel inChannel = in.getChannel();
		FileChannel outChannel = out.getChannel();
		
		// 分配非直接缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		while(inChannel.read(buf) != -1) {
			// 刚开始是对缓冲区buf写数据，因此buf是写的模式，现在要切换到读的模式
			buf.flip();
			// 开始往outChannel里面写数据，因此针对buf是读的操作
			outChannel.write(buf);
			// 置0，并重新进行写入数据和读取数据
			buf.clear();
		}
		inChannel.close();
		outChannel.close();
		in.close();
		out.close();
	}
}
