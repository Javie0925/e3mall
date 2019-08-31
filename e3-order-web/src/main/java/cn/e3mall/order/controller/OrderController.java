package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;

@Controller
public class OrderController {

	private static final String TbUser = null;
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/order/order-cart")
	public String showOrderPage(HttpServletRequest request){
		
		//从request中获取用户id
		TbUser user = (cn.e3mall.pojo.TbUser) request.getAttribute("user");
		//将cookie中购物车物品合并到redis
		String cookieValue = CookieUtils.getCookieValue(request, "cart", true);
		if(StringUtils.isNotBlank(cookieValue)){
			List<TbItem> jsonToList = JsonUtils.jsonToList(cookieValue, TbItem.class);
			cartService.merge(user.getId(), jsonToList);
		}
		//根据id查询购物车信息
		List<TbItem> cartList = cartService.getItemListFromRedis(user.getId());
		//将购物车信息放入request中
		request.setAttribute("cartList", cartList);
		//转发到订单页面
		return "order-cart";
	}
	
	@RequestMapping("/order/create")
	public String createOrder(OrderInfo orderInfo,HttpServletRequest request){
		
		TbUser user = (TbUser) request.getAttribute("user");
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		E3Result e3Result = orderService.createOrder(orderInfo);
		OrderInfo data = (OrderInfo) e3Result.getData();
		request.setAttribute("orderId", data.getOrderId());
		request.setAttribute("payment", orderInfo.getPayment());
		return "success";
	}
}
