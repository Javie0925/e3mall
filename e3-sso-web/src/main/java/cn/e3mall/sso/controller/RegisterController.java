package cn.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.RegisterService;

/**
 * 注册功能controller
 */
@Controller
public class RegisterController {
	
	@Autowired
	private RegisterService registerService;
	
	@RequestMapping("/page/register")
	public String register(){
		return "register";
	}
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkDate(@PathVariable String param,@PathVariable Integer type){
		E3Result e3Result = registerService.checkDate(param, type);
		return e3Result;
	}
	
	@RequestMapping("/user/register")
	@ResponseBody
	public E3Result register(TbUser tbUser){
		
		E3Result e3Result = registerService.createUser(tbUser);
		
		return e3Result;
	}
}
