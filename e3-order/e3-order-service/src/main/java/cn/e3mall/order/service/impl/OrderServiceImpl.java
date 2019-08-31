package cn.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;

@Service
public class OrderServiceImpl implements OrderService{

	@Value("${ORDER_INCR_KEY}")
	private String ORDER_INCR_KEY;
	@Value("${ORDER_INCR_INITIAL_VALUE}")
	private String ORDER_INCR_INITIAL_VALUE;
	@Value("${SHIPPING_INCR_KEY}")
	private String SHIPPING_INCR_KEY;
	@Value("${SHIPPING_INCR_INITIAL_VALUE}")
	private String SHIPPING_INCR_INITIAL_VALUE;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbOrderMapper tbOrderMapper;
	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;
	@Autowired
	private TbOrderShippingMapper tbOrderShippingMapper;
	
	@Override
	public E3Result createOrder(OrderInfo orderInfo) {
		
		//补全order信息
		//设置订单id
		if(!jedisClient.exists(ORDER_INCR_KEY)){
			jedisClient.set(ORDER_INCR_KEY, ORDER_INCR_INITIAL_VALUE);
		}
		String orderId = jedisClient.incr(ORDER_INCR_KEY).toString();
		orderInfo.setOrderId(orderId);
		//物流id
		if(!jedisClient.exists(SHIPPING_INCR_KEY)){
			jedisClient.set(SHIPPING_INCR_KEY, SHIPPING_INCR_INITIAL_VALUE);
		}
		String shippingCode = jedisClient.incr(SHIPPING_INCR_INITIAL_VALUE).toString();
		orderInfo.setShippingCode(shippingCode);
		//状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		//插入数据库
		tbOrderMapper.insert(orderInfo);
		//补全order_item信息,插入数据库
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		if(orderItems!=null&&orderItems.size()>0){
			for(TbOrderItem item:orderItems){
				String orderItemId = jedisClient.incr("orderItemId").toString();
				item.setId(orderItemId);
				item.setOrderId(orderId);
				tbOrderItemMapper.insert(item);
			}
		}
		//补全order_shipping信息
		TbOrderShipping shipping = orderInfo.getOrderShipping();
		if(shipping!=null){
			shipping.setOrderId(orderId);
			shipping.setCreated(new Date());
			shipping.setUpdated(new Date());
			//插入数据库
			tbOrderShippingMapper.insert(shipping);
		}
		return E3Result.ok(orderInfo);
	}

	
}
