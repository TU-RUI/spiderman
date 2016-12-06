package spiderman;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// PThread pthread = new PThread();
		// pthread.start();
		ZThread zthread = new ZThread();
		zthread.start();
//		weiboDelete wdthread = new weiboDelete();
//		wdthread.start();
//		 WThread wthread = new WThread("991642404@qq.com","turui123123");
//		 wthread.start();
//		weiboQuguan w = new weiboQuguan();
//		w.start();
	}
}

/**
 * P站爬虫
 * 
 * @author tr
 * 
 */
class PThread extends Thread {
	@Override
	public void run() {
		GetAndPost http = new GetAndPost();
		String posturl = "https://www.secure.pixiv.net/login.php";
		String postresult = "";
		String getresult = "";

		int collectionnum = 10; // 最低收藏数
		String key = "league_of_legends"; // 关键词
		String dirPath = "D:/picture/英雄联盟/"; // 文件存储路径
		int p = 1; // 开始的页数
		int e = 1000; // 结束的页数
		int i = 0; // 已扫描图片数
		String add = "&r18=1&abt=x";// &wlt=3001&hlt=3001
		int folder = 0; // 新建文件夹数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mode", "login"));
		params.add(new BasicNameValuePair("return_to", "/"));
		params.add(new BasicNameValuePair("pixiv_id", "991642404"));
		params.add(new BasicNameValuePair("pass", "turui123"));
		params.add(new BasicNameValuePair("skip", "1"));
		postresult = http.post(posturl, params, "utf-8", "utf-8");
		// System.out.println(postresult);
		// postresult = http.get("https://www.secure.pixiv.net", "utf-8");
		String url = "http://www.pixiv.net/search.php?s_mode=s_tag&word=" + key
				+ add;
		for (; p <= e; p++) {
			postresult = http.get(url + "&p=" + p, "utf-8", "");
			Document doc = Jsoup.parse(postresult);
			Elements elements = doc.select("li[class~=(image-item)]");
			if (elements.size() == 0) {
				break;
			} else {
				System.out.println("第" + p + "页");
				System.out.println(elements.size() + "个结果");
				for (Element element : elements) {
					System.out.println("已扫描" + ++i + "张图");
					if ((element.select("ul") != null)) {
						// System.out.println(element.select("ul").select("a").text()
						// + ""+"赞");
						String count = element.select("ul").select("a").text();
						count = count.replace(" ", "");
						if (!count.equals("")) {
							if ((Integer.parseInt(count) > collectionnum)) {
								Elements temp = element
										.select("a[class~=(work  _work)]");
								if (temp.size() == 0) {
									temp = element.select("a[class~=(work )]");
								}
								String t = "http://www.pixiv.net"
										+ temp.get(0).attr("href");
								// System.out.println("####"+t);
								getresult = http.get(t, "utf-8", "");
								Document doc2 = Jsoup.parse(getresult);
								Elements elements2 = doc2
										.select("img[class~=(original-image)]");
								if (elements2.size() == 0) {
									t = t.replace("medium", "manga");
									System.out.println("---->" + t);
									getresult = http.get(t, "utf-8", "");
									doc2 = Jsoup.parse(getresult);
									elements2 = doc2.select("img[src]");
									for (Element element2 : elements2) {
										if (element2.attr("data-src") != "") {
											System.out
													.println("--->---"
															+ element2
																	.attr("data-src"));
											http.download(
													element2.attr("data-src"),
													t, dirPath + folder + "/");
										} else {

										}
									}
									folder++;
								} else {
									String pictureurl = elements2.get(0).attr(
											"data-src");
									http.download(pictureurl, t, dirPath);
									System.out.println("---->" + pictureurl);
								}
							}
						}
					}
				}
			}
		}
		System.out.println("扫描结束");

	}
}

/**
 * Z站爬虫
 * 
 * @author tr
 * 
 */
