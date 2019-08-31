package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;

@Controller
public class SearchController {

	@Value("${PAGE_ROWS}")
	private Integer PAGE_ROWS;
	@Autowired
	private SearchService searchService;
	
	@RequestMapping("/search")
	public String search(String keyword,@RequestParam(defaultValue="1") Integer page, Model model) throws Exception{
		
		keyword = new String(keyword.getBytes("ISO8859-1"),"UTF-8");
		SearchResult searchResult = searchService.search(keyword, page, PAGE_ROWS);
		model.addAttribute("query",keyword);
		model.addAttribute("totalPages",searchResult.getTotalPages());
		model.addAttribute("recourdCount",searchResult.getRecourdCount());
		model.addAttribute("itemList",searchResult.getItemList());

		return "search";
		
	}
}
