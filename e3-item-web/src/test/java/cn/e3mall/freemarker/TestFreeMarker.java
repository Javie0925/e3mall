package cn.e3mall.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreeMarker {

	@Test
	public void testFreeMarker() throws Exception{
		
		//1 创建模板对象
		//2 创建一个configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//3 设置模板文件保存的目录
		configuration.setDirectoryForTemplateLoading(
				new File("F:/Java/MyeclipseWorkspace/e3-item-web/src/main/webapp/WEB-INF/ftl/"));
		//4 模板文件的编码格式，一般使用utf-8
		configuration.setDefaultEncoding("utf-8");
		//5 加载一个模板文件，创建一个模板对象
		Template template = configuration.getTemplate("hello.ftl");
		//6 创建一个数据集/可以实pojo也可以用map。推荐用map。
		Map data = new HashMap();
		data.put("hello", "hello freemarker");
		//7 创建一个writer对象，指定输出文件的路径及名称
		File dir = new File("D:/temp/JavaEE32/freemarker/");
		dir.mkdirs();
		Writer out = new FileWriter(new File("D:/temp/JavaEE32/freemarker/hello.txt"));
		//8 生成静态页面
		template.process(data, out);
		//9 关闭流
		out.close();
	}
}
