package cn.e3mall.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
/**
 * 全局异常处理器
 */
public class GlobleExceptionHandler implements HandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(GlobleExceptionHandler.class);
	
	@Override
	public ModelAndView resolveException(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Object handler,
			Exception e) {
		//打印控制台
		e.printStackTrace();
		//写日志
		logger.debug("测试输出的日志。。。。");
		logger.info("系统发生异常了。。。。。");
		logger.error("系统发生异常",e);
		//发邮件使用jmall工具包，发短信使用第三方webservice
		//显示错误页面
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/exception");
		
		return modelAndView;
	}

}
