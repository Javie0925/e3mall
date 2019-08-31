package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper tbUserMapper;
	@Override
	public E3Result checkDate(String param, Integer type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//type://1、2、3分别代表username、phone、email
		if(type==1){
			criteria.andUsernameEqualTo(param);
		}else if(type==2){
			criteria.andPhoneEqualTo(param);
		}else if(type==3){
			criteria.andEmailEqualTo(param);
		}else{
			return E3Result.build(400, "非法参数");
		}
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if(list==null||list.size()==0){
			return E3Result.ok(true);
		}
		return E3Result.ok(false);
	}
	@Override
	public E3Result createUser(TbUser tbUser) {
		//检查数据可用性
		if(StringUtils.isBlank(tbUser.getUsername())||
				StringUtils.isBlank(tbUser.getPhone())||
				tbUser==null){
			return E3Result.build(400, "数据错误");
		}
		if(!(boolean)checkDate(tbUser.getUsername(), 1).getData()||
				!(boolean)checkDate(tbUser.getPhone(), 2).getData()){
			return E3Result.build(400, "数据错误");
		}
		//添加时间信息
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		//加密
		String md5Pass = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
		tbUser.setPassword(md5Pass);
		tbUserMapper.insert(tbUser);
		return E3Result.ok();
	}

}
