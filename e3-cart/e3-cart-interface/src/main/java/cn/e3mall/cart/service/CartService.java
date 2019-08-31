package cn.e3mall.cart.service;

import java.util.List;
import java.util.Map;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;

public interface CartService {

	E3Result addItemToRedisCart(Long userId,Long itemId,Integer num);
	public List<TbItem> getItemListFromRedis(Long userId);
	TbItem getItem(Long userId,Long itemId);
	E3Result deleteItem(Long userId,Long itemId);
	E3Result updateItemNum(Long userId,Long itemId,Integer num);
	void merge(Long userId,List<TbItem> itemList);
}
