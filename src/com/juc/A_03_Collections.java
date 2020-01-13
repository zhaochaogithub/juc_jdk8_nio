package com.juc;

/**
 * 集合的不安全
 * @author zhaochao
 * @Desc ConcurrentModificationException 并发修改异常
 */
public class A_03_Collections {

	public static void main(String[] args) {
//		list();
//		set();
	}
	
	public static void set() {
		
//		HashSet是线程不安全的，它的内部用的是HashMap，HashSet在进行add值的时候会调用HashMap的put方法
//		这个put方法会把接收HashSet的值作为Key，Value使用的是一个new Object()对象来存储
		
//		Set<String> set = Collections.synchronizedSet(new HashSet<>());
		
//		CopyOnWriteArraySet在new对象的时候调用的是一下代码：
//		public CopyOnWriteArraySet() {
//	        al = new CopyOnWriteArrayList<E>();
//	    }
//		内部还是使用的CopyOnWriteArrayList来完成的
//		Set<String> set = new CopyOnWriteArraySet<>();
		
	}
	
	public static void list() {
		
//		ArrayList是不安全的，它初始值默认是0，进行add操作的时候默认给10，每次扩容为原来的一半（舍去小数部分）
		
//		Vector整个方法上面采用Synchronized
//		List<String> list = new Vector<>();
		
//		synchronizedList是在方法的内部采用Synchronized
//		List<String> list = Collections.synchronizedList(new ArrayList<>());
		
//		CopyOnWriteArrayList内部采用写时加锁（add的时候使用的ReentrantLock加锁，加锁之后复制一份数据，然后扩容1
//		再把新数据放回去并返回），使用的是读写分离的思想
//		List<String> list = new CopyOnWriteArrayList<String>();
	}
}