package cn.e3mall.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.pojo.ItemQueryVo;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId){
		
		TbItem item = itemService.getItemById(itemId);
		
		return item;
	}
	
	@RequestMapping("/item/list")
	public @ResponseBody EasyUIDataGridResult getItemList(Integer page,Integer rows){
		
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}
	
	/**
	 * 商品添加方法
	 */
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem tbItem,String desc){
		
		E3Result e3Result = itemService.addItem(tbItem, desc);
		return e3Result;
	}
	
	/**
	 * 加载itemDesc
	 */
	@RequestMapping("/rest/item/query/item/desc/{itemId}")
	@ResponseBody
	public Map loadItemDesc(@PathVariable Long itemId){
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		Map result = new HashMap();
		TbItemDesc _itemDesc = itemService.loadItemDesc(itemId);
		String itemDesc = _itemDesc.getItemDesc();
		Map data = new HashMap();
		data.put("itemDesc", itemDesc);
		result.put("status", 200);
		result.put("data", data);
		return result;
	}
	
	/**
	 * load ParamData
	 */
	@RequestMapping("/rest/item/param/item/query/{itemId}")
	@ResponseBody
	public Map loadParamData(@PathVariable Long itemId){
		Long itemCid = itemService.getItemCid(itemId);
		String paramData = itemService.getItemParamData(itemCid);
		//paramData.replaceAll("\"", "\'");
		System.out.println(paramData);
		Map result = new HashMap();
		Map data = new HashMap();
		data.put("paramData", paramData);
		data.put("id", itemCid);
		result.put("status", 200);
		result.put("data", data);
		return result;
	}
	//<a href="javascript:void(0)" class="l-btn l-btn-small l-btn-plain" group="" id="">
	//<span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">编辑</span>
	//<span class="l-btn-icon icon-edit">&nbsp;</span></span></a>
	@RequestMapping("/rest/page/item-edit")
	public String loadItem(){
		
		return "item-edit";
	}
	
	@RequestMapping("/rest/item/update")
	@ResponseBody
	public E3Result update(TbItem tbitem,TbItemDesc tbItemDesc){
		tbitem.setStatus((byte) 1);
		tbitem.setUpdated(new Date());
		tbitem.setCreated(itemService.getItemById(tbitem.getId()).getCreated());
		E3Result e3Result = itemService.update(tbitem, tbItemDesc);
		return e3Result;
	}
	@RequestMapping("/rest/item/delete")
	@ResponseBody	
	public E3Result delete(String ids){
		Long[] criteria = null;
		if(ids.contains(",")){
			String[] split = ids.split(",");
			criteria = new Long[split.length];
			for(int i=0;i<split.length;i++){
				criteria[i] = Long.parseLong(split[i]);
			}
		}else{
			criteria = new Long[]{Long.parseLong(ids)};
		}
		E3Result e3Result = itemService.delete(criteria);
		return e3Result;
	}
	@RequestMapping("rest/item/instock")
	@ResponseBody
	public E3Result instock(String ids){
		Long[] criteria = null;
		if(ids.contains(",")){
			String[] split = ids.split(",");
			criteria = new Long[split.length];
			for(int i=0;i<split.length;i++){
				criteria[i] = Long.parseLong(split[i]);
			}
		}else{
			criteria = new Long[]{Long.parseLong(ids)};
		}
		E3Result e3Result = itemService.instock(criteria);
		return e3Result;
	}
	
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public E3Result reshelf(String ids){
		Long[] criteria = null;
		if(ids.contains(",")){
			String[] split = ids.split(",");
			criteria = new Long[split.length];
			for(int i=0;i<split.length;i++){
				criteria[i] = Long.parseLong(split[i]);
			}
		}else{
			criteria = new Long[]{Long.parseLong(ids)};
		}
		E3Result e3Result = itemService.reshelf(criteria);
		return e3Result;
	}
}
