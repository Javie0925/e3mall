package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

public class CartInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;
	
	/**
	 * 调用handler之前
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		
		String token = CookieUtils.getCookieValue(request, "TOKEN_KEY", true);
		//System.out.println("$$$$$$$$$$$"+token+"$$$$$$$$$$$");
		if(StringUtils.isBlank(token)){
			//未登录
			return true;
		}else{
			//已登录，检验是否过期
			E3Result e3Result = tokenService.getUserByToken(token);
			if(e3Result.getStatus()!=200){
				//token过期
				return true;
			}else{
				//token未过期，将登陆信息放入request中
				TbUser user = (TbUser)e3Result.getData();
				request.setAttribute("userId",user.getId());
				return true;
			}
		}
	}
	
	/**
	 * 返回modelandview之后
	 */
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	/**
	 * 调用handler之后，返回modelandview之前
	 */
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
	}


}
