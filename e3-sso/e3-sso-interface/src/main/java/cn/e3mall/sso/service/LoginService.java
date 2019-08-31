package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

public interface LoginService {

	/**
	 * 参数：用户名，mima
	 * 业务逻辑：
	 * 1 验证用户名和密码
	 * 2 如果不正确，返回错误信息
	 * 3 如果正确生成token
	 * 4 把用户信息写入redis，key：token，value:用户信息
	 * 5 设置token的过期时间
	 * 6 返回token
	 */
	public E3Result userLogin(String username,String password);
}
