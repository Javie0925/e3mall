package cn.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.jedis.JedisClientPool;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private JedisClientPool jedisClientPool;
	@Value("${SESSION_EXPIRE}")
	private int SESSION_EXPIRE;
	
	@Override
	public E3Result userLogin(String username, String password) {
		
		// 1 验证用户名和密码
		 TbUserExample example = new TbUserExample();
		 Criteria criteria = example.createCriteria();
		 criteria.andUsernameEqualTo(username);
		 List<TbUser> list = tbUserMapper.selectByExample(example );
		 // 2 如果不正确，返回错误信息
		 if(list==null||list.size()==0){
			 return E3Result.build(400, "用户不存在！");
		 }
		 TbUser user = list.get(0);
		 if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
			 return E3Result.build(400, "密码错误！");
		 }
		 // 3 如果正确生成token
		 String token = UUID.randomUUID().toString();
		 // 4 把用户信息写入redis，key：token，value:用户信息
		 user.setPassword(null);
		 jedisClientPool.set("SESSION:"+token, JsonUtils.objectToJson(user));
		 // 5 设置token的过期时间
		 jedisClientPool.expire("SESSION:"+token, SESSION_EXPIRE);
		 // 6 返回token
		 
		return E3Result.ok(token);
	}

}
