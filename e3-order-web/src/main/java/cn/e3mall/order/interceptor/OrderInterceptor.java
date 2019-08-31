package cn.e3mall.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

public class OrderInterceptor implements HandlerInterceptor {

	@Value("${SSO_LOGIN_URL}")
	private String SSO_LOGIN_URL;
	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//拿到token
		String token = CookieUtils.getCookieValue(request, "TOKEN_KEY", true);
		//判断token是否为空
		if(StringUtils.isBlank(token)){
			//token为空，重定向到登陆界面，登陆完成重定向回订单界面
			response.sendRedirect(SSO_LOGIN_URL+"/page/login?redirect="+request.getRequestURL());
			return false;
		}
		//token不为空，比对redis中用户信息
		E3Result e3Result = tokenService.getUserByToken(token);
		if(e3Result.getStatus()!=200){
			//用户信息过期，重定向登陆界面
			response.sendRedirect(SSO_LOGIN_URL+"/page/login?redirect="+request.getRequestURL());
			return false;
		}
		//验证成功，将用户信息存入request，放行
		TbUser user = (TbUser) e3Result.getData();
		request.setAttribute("user", user);
		return true;
	}
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
	}


}
