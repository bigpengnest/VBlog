package com.BigPeng.VBlog;

import com.BigPeng.VBlog.util.VBlogUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VBlogApplicationTests {

	// 获取img标签正则
	private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
	// 获取src路径的正则
	private static final String IMGSRC_REG = "/\"(.*?)(\"|>|\\s+)";

	public void contextLoads() {
	}

	@Test
	public void testRegularExpression(){
		VBlogApplicationTests cm = new VBlogApplicationTests();
		String html = "<p><br/></p><p style=\"text-align:center\">    <img src=\"/ueditor/jsp/upload/image/20180322/1521729583711005560.jpg\" " +
				"width=\"550\" height=\"367\" alt=\"\"/>\n</p> " +
				"width=\"300\" height=\"200\"/></p><p>图文混排方法<br/></p><p>1. 图片居左，文字围绕图片排版</p>" +
				"<p>方法：在文字前面插入图片，设置居左对齐，然后即可在右边输入多行文本</p><p><br/></p><p>2. " +
				"图片居右，文字围绕图片排版</p><p>方法：在文字前面插入图片，设置居右对齐，然后即可在左边输入多行文本" +
				"</p><p><br/></p><p>3. 图片居中环绕排版</p><p>方法：亲，这个真心没有办法。。。</p><p><br/></p><p>" +
				"<br/></p><p><img src=\"http://img.baidu.com/hi/youa/y_0040.gif\" width=\"300\" height=\"300\"/>" +
				"</p><p>还有没有什么其他的环绕方式呢？这里是居右环绕</p><p><br/></p><p>欢迎大家多多尝试，为UEditor提供更多高质量模板！" +
				"</p><p><br/></p><p>占位</p><p><br/></p><p>占位</p><p><br/></p><p>占位</p><p><br/></p><p>占位</p><p><br/>" +
				"</p><p>占位</p><p><br/></p><p><br/></p><p><br/></p>";
		String src = VBlogUtil.getImgSrc(html);
		System.out.println(src);
	}
}
