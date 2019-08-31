package cn.e3mall.cart.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

/**
 * 购物车controller
 */
@Controller
public class CartController {

	@Value("${COOKIE_MAX_AGE}")
	private int COOKIE_MAX_AGE;
	@Autowired
	private ItemService itemService;
	@Autowired
	private CartService cartService;
	
	/**
	 * 增加购物车商品数量
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateItemNum(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//判断是否登陆
		Long userId = (Long) request.getAttribute("userId");
		if(userId!=null){
			//已经登陆
			cartService.updateItemNum(userId, itemId, num);
			return E3Result.ok();
		}
		List<TbItem> itemList = getItemListFromCookie(request,"cart");
		for(TbItem item:itemList){
			if(item.getId()==itemId.longValue()){
				item.setNum(num);
				break;
			}
		}
		String cookieValue = JsonUtils.objectToJson(itemList);
		//request.setAttribute("cartList", itemList);
		CookieUtils.setCookie(request, response, "cart", cookieValue, COOKIE_MAX_AGE, true);
		return E3Result.ok();
	}
	
	
	/**
	 * 删除购物车物品
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteItemFromCart(@PathVariable Long itemId,
			HttpServletRequest request,HttpServletResponse response){
		
		//判断是否登陆
		Long userId = (Long) request.getAttribute("userId");
		if(userId!=null){
			//已经登陆
			E3Result e3Result = cartService.deleteItem(userId, itemId);
			
			return "redirect:/cart/cart.html";
			/*try {
				response.sendRedirect("http://localhost:8090/cart/cart.html");
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
		
		//未登陆，操作cookie
		List<TbItem> itemList = getItemListFromCookie(request,"cart");
		for(TbItem item:itemList){
			if(item.getId()==itemId.longValue()){
				itemList.remove(item);
				break;
			}
		}
		String cookieValue = JsonUtils.objectToJson(itemList);
		CookieUtils.setCookie(request, response, "cart", cookieValue, COOKIE_MAX_AGE, true);
		return "redirect:/cart/cart.html";
		/*try {
			response.sendRedirect("http://localhost:8090/cart/cart.html");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		//return "cart";
	}
	
	
	/**
	 * 加载购物车方法
	 */
	@RequestMapping("/cart/cart")
	public String showCart(
			HttpServletRequest request,HttpServletResponse response,Model model){
		
		//判断是否登陆
		Long userId = (Long) request.getAttribute("userId");
		if(userId!=null){
			//已经登陆,将cookie购物车与redis购物车合并，保存到redis中
			//1.从cookie中取,并加入redis中
			List<TbItem> itemListFromCookie = getItemListFromCookie(request,"cart");
			if(itemListFromCookie.size()>0){
				for(TbItem item:itemListFromCookie){
					cartService.addItemToRedisCart(userId, item.getId(), item.getNum());
				}
				//删除cookie
				CookieUtils.deleteCookie(request, response, "cart");
			}
			
			//2.从redis中取
			List<TbItem> itemList = cartService.getItemListFromRedis(userId);
			request.setAttribute("cartList", itemList);
			return "cart";
		}
		
		//未登陆，加载cookie购物车信息
		List<TbItem> itemList = getItemListFromCookie(request,"cart");
		//处理image
		for(TbItem item:itemList){
			if(StringUtils.isNotBlank(item.getImage())&&item.getImage().contains(",")){
				String image = item.getImage().split(",")[0];
				item.setImage(image);
			}
		}
		model.addAttribute("cartList", itemList);
		return "cart";
	}
	
	
	/**
	 * 添加商品到购物车
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addItemToCart(
			@PathVariable Long itemId,@RequestParam(defaultValue="1") Integer num,
			HttpServletRequest request,HttpServletResponse response){

		//判断是否登陆
		Long userId = (Long) request.getAttribute("userId");
		if(userId!=null){
			//登陆状态，将购物车存入redis
			E3Result e3Result = cartService.addItemToRedisCart(userId, itemId, num);
			return "cartSuccess";
		}
		
		//未登陆，将商品存入cookie
		List<TbItem> itemList = getItemListFromCookie(request,"cart");
		boolean flag = false;
		if(itemList!=null&&itemList.size()>0){
			for(TbItem item:itemList){
				if(item.getId()==itemId.longValue()){
					item.setNum(item.getNum()+num.intValue());
					flag = true;
					break;
				}
			}
		}
		if(flag==false){
			TbItem item = itemService.getItemById(itemId);
			item.setNum(num);
			itemList.add(item);
		}
		String json = JsonUtils.objectToJson(itemList);
		CookieUtils.setCookie(request, response, "cart",json , COOKIE_MAX_AGE, true);
		
		return "cartSuccess";
	}
	
	/**
	 * 从cookie中获取订单信息
	 */
	public List<TbItem> getItemListFromCookie(HttpServletRequest request,String cookieName){
		String cookieValue = CookieUtils.getCookieValue(request, cookieName,true);
		if(StringUtils.isNotBlank(cookieValue)){
			List<TbItem> itemList = JsonUtils.jsonToList(cookieValue, TbItem.class);
			return itemList;
		}
		
		return new ArrayList<TbItem>();
		
	}
	
}
