package com.juc;

/**
 * 自定义接口函数
 * 定义：接口中只有一个抽象方法
 * 
 * 1、函数接口只允许有一个非default的方法
 * 2、函数接口允许有多个default方法
 * 3、函数接口允许有多个static方法
 */
@FunctionalInterface
interface Foo {
	
	void sayHello();
	
	default void eat() {
		System.out.println("默认吃的方法");
	}
	
	default void run() {
		System.out.println("默认跑的方法");
	}
	
	public static void test() {
		System.out.println("静态方法");
	}
}

/**
 * Lambd表达式
 * 拷贝小括号	写死右箭头	落地大括号
 * @author zhaochao
 */
public class A_02_Lambda {

	public static void main(String[] args) {
		
//		接口也可以通过new的方式实现，但是需要实现里面所有的方法
//		Foo foo = new Foo() {
//			@Override
//			public void sayHello() {
//				System.out.println("说话");
//			}
//		};
//		foo.sayHello();
		
//		=====================================================
		
//		通过Lambda的方式实现接口的实例化
//		Foo foo = () -> {System.out.println("Lambda的方式说话");};
//		foo.sayHello();
		
//		=====================================================
		
//		函数式编程，在接口中可以有一个default方法，并且该方法可以进行实现
//		Foo foo = () -> {};
//		foo.eat();
//		foo.run();
		
//		静态方法
//		Foo.test();
	}
}