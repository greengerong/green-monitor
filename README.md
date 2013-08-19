build status: [![Build Status](https://travis-ci.org/greengerong/green-monitor.png)](https://travis-ci.org/greengerong/green-monitor)

===============================

Let monitor work with your project

===============================

1：Create your spring mvc(3.0） project.

2: add bean in your spring ioc config:

  ensure enable annotation driver:
  
  like this；
  
      <?xml version="1.0" encoding="UTF-8"?>
      <beans xmlns="http://www.springframework.org/schema/beans"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns:p="http://www.springframework.org/schema/p"
             xmlns:mvc="http://www.springframework.org/schema/mvc"
             xmlns:context="http://www.springframework.org/schema/context"
             xmlns:util="http://www.springframework.org/schema/util"
             xsi:schemaLocation="http://www.springframework.org/schema/beans
                   http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
                   http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd
                   http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd
                   http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-3.0.xsd">
    
          <mvc:annotation-driven/>
    
          <context:component-scan base-package="green.monitor"/>
    
          <bean class="org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter"/>
    
          <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
              <property name="prefix">
                  <value>/WEB-INF/pages/</value>
              </property>
              <property name="suffix">
                  <value>.jsp</value>
              </property>
          </bean>
      </beans>

 3: Add monitor config in your mian/resources, file name is applicationContext.xml.
 
   if you want to set config for each env, you have two way to do it.
   
   1: set your web app machine env(key:appenv), monitor will find the config monitor-config.[appenv].xml.
   
   2:use maven to compile for each env.
   
 4: depends:
 
    <dependency>
      <groupId>com.github.greengerong</groupId>
      <artifactId>green.monitor</artifactId>
      <version>1.4</version>
    </dependency>

  ===============================
  
  monitor config
  
  ===============================
  
  
  1:sample:


         <?xml version="1.0" encoding="UTF-8"?>
          <monitoring version="1.0" name="monitor-sample">
              <monitors>
                  <monitor name="mock-monitor">green.monitor.demo.MockMonitorRunner</monitor>
              </monitors>
              <items>
                  <item monitor="http-connection" name="hello service">
                      <params>
                          <param name="url">http://localhost:8080/demo/hello</param>
                          <param name="method">GET</param>
                          <param name="response-code">200</param>
                          <param name="param">name=success</param>
                      </params>
                      <description>This is a monitor for hello service.should be success.</description>
                  </item>
                  <item monitor="http-connection" name="error service 2">
                      <params>
                          <param name="url">http://localhost:8080/demo/failed</param>
                          <param name="method">GET</param>
                          <param name="response-code">200</param>
                          <param name="param">name=must be failed</param>
                      </params>
                      <description>This is a monitor for error service.should be failed.</description>
                  </item>
                  <item monitor="mock-monitor" name="Random failed Service">
                      <description>This monitor will be random failed!</description>
                  </item>
              </items>
          </monitoring>

2: you can extensions the monitor runner by yourself, and config it in  monitoring/monitors/monitor,
  like this:
  
       <monitors>
          <monitor name="mock-monitor">green.monitor.demo.MockMonitorRunner</monitor>
       </monitors>

 3: you can use add jsonp filter on you project to  use ajx jsonp call service.

    <filter>
        <filter-name>jsonp</filter-name>
        <filter-class>green.monitor.filer.JsonpFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>jsonp</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    You also can change jsonp parameter name from init param.
