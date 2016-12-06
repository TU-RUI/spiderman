package spiderman;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class Fetcher {
	// private HttpClient httpclient;
	private HttpGet httpget;
	private String UserAgent[] = {
			"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0",
			"AppleWebKit/537.36 (KHTML, like Gecko)", "Chrome/27.0.1453.94",
			"Safari/537.36", "Opera/9.27 (Windows NT 5.2; U; zh-cn)" };
	static int u = 0;

	public Fetcher() {
		// PoolingClientConnectionManager conMan = new
		// PoolingClientConnectionManager();
		// conMan.setMaxTotal(5);
		// conMan.setDefaultMaxPerRoute(5);

	}

	/**
	 * 
	 * @param url
	 *            需要POST的地址
	 * @param params
	 *            需要POST的数据
	 * @param encode
	 *            POST数据的编码方式
	 * @param decode
	 *            页面的解码方式
	 * @return
	 */
	@SuppressWarnings("resource")
	public String Post(String url, List<NameValuePair> params, String encode,
			String decode) {
		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		httppost.addHeader("Accept-Language",
				"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		httppost.addHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(5000).setConnectTimeout(5000).build();// 设置请求和传输超时时间
		httppost.setConfig(requestConfig);
		httppost.setHeader("Content-Type", HTTP.UTF_8);
		UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(params, encode);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		httppost.setEntity(entity);
		HttpResponse httpresponse = null;
		try {
			httpresponse = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		// httppost.abort();
		HttpEntity httpentity = httpresponse.getEntity();
		// System.out.println(httpresponse.getStatusLine().getStatusCode()
		// + "");
		if (httpresponse.getStatusLine().getStatusCode() == 200
				&& httpentity != null) {
			try {
				result = EntityUtils.toString(httpentity, decode);
				// System.out.println("PageResult:\n"+result);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		} else if (httpresponse.getStatusLine().getStatusCode() == 302) {
			String locationUrl = httpresponse.getLastHeader("Location")
					.getValue();
			result = getPage(locationUrl, "utf-8");
		} else {
			result = httpresponse.getStatusLine().getStatusCode() + "";
		}
		return result;
	};

	/**
	 * 获取页面
	 * 
	 * @param url
	 *            地址
	 * @param decode
	 *            页面解码方式
	 * @return
	 */
	@SuppressWarnings("resource")
	public String getPage(String url, String decode) {
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT,
				10000);
		httpclient.getParams().setIntParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		httpclient.getParams().setParameter(
				ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		HttpClientParams.setCookiePolicy(httpclient.getParams(),
				CookiePolicy.BROWSER_COMPATIBILITY);
		httpget = new HttpGet(url);
		// 设置请求头
		httpget.addHeader("Accept-Language",
				"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");

		httpget.addHeader("User-Agent", UserAgent[new Random().nextInt(4)]);
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(10000).setConnectTimeout(10000).build();
		httpget.setConfig(requestConfig);
		HttpResponse httpresponse = null;
		try {
			httpresponse = httpclient.execute(httpget);
			HttpEntity httpentity = httpresponse.getEntity();
			int Status = httpresponse.getStatusLine().getStatusCode();
			if (Status == 200 && httpentity != null) {
				try {
					result = EntityUtils.toString(httpentity, decode);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						httpresponse = httpclient.execute(httpget);
						HttpEntity httpentity1 = httpresponse.getEntity();
						int Status1 = httpresponse.getStatusLine()
								.getStatusCode();
						if (Status1 == 200 && httpentity1 != null) {
							try {
								result = EntityUtils.toString(httpentity1,
										decode);
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								try {
									result = EntityUtils.toString(httpentity1,
											decode);
								} catch (ParseException e11) {
									// TODO Auto-generated catch block
									e11.printStackTrace();
								} catch (IOException e11) {
									// TODO Auto-generated catch block

								}

							}
						} else if (Status1 == 429) {
							String TryAfterTime = httpresponse.getLastHeader(
									"Retry-After").getValue();

							return "429," + TryAfterTime;
						}
					} catch (ConnectTimeoutException e1) {
						// TODO Auto-generated catch block
						try {
							httpresponse = httpclient.execute(httpget);
							HttpEntity httpentity1 = httpresponse.getEntity();
							int Status1 = httpresponse.getStatusLine()
									.getStatusCode();
							if (Status1 == 200 && httpentity1 != null) {
								try {
									result = EntityUtils.toString(httpentity1,
											decode);
								} catch (ParseException e11) {
									// TODO Auto-generated catch block
									e11.printStackTrace();
								} catch (IOException e11) {
									// TODO Auto-generated catch block
									e11.printStackTrace();
								}
							} else if (Status1 == 429) {
								String TryAfterTime = httpresponse
										.getLastHeader("Retry-After")
										.getValue();

								return "429," + TryAfterTime;
							}
						} catch (ConnectTimeoutException e11) {
							// TODO Auto-generated catch block
							try {
								httpresponse = httpclient.execute(httpget);
								HttpEntity httpentity1 = httpresponse
										.getEntity();
								int Status1 = httpresponse.getStatusLine()
										.getStatusCode();
								if (Status1 == 200 && httpentity1 != null) {
									try {
										result = EntityUtils.toString(
												httpentity1, decode);
									} catch (ParseException e111) {
										// TODO Auto-generated catch block
										e111.printStackTrace();
									} catch (IOException e111) {
										// TODO Auto-generated catch block
										e111.printStackTrace();
									}
								}
							} catch (ConnectTimeoutException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							} catch (IOException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
						} catch (IOException e11) {
							// TODO Auto-generated catch block
							e11.printStackTrace();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					httpclient.getConnectionManager().shutdown();
					return result;

				}
			} else if (Status == 429) {
				String TryAfterTime = httpresponse.getLastHeader("Retry-After")
						.getValue();
				return "429," + TryAfterTime;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				httpresponse = httpclient.execute(httpget);
				HttpEntity httpentity = httpresponse.getEntity();
				int Status = httpresponse.getStatusLine().getStatusCode();
				if (Status == 200 && httpentity != null) {
					try {
						result = EntityUtils.toString(httpentity, decode);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} catch (ConnectTimeoutException e1) {
				// TODO Auto-generated catch block
				try {
					httpresponse = httpclient.execute(httpget);
					HttpEntity httpentity = httpresponse.getEntity();
					int Status = httpresponse.getStatusLine().getStatusCode();
					if (Status == 200 && httpentity != null) {
						try {
							result = EntityUtils.toString(httpentity, decode);
						} catch (ParseException e11) {
							// TODO Auto-generated catch block
							e11.printStackTrace();
						} catch (IOException e11) {
							// TODO Auto-generated catch block
							e11.printStackTrace();
						}
					} else if (Status == 429) {
						String TryAfterTime = httpresponse.getLastHeader(
								"Retry-After").getValue();

						return "429," + TryAfterTime;
					}
				} catch (ConnectTimeoutException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}catch(SocketException e2){
				e2.printStackTrace();
				return null;
			}catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			} 
		}
		httpclient.getConnectionManager().shutdown();
		return result;
	}
}