class ZThread extends Thread {
	@Override
	public void run() {
		// TODO Auto-generated method stub
		GetAndPost http = new GetAndPost();
		String posturl = "http://www.zerochan.net/login";
		String postresult = "";
		String getresult = "";

		int collectionnum = 0; // 最低收藏数
		String key = "Horo"; // 关键词
//		String dirPath = "D:/picture/赫萝(Z站)/"; // 文件存储路径
		String dirPath = "/picture/赫萝(Z站)/"; // 文件存储路径
		int p = 1; // 开始的页数
		int e = 55; // 结束的页数
		int i = 0; // 已扫描图片数
		String add = "?s=fav&t=0";// 附加条件(热度排序,所有时间,质量超级高)
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("ref", "/"));
		params.add(new BasicNameValuePair("name", "991642404"));
		params.add(new BasicNameValuePair("password", "turui123"));
		params.add(new BasicNameValuePair("login", "Login"));
		postresult = http.post(posturl, params, "utf-8", "utf-8");
		// System.out.println(postresult);
		// http://www.zerochan.net/login?ref=/&name=991642404&password=turui123&login=Login
		// postresult = http.get("https://www.secure.pixiv.net", "utf-8");
		String url = "http://www.zerochan.net/" + key + add;
		for (; p <= e; p++) {
			postresult = http.get(url + "&p=" + p, "utf-8", "");
			Document doc = Jsoup.parse(postresult);
			Elements elements = doc.select("ul[id=thumbs2]").select("li");
			if (elements.size() == 0) {
				break;
			} else {
				System.out.println("第" + p + "页");
				System.out.println(elements.size() + "个结果");
				int count = 0;
				for (Element element : elements) {
					System.out.println("已扫描" + ++i + "张图");
					if (!(element.select("span").isEmpty())) {
						String c = element.select("span").text();
						c = c.replace(" Fav", "");
						count = Integer.parseInt(c);
					}
					if (count >= collectionnum) {
						String picurl2 = "http://www.zerochan.net"
								+ element.select("a").get(0).attr("href");
						getresult = http.get(picurl2, "utf-8", "");
						Document doc2 = Jsoup.parse(getresult);
						Elements elements2 = doc2.select("div[id=large]");
						String picurl = "";
						if (!elements2.select("img").isEmpty()
								&& elements2.select("img").get(0).attr("title")
										.equals("No larger size available")) {
							picurl = elements2.select("img").get(0).attr("src");
						} else {
							if(elements2.select("a").isEmpty()){
								continue;
							}
							picurl = elements2.select("a").get(0).attr("href");
						}
						System.out.println(picurl);
						http.download(picurl, picurl2, dirPath);
					}
				}
			}
		}
		System.out.println("扫描结束");

	}
}

/**
 * 微博爬虫
 * 
 * @author tr
 * 
 */
class WThread extends Thread {
	String username = "";
	String password = "";

	public WThread(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		GetAndPost http = new GetAndPost();
		String url = "https://passport.weibo.cn/sso/login";
		String getresult = "";
		String postresult = "";

		int page = 1; // 开始页数
		int end = 5; // 结束页数
		int repost = 0; // 转发数
		int follow = 0; // 关注数
		String key = "转发抽奖平台"; // 关键字

		Date date1 = new Date();
		System.err.println("任务开始--> " + date1);

		String searchurl = "http://weibo.cn/search/?gsid=&keyword=" + key
				+ "&smblog=搜微博&page=";
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("savestate", "1"));
		params.add(new BasicNameValuePair("pagerefer",
				"https://passport.weibo.cn/signin/welcome?entry=mweibo"));
		postresult = http.post(url, params, "utf-8", "utf-8");
		System.out.println(postresult);
		String ss = http.get("http://weibo.com/u/2631409972/home?wvr=5", "utf-8", "");
		System.out.println(ss);
		// 转发+关注
		for (; page <= end; page++) {
			System.out.println("读第" + page + "页");
			getresult = http.get(searchurl + page, "utf-8", "");
			Document doc = Jsoup.parse(getresult);
			System.out.println(getresult);
			Elements elements = doc.select("div[id]");
			String reposturl = "";
			for (Element element : elements) {
				Elements e = element.select(" a:contains(转发[)");
				if (!e.isEmpty()) {
					reposturl = e.get(0).attr("href");
					getresult = http.get(reposturl, "utf-8", "");
					doc = Jsoup.parse(getresult);
					Elements elements3 = doc.select("form[action]");
					if (!elements3.isEmpty()) {
						String real_repost_url = "http://weibo.cn"
								+ elements3.get(0).attr("action");
						List<NameValuePair> params2 = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("act", "dort"));
						params.add(new BasicNameValuePair("rl", "2"));
						params.add(new BasicNameValuePair("id", elements3
								.select("div").get(0).select("input[name=id]")
								.attr("value")));
						params.add(new BasicNameValuePair("content",
								"哈哈@qwerqwer520哈哈@xiaohaoqwe"));
						params.add(new BasicNameValuePair("rtkeepreason", "on"));
						postresult = http.post(real_repost_url, params2,
								"utf-8", "utf-8");
						if (postresult.contains("转发成功")) {
							System.out.println("转发第" + ++repost + "条");
						}
					}
					if (!doc.select("div[id]").select("a").isEmpty()) {
						String followurl = doc.select("div[id]").select("a")
								.get(0).attr("href");
						System.out.println("------" + "http://weibo.cn"
								+ followurl);
						getresult = http.get("http://weibo.cn" + followurl,
								"utf-8", "");
						doc = Jsoup.parse(getresult);
						Elements elements4 = doc.select("div[class=u]")
								.select("span[class=ctt]")
								.select("a:contains(加关注)");
						if (!elements4.isEmpty()) {
							String followurl2 = "http://weibo.cn"
									+ elements4.get(0).attr("href");

							System.out.println("关注:" + followurl);
							getresult = http.get(followurl2, "utf-8",
									"http://weibo.cn" + followurl);
							if (getresult.contains("关注成功")) {
								System.out.println("关注第" + ++follow + "个");
							}

						}
					}

				} else {

				}
			}
			// System.out.println(getresult);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 取消关注
		int cancelcount = 0;
		String followstr = http.get("http://weibo.cn/2631409972/follow",
				"utf-8", "");
		Document d = Jsoup.parse(followstr);
		Elements es = d.select("span[class=tc]");
		String followcount = es.get(0).text();
		followcount = followcount.substring(3, followcount.length() - 1);
		int endpage = (new Integer(followcount)) / 10 - 10;
		int i = 0;
		for (; i < 5; i++) {
			int temppage = endpage - i;
			String followurl = "http://weibo.cn/2631409972/follow?page="
					+ temppage;
			followstr = http.get(followurl, "utf-8", "");
			d = Jsoup.parse(followstr);
			System.out.println(followurl);
			Elements es2 = d.select("a:contains(取消关注)");
			System.out.println(es2.size() + "");
			for (Element e : es2) {
				followstr = http.get(e.attr("href"), "utf-8", followurl);
				d = Jsoup.parse(followstr);
				String s = d.select("a:contains(确定)").attr("href");
				followstr = http.get("http://weibo.cn" + s, "utf-8",
						e.attr("href"));
				if (followstr.contains("操作成功")) {
					System.out.println("取消关注" + ++cancelcount);
				}
			}
		}

