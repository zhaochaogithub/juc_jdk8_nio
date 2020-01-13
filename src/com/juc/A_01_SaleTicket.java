package com.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程	操作	资源类
 * 多线程卖票
 */
public class A_01_SaleTicket {

	public static void main(String[] args) {
		Ticket t = new Ticket();
		threadMethos(t);
	}
	
	public static void threadMethos(Ticket t) {
		// 线程
		new Thread(()->{
			for(int i = 0; i < 30; i++) {
				t.sale();
			}
		}, "A").start(); 
		
		new Thread(()->{
			for(int i = 0; i < 30; i++) {
				t.sale();
			}
		}, "B").start(); 
		
		new Thread(()->{
			for(int i = 0; i < 30; i++) {
				t.sale();
			}
		}, "C").start(); 
	}
}

/**
 * 资源类
 */
class Ticket {
	private int num = 30;
	Lock lock = new ReentrantLock();
	
	public void sale() {
		lock.lock();
		try {
			if(num > 0) {
				num--;
				System.out.println(Thread.currentThread().getName() + "卖出1张票，剩余" + num + "张票");
			}
		} finally {
			lock.unlock();
		}
	}
}