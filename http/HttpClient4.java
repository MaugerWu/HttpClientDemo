package com.cqupt.mauger.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * HttpClient4.x 实现一个 HTTP 请求
 * HttpClient4.5.6 是 org.apache.http.client 下操作远程 URL 的工具包，2018-09-01目前最新的。
 * 
 * @author Mauger
 * @date 2018年9月4日  
 * @version 1.0
 */
public class HttpClient4
{

	/**
	 * doGet
	 * @param url URL
	 * @return String
	 */
	public static String doGet(String url)
	{
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		String result = "";

		// 通过默认配置创建一个 HttpClient 实例
		httpClient = HttpClients.createDefault();
		// 创建 HttpGet 远程连接实例
		HttpGet httpGet = new HttpGet(url);
		// 设置请求头信息，鉴权
		httpGet.setHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
		// 设置配置请求参数（连接主机服务超时、连接请求超时、数据读取时间）
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3500).setConnectionRequestTimeout(3500)
				.setSocketTimeout(60000).build();
		httpGet.setConfig(requestConfig);

		try
		{
			// 执行 Get 请求得到返回对象
			response = httpClient.execute(httpGet);
			// 通过返回对象获取返回数据
			HttpEntity entity = response.getEntity();
			// 通过 EntityUtils 中的 toString 方法将结果转化为字符串
			result = EntityUtils.toString(entity);
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
		} catch (IOException e1)
		{
			e1.printStackTrace();
		} finally
		{
			// 关闭资源
			if (null != response)
			{
				try
				{
					response.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			if (null != httpClient)
			{
				try
				{
					httpClient.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		return result;
	}
	
	/**
	 * doPost
	 * @param url URL
	 * @param paramMap Parameter
	 * @return String
	 */
	public static String doPost(String url, Map<String, Object> paramMap)
	{
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		String result = "";

		// 创建 httpClient 实例
		httpClient = HttpClients.createDefault();
		// 创建 httpPost 远程连接实例
		HttpPost httpPost = new HttpPost(url);
		// 设置配置请求参数（连接主机服务超时、连接请求超时、数据读取时间）
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3500).setConnectionRequestTimeout(3500)
				.setSocketTimeout(60000).build();
		// 为 httpPost 实例设置配置
		httpPost.setConfig(requestConfig);
		
		// 设置请求头
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		// 封装 post 请求参数
		if (null != paramMap && paramMap.size() > 0)
		{
			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
			// 通过 Map 集成 entrySet 方法 获取 Entity
			Set<Entry<String, Object>> entrySet = paramMap.entrySet();
			// 循环遍历，获取迭代器
			Iterator<Entry<String, Object>> iterator = entrySet.iterator();
			while (iterator.hasNext())
			{
				Entry<String, Object> entry = iterator.next();
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
			
			try
			{
				// 为 httpPost 设置封装好的请求参数
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			
			try
			{
				// httpClient 对象执行 post 请求，并返回相应参数对象
				response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);
				
				// 若传入参数为 json 时
				// httpPost.setEntity(new StringEntity("json串"));
				// httpPost.addHeader("Content-Type", "application/json");
			} catch (ClientProtocolException e)
			{
				e.printStackTrace();
			} catch (IOException e1)
			{
				e1.printStackTrace();
			} finally
			{
				// 关闭资源
				if (null != response)
				{
					try
					{
						response.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}

				if (null != httpClient)
				{
					try
					{
						httpClient.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		return result;
	}
}
