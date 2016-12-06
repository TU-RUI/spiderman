package spiderman;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class GetAndPost {

	public HttpClient httpclient;
	private static int i = 0; // �ļ�����ʼ��

	public GetAndPost() {
		// TODO Auto-generated constructor stub
		PoolingClientConnectionManager conMan = new PoolingClientConnectionManager(
				SchemeRegistryFactory.createDefault());
		conMan.setMaxTotal(200);
		conMan.setDefaultMaxPerRoute(200);

		httpclient = new DefaultHttpClient(conMan);
		httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT,
				10000);
		httpclient.getParams().setIntParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		httpclient.getParams().setParameter(
				ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		HttpClientParams.setCookiePolicy(httpclient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);  
		
	}
	

	/**
	 * 
	 * @param url
	 *            ��ҪPOST�ĵ�ַ
	 * @param params
	 *            ��ҪPOST������
	 * @param encode
	 *            POST���ݵı��뷽ʽ
	 * @param decode
	 *            Դ����뷽ʽ
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String post(String url, List<NameValuePair> params, String encode,
			String decode) {
		String result = "";
		HttpPost httppost = new HttpPost(url);
		httppost.addHeader("Accept-Language",
				"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		if (url.contains("weibo")) {
			httppost.addHeader("Referer", "http://m.weibo.cn/");
//			httppost.addHeader("Referer", "http://weibo.com/login.php");
			
		}
		httppost.addHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(5000).setConnectTimeout(5000).build();// ��������ʹ��䳬ʱʱ��
		httppost.setConfig(requestConfig);
		UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(params, encode);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		httppost.setEntity(entity);
		try {
			HttpResponse httpresponse = httpclient.execute(httppost);
			// httppost.abort();
			HttpEntity httpentity = httpresponse.getEntity();
			System.out.println(httpresponse.getStatusLine().getStatusCode()
					+ "");
			if (httpresponse.getStatusLine().getStatusCode() == 200
					&& httpentity != null) {
				result = EntityUtils.toString(httpentity, decode);
			} else if (httpresponse.getStatusLine().getStatusCode() == 302) {
				String locationUrl = httpresponse.getLastHeader("Location")
						.getValue();
				// System.err.print(locationUrl);
				result = get(locationUrl, "utf-8", "");// ��ת���ض����url
			} else {
				result = httpresponse.getStatusLine().getStatusCode() + "";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.print("�쳣!!");
			System.exit(1);
		}
		return result;
	};

	/**
	 * 
	 * @param url
	 *            ��ҪGET�ĵ�ַ
	 * @param decode
	 *            ���뷽ʽ
	 * @return
	 */
	public String get(String url, String decode, String referer){
		String result = "";
		HttpGet httpget = new HttpGet(url);
		httpget.addHeader("Accept-Language",
				"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		httpget.addHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
		if (referer != "") {
			httpget.addHeader("Referer", referer);
		}
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(10000).setConnectTimeout(10000).build();// ��������ʹ��䳬ʱʱ��
		httpget.setConfig(requestConfig);
		try {
			HttpResponse httpresponse = httpclient.execute(httpget);
			HttpEntity httpentity = httpresponse.getEntity();
			if (httpresponse.getStatusLine().getStatusCode() == 200) {
				if (httpentity != null) {
					result = EntityUtils.toString(httpentity, decode);
				}
			} else {
				result = httpresponse.getStatusLine().getStatusCode() + "";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param url
	 *            ͼƬ��ַ
	 * @param referer
	 *            referer
	 * @param dirPath
	 *            �ļ��洢·��
	 */
	public void download(String url, String referer, String dirPath) {
		HttpClient download = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		if (!url.contains("http")) {
			System.err.print("ͼƬ��ַ��Ч: " + url);
		} else {

			httpget.addHeader("Referer", referer);
			try {
				HttpResponse httpresponse = download.execute(httpget);
				HttpEntity httpentity = httpresponse.getEntity();
				InputStream in = httpentity.getContent();
				try {
					File dir = new File(dirPath);
					if (dir == null || !dir.exists()) {
						dir.mkdirs();
					}
					// �ļ���ʵ·��
					String realPath = dirPath.concat(i++ + ".jpg");
					File file = new File(realPath);
					if (file == null || !file.exists()) {
						file.createNewFile();
					}
					FileOutputStream fos = new FileOutputStream(file);
					byte[] buf = new byte[1024];
					int len = 0;
					while ((len = in.read(buf)) != -1) {
						fos.write(buf, 0, len);
					}
					fos.flush();
					fos.close();
					System.err.println("���ص�" + i + "��");
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("error");
				} finally {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
