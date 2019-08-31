package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbContent;

public interface ContentService {
	
	E3Result addContent(TbContent content);
	EasyUIDataGridResult loadContentList(Long categoryId,Integer page,Integer rows); 
	void updateContent(TbContent content);
	E3Result deleteContentByIds(Long[] ids);
	List<TbContent> getContentListByCatId(Long id);
}
