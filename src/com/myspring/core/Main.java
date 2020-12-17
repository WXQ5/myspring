package com.myspring.core;



public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
        Object zhao = ctx.getBean("zhao");
        System.out.println(zhao);
        Object qian = ctx.getBean("qian");
        System.out.println(qian);
        Object sun = ctx.getBean("sun");
        System.out.println(sun);
        Object li = ctx.getBean("li");
        System.out.println(li);
    }
}
