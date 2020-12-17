package com.myspring.core;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClassPathXmlApplicationContext implements ApplicationContext{
    private String configFileName;

    public ClassPathXmlApplicationContext(String configFileName) {
        this.configFileName = configFileName;
    }

    public ClassPathXmlApplicationContext() {
        this.configFileName = "applicationContext.xml";
    }
    //1--解析xml文件
    public Map<String,EntityBean> springXmlParser() throws Exception {
       //创建解析器
        XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
        InputStream in = ClassPathXmlApplicationContext.class.getClassLoader().getResourceAsStream(configFileName);
        pullParser.setInput(in,"utf-8");
        int eventType = pullParser.getEventType();
        Map<String,EntityBean> beans = null;
        EntityBean bean = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    beans = new HashMap<String, EntityBean>();
                    break;
                case XmlPullParser.START_TAG:
                    if("bean".equals(pullParser.getName())){
                        bean = new EntityBean();
                        bean.setId(pullParser.getAttributeValue(null,"id"));
                        bean.setClassName(pullParser.getAttributeValue(null,"class"));
                    }
                    if("property".equals(pullParser.getName())){
                        String attrName = pullParser.getAttributeValue(null,"name");
                        String vattrVal = pullParser.getAttributeValue(null,"value");
                        bean.getProps().put(attrName,vattrVal);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    beans.put(bean.getId(),bean);
                    break;

            }
            eventType= pullParser.next();
        }

        return beans;
    }

/*    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext cpxs = new ClassPathXmlApplicationContext();
        cpxs.springXmlParser();
        Map<String,EntityBean> beans =cpxs.springXmlParser();
        for(Map.Entry<String,EntityBean> entry:beans.entrySet()){
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            System.out.println("*************");
        }
    }*/

    //2--根据解析来的信息，通过反射返程对象组装assemble
    public Map<String,Object> getIOC(Map<String ,EntityBean> beansInfo) throws Exception {
        Map<String,Object> results = new HashMap<String,Object>();
        for(Map.Entry<String,EntityBean> beanInfo:beansInfo.entrySet()){
            String resultId = beanInfo.getKey();
            EntityBean bean = beanInfo.getValue();
            String className = bean.getClassName();
            Map<String,String> props = bean.getProps();

            //反射--输入字符串，返回对象
            Class clazz = Class.forName(className);
            Object obj = clazz.newInstance();
            for(Map.Entry<String,String> prop:props.entrySet()){
                String propName = prop.getKey();
                String propValue = prop.getValue();
                StringBuilder buffer = new StringBuilder("set");
                buffer.append(propName.substring(0,1).toUpperCase());
                buffer.append(propName.substring(1));
                String setterMethodName = buffer.toString();

                Field field = clazz.getDeclaredField(propName);
                Method setMethod = clazz.getDeclaredMethod(setterMethodName,field.getType());
               // System.out.println(field.getType().getName());
                if("int".equals(field.getType().getName())){
                    setMethod.invoke(obj,Integer.parseInt(propValue));
                }else if("java.lang.String".equals(field.getType().getName())){
                    setMethod.invoke(obj,propValue);
                }
                results.put(resultId,obj);
            }
        }

        return  results;
    }
    @Override
    public Object getBean(String beanID) {
        Object result = null;

        try {
            Map<String,EntityBean> beansInfo = springXmlParser();
            Map<String, Object> ioc = getIOC(beansInfo);
            result = ioc.get(beanID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
