package spiderman;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import junit.framework.TestCase;

public class UnitTest extends TestCase {
	private SQL sql;
	
	public void test(){
		GetAndPost http = new GetAndPost();
		String s;// = http.get("http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.18)", "utf-8", "");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String username = "991642404@qq.com";
		String password = "turui123123";
		String url = "https://passport.weibo.cn/sso/login";
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("savestate", "1"));
		params.add(new BasicNameValuePair("pagerefer",
				"https://passport.weibo.cn/signin/welcome?entry=mweibo"));
		s = http.post(url, params, "utf-8", "utf-8");
		System.out.println(s);
		s = http.get("http://weibo.cn/2631409972/profile", "utf-8", "");
		System.out.println(s);
		s = http.get("http://weibo.com/u/2631409972/home?wvr=5", "gb2312", "http://weibo.cn/2631409972/profile");
		System.out.println(s);
	}
	
	
}
