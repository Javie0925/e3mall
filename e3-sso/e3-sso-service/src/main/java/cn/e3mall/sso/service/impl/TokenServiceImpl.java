package cn.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClientPool;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;


@Service
public class TokenServiceImpl implements TokenService {

	@Value("${SESSION_EXPIRE}")
	private int SESSION_EXPIRE;
	@Autowired
	private JedisClientPool jedisClientPool;
	
	@Override
	public E3Result getUserByToken(String token) {
		
		String json = jedisClientPool.get("SESSION:"+token);
		if (StringUtils.isBlank(json)) {
			return E3Result.build(400, "您尚未登陆或登陆信息已过期，请登陆！");
		}
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		jedisClientPool.expire("SESSION:"+token, SESSION_EXPIRE);
		return E3Result.ok(user);
	}

}
