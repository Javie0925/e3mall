package cn.e3mall.item.pojo;

import cn.e3mall.pojo.TbItem;

public class Item extends TbItem {
	
	public Item(TbItem tbItem){
		this.setBarcode(tbItem.getBarcode());
		this.setCid(tbItem.getCid());
		this.setCreated(tbItem.getCreated());
		this.setId(tbItem.getId());
		this.setImage(tbItem.getImage());
		this.setNum(tbItem.getNum());
		this.setPrice(tbItem.getPrice());
		this.setSellPoint(tbItem.getSellPoint());
		this.setStatus(tbItem.getStatus());
		this.setTitle(tbItem.getTitle());
		this.setUpdated(tbItem.getUpdated());
	}
	public String[] getImages() {
		String image = this.getImage();
		String[] images = null;
		if(image!=null&&!"".equals(image)){
			if(image.contains(",")){
				images = image.split(",");
				return images;
			}else{
				images = new String[]{image};
				return images;
			}
		}
		return null;
	}
}
