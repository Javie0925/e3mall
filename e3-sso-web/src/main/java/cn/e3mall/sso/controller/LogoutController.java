package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.common.utils.CookieUtils;

@Controller
public class LogoutController {

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@RequestMapping("/user/logout")
	public String logout(
			HttpServletRequest request,HttpServletResponse response){

		CookieUtils.deleteCookie(request, response, TOKEN_KEY);
		return "login";
	}
}
