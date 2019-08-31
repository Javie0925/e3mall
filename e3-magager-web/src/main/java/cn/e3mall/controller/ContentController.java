package cn.e3mall.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/content/save")
	@ResponseBody
	public E3Result addContent(TbContent content){
		Date date = new Date();
		content.setCreated(date);
		content.setUpdated(date);
		E3Result e3Result = contentService.addContent(content);
		return e3Result;
	}
	
	@RequestMapping("content/query/list")
	@ResponseBody
	public EasyUIDataGridResult loadContentList(Long categoryId, Integer page,Integer rows){
		EasyUIDataGridResult easyUIDataGridResult = contentService.loadContentList(categoryId, page, rows);
		return easyUIDataGridResult;
	}
	
	@RequestMapping("rest/content/edit")
	@ResponseBody
	public E3Result editContent(TbContent content){	
		content.setUpdated(new Date());
		contentService.updateContent(content);
		return E3Result.ok();
	}
	
	@RequestMapping("/content/delete")
	@ResponseBody
	public E3Result deleteContents(String ids){
		Long[] longIds = null;
		if(ids.contains(",")){
			String[] split = ids.split(",");
			longIds = new Long[split.length];
			for(int i=0;i<split.length;i++){
				longIds[i] = Long.parseLong(split[i]);
			}
		}else{
			longIds = new Long[]{Long.parseLong(ids)};
		}
		E3Result e3Result = contentService.deleteContentByIds(longIds);
		return e3Result;
	}
}
