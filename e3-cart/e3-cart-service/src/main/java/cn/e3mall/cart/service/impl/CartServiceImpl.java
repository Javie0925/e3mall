package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${CART_PRE_NAME}")
	private String CART_PRE_NAME;
	
	@Override
	public E3Result addItemToRedisCart(Long userId, Long itemId, Integer num) {
		//判断商品是否已经存在与redis中
		Boolean hexits = jedisClient.hexits(CART_PRE_NAME+":"+userId, itemId+"");
		if(hexits){
			//商品已经存在，增加数量
			String jsonData = jedisClient.hget(CART_PRE_NAME+":"+userId, itemId+"");
			TbItem item = JsonUtils.jsonToPojo(jsonData, TbItem.class);
			item.setNum(item.getNum()+num);
			jedisClient.hset(
					CART_PRE_NAME+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		}else{//商品不存在
			//从数据库中查找商品，转成json存入redis。
			TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
			item.setNum(num);
			//处理图片
			String images = item.getImage();
			if(images!=null&&images.contains(",")){
				String image = images.split(",")[0];
				item.setImage(image);
			}
			jedisClient.hset(CART_PRE_NAME+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		}
		return E3Result.ok();
	}

	@Override
	public List<TbItem> getItemListFromRedis(Long userId) {
		Map<String, String> hgetAll = jedisClient.hgetAll(CART_PRE_NAME+":"+userId);
		List<TbItem> itemList = new ArrayList<TbItem>();
		Set<String> keySet = hgetAll.keySet();
		for(String key:keySet){
			String str = hgetAll.get(key);
			itemList.add(JsonUtils.jsonToPojo(str, TbItem.class));
		}
		return itemList;
	}

	@Override
	public TbItem getItem(Long userId, Long itemId) {
		String hget = jedisClient.hget(CART_PRE_NAME+":"+userId, itemId+"");
		TbItem tbItem = JsonUtils.jsonToPojo(hget, TbItem.class);
		return tbItem;
	}

	@Override
	public E3Result deleteItem(Long userId, Long itemId) {
		
		jedisClient.hdel(CART_PRE_NAME+":"+userId, itemId+"");
		
		return E3Result.ok();
	}

	@Override
	public E3Result updateItemNum(Long userId, Long itemId, Integer num) {
		String string = jedisClient.hget(CART_PRE_NAME+":"+userId, itemId+"");
		TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
		item.setNum(num);
		jedisClient.hset(CART_PRE_NAME+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		return E3Result.ok();
	}

	@Override
	public void merge(Long userId, List<TbItem> itemList) {
		for(TbItem item:itemList){
			addItemToRedisCart(userId,item.getId(),item.getNum());
		}
	}

}
