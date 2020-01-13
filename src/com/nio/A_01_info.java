package com.nio;

import java.nio.ByteBuffer;

/**
 * NIO与IO联系和区别 ：
 * 1.NIO面向缓冲区				IO面向流
 * 2.NIO是非阻塞式IO			IO是阻塞式IO
 * 3.NIO是双向的				IO是单项的
 * 4.NIO有一个Selectors选择器	无
 */
public class A_01_info {
	
	/**
	 * 缓冲区分类 :
	 * ByteBuffer	CharBuffer	ShortBuffer		IntBuffer
	 * LongBuffer	FloatFbuuer	DoubleBuffer
	 * 
	 * 获取缓冲区对象的方法是XXXBuffer.allocate(size)或者XXXBuffer.allocateDirect(size);
	 * allocate获取的是JVM内存里面的缓冲区(非直接缓冲区)，allocateDirect获取的是系统(内存)里面的缓冲区（即JVM内存之外的，直接缓冲区）
	 * 
	 * 拥有的方法：
	 * put() : 放入缓冲区数据
	 * get() : 获取缓冲区数据
	 * capacity() : 获取当前缓冲区的容量
	 * limit() : 获取当前缓冲区可操作的容量大小
	 * position() : 获取当前指针的位置
	 * flip() : 切换模式（读-->写, 写-->读）
	 * clear() : 用于写模式，所谓清空是指设置limit==capacity，同时将当前写位置position置为0，它不会清空数据
	 * rewind() : 在读写模式下都可用，它单纯的将当前位置position置0，同时取消mark标记
	 * mark() : 标记当前位置的position并记录下来，当调用reset()方法的时候会将position设置为刚才mark的值
	 * reset() : 恢复到刚才mark()的position位置
	 * 
	 * position <= limit <= capacity
	 */
	public static void main(String[] args) {
		ByteBuffer buf = ByteBuffer.allocate(1024);
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
		
		System.out.println("---------put()-----------");
		buf.put("a".getBytes());
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
		
		System.out.println("---------flip()-----------");
		buf.flip();
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
		
		System.out.println("---------get()-----------");
		byte b = buf.get();
		System.out.println((char)b);
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
		
		System.out.println("---------rewind()-----------");
		buf.rewind();
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
		
		System.out.println("---------clear()-----------");
		buf.clear();
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
		
		System.out.println("---------mark()-----------");
		mark_Reset();
	}
	
	public static void mark_Reset() {
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.put("abcdef".getBytes());
		buf.flip();
		buf.get();
		System.out.println(buf.position());
		buf.mark();
		buf.get();
		System.out.println(buf.position());
		buf.reset();
		System.out.println(buf.position());
	}
}