		Date date2 = new Date();
		System.err.print("任务结束-->" + date2);
	}
}


//删微博
class weiboDelete extends Thread {
	String username = "991642404@qq.com";
	String password = "turui123123";
	int count = 1; 
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		GetAndPost http = new GetAndPost();
		String url = "https://passport.weibo.cn/sso/login";
		String getresult = "";
		String postresult = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("savestate", "1"));
		params.add(new BasicNameValuePair("pagerefer",
				"https://passport.weibo.cn/signin/welcome?entry=mweibo"));
		postresult = http.post(url, params, "utf-8", "utf-8");
		System.out.println(postresult);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getresult = http.get("http://weibo.cn/2631409972/profile?page=", "utf-8","");
		System.out.println(getresult);
		
		int i = 1;
		for (; ; i++) {
			getresult = http
					.get("http://weibo.cn/2631409972/profile?page=" + i,
							"utf-8","");
			if(getresult.contains("微博广场")||getresult.contains("charset=gb2312")){
				break;
			}
			Document doc = Jsoup.parse(getresult);
			Elements elements = doc.select("div[class=c]");

			for (Element e : elements) {
				String deleteUrl = e.select("a:contains(删除)").attr("href");
				if(deleteUrl == null||deleteUrl.equals("")){
					continue;
				}
				String res = http.get(deleteUrl, "utf-8","");
				if(res.contains("微博广场")||res.contains("charset=gb2312")){
					System.exit(0);
				}
				doc = Jsoup.parse(res);
				String deteleUrl2 = "http://weibo.cn"
						+ doc.select("a:contains(确定删除)").attr("href");
				if(deleteUrl == null||deleteUrl.equals("")){
					continue;
				}
				res = http.get(deteleUrl2, "utf-8","");
				if(res.contains("微博广场")||res.contains("charset=gb2312")){
					System.exit(0);
				}
				if (res.contains("删除成功!")) {
					System.out.println("删除成功"+count++);
				}
				if(count==200){
					System.exit(0);
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

}


//微博取关
class weiboQuguan extends Thread {
	String username = "991642404@qq.com";
	String password = "turui123123";
	int count = 1; 
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		GetAndPost http = new GetAndPost();
		String url = "https://passport.weibo.cn/sso/login";
		String getresult = "";
		String postresult = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("savestate", "1"));
		params.add(new BasicNameValuePair("pagerefer",
				"https://passport.weibo.cn/signin/welcome?entry=mweibo"));
		postresult = http.post(url, params, "utf-8", "utf-8");
		System.out.println(postresult);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getresult = http.get("http://weibo.cn/2631409972/profile?page=", "utf-8","");
		System.out.println(getresult);
		
		int i = 1; 
		String followstr;
		for (; ; i++) {
			String followurl = "http://weibo.cn/2631409972/follow?page=" + i;
			getresult = http.get(followurl,"utf-8","");
			if(getresult.contains("微博广场")||getresult.contains("charset=gb2312")){
				break;
			}
			Document doc = Jsoup.parse(getresult);
			Elements es2 = doc.select("a:contains(取消关注)");
			System.out.println(es2.size() + "");
			for (Element e : es2) {
				followstr = http.get(e.attr("href"), "utf-8", followurl);
				doc = Jsoup.parse(followstr);
				String s = doc.select("a:contains(确定)").attr("href");
				followstr = http.get("http://weibo.cn" + s, "utf-8",
						e.attr("href"));
				if (followstr.contains("操作成功")) {
					System.out.println("取消关注" + ++count);
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

}
