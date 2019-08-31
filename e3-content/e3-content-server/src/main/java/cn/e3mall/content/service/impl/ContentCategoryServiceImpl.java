package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容分类管理service
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCatagoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCatList(Long parentId) {
		
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> catList = contentCatagoryMapper.selectByExample(example);
		List<EasyUITreeNode> result = new ArrayList<>();
		for(TbContentCategory t:catList){
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			easyUITreeNode.setId(t.getId());
			easyUITreeNode.setState(t.getIsParent()?"closed":"open");
			easyUITreeNode.setText(t.getName());
			result.add(easyUITreeNode);
		}
		return result;
	}

	@Override
	public E3Result addContentCategory(Long parentId, String name) {
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setName(name);
		tbContentCategory.setIsParent(false);
		Date date = new Date();
		tbContentCategory.setCreated(date);
		tbContentCategory.setUpdated(date);
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setStatus(1);//1.正常，2.删除
		contentCatagoryMapper.insert(tbContentCategory);
		TbContentCategory parent = contentCatagoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()){
			parent.setIsParent(true);
			contentCatagoryMapper.updateByPrimaryKey(parent);
		}
		return E3Result.ok(tbContentCategory);
	}

	@Override
	public void updateContentCatById(Long id, String name) {
		TbContentCategory category = contentCatagoryMapper.selectByPrimaryKey(id);
		category.setName(name);
		contentCatagoryMapper.updateByPrimaryKey(category);
	}

	@Override
	public void deleteCatById(Long id) {
		TbContentCategory category = contentCatagoryMapper.selectByPrimaryKey(id);
		category.setStatus(2);
		contentCatagoryMapper.updateByPrimaryKey(category);
	}

	

}
