package cn.e3mall.sso.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.LoginService;

/**
 * 登陆功能controller
 */
@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@RequestMapping("/page/login")
	public String showLoginPage(){
		return "login";
	}
	
	
	@RequestMapping("/user/login")
	@ResponseBody
	public E3Result login(String username,String password,
			HttpServletRequest request, HttpServletResponse response){
		
		E3Result e3Result = loginService.userLogin(username, password);
		//判断是否登陆成功
		if(e3Result.getStatus()==200){
			String token = (String) e3Result.getData();
			CookieUtils.setCookie(request, response, TOKEN_KEY, token);
		}
		
		return e3Result;
	}
}
