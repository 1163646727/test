package org.springframework;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 注入测试<BR>
 * author: ChenQi <BR>
 * createDate: 2021/2/26 <BR>
 */
public class SpringTest {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-common.xml",
				"application-beans.xml","application-jdbc.xml");

		Object obj = applicationContext.getBean("member");
	}
}
