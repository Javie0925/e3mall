package cn.e3mall.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.jedis.JedisClient;

public class JedisClientTest {
	
	@Test
	public void testJedisClient(){
		//初始化spring容器
		ApplicationContext applicationContenxt = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//从容器中获取jedisclient对象
		JedisClient jedisClient = applicationContenxt.getBean(JedisClient.class);
		jedisClient.set("test", "test");
		String string = jedisClient.get("test");
		System.out.println(string);
	}
}
