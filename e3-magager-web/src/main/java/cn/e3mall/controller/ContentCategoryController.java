package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;

@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;

	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(
			@RequestParam(name="id",defaultValue="0") Long parentId){
		
		List<EasyUITreeNode> result = contentCategoryService.getContentCatList(parentId);
		
		return result;
	}
	@RequestMapping("/content/category/create")
	@ResponseBody
	public E3Result addContentCategory(Long parentId,String name){
		
		E3Result e3Result = contentCategoryService.addContentCategory(parentId, name);
		return e3Result;
	}
	@RequestMapping("/content/category/update")
	@ResponseBody
	public E3Result updateContentCatById(Long id,String name){
		contentCategoryService.updateContentCatById(id, name);
		return E3Result.ok();
	}
	
	@RequestMapping("/content/category/delete/")
	@ResponseBody
	public E3Result deleteCatById(Long id){
		contentCategoryService.deleteCatById(id);
		return E3Result.ok();
	}
}
