package cn.e3mall.content.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {

	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbContentMapper contentMapper;
	@Override
	public E3Result addContent(TbContent content) {
		contentMapper.insertSelective(content);
		try {
			jedisClient.hdel(CONTENT_LIST, content.getCategoryId()+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return E3Result.ok();
	}
	@Override
	public EasyUIDataGridResult loadContentList(Long categoryId, Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example );
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());

		return result;
	}
	@Override
	public void updateContent(TbContent content) {
		contentMapper.updateByPrimaryKeySelective(content);
		try {
			jedisClient.hdel(CONTENT_LIST, content.getCategoryId()+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public E3Result deleteContentByIds(Long[] ids) {
		
		for(Long id:ids){
			contentMapper.deleteByPrimaryKey(id);
		}
		try {
			jedisClient.hdel(CONTENT_LIST, "*");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return E3Result.ok();
	}
	@Override
	public List<TbContent> getContentListByCatId(Long catId) {
		//查询缓存
		try {
			String json = jedisClient.hget(CONTENT_LIST, catId+"");
			if(StringUtils.isNotBlank(json)){
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(catId);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example );
		//保存到缓存中
		try {
			jedisClient.hset(CONTENT_LIST, catId+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
