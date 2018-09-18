package com.cqupt.mauger.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * HttpClient3.1 实现一个 HTTP 请求
 * 
 * HttpClient3.1 是 org.apache.commons.httpclient 下操作远程 URL 的工具包，虽然已不再更新，
 * 但现实工作中使用 HttpClient3.1 的代码还是很多。
 * 
 * @author Mauger
 * @date 2018年9月4日  
 * @version 1.0
 */
public class HttpClient3
{
	/**
	 * doGet
	 * @param url URL
	 * @return String
	 */
	public String doGet(String url)
	{
		// 输入流
		InputStream is = null;
		BufferedReader br = null;
		String result = null;
		
		// 创建 HttpClient 实例
		HttpClient httpClient = new HttpClient();
		// 设置 HTTP 连接主机服务超时时间：15000毫秒
		// 先获取连接管理器对象，再获取参数对象，再进行参数的赋值
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
		// 创建一个 GET 方法实例对象
		GetMethod getMethod = new GetMethod(url);
		// 设置请求超时：60000毫秒
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
		// 设置请求重试机制，默认重试次数：3次；参数设置：true，重试机制可用，false 则相反
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, true));
		
		try
		{
			// 执行 GET 方法
			int statusCode = httpClient.executeMethod(getMethod);
			// 判断返回状态码
			if (statusCode != HttpStatus.SC_OK)
			{
				// 如果状态码返回的不是OK，说明失败了并打印错误信息
				System.out.println("Method faild: " + getMethod.getStatusLine());
			} else
			{
				// 通过 getMethod 实例，获取一个输入流
				is = getMethod.getResponseBodyAsStream();
				// 包装输入流
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				StringBuffer sb = new StringBuffer();
				// 获取封装的输入流
				String temp = null;
				while ((temp = br.readLine()) != null)
				{
					sb.append(temp).append("\r\n");
				}
				result = sb.toString();
			}
		} catch (HttpException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			// 关闭资源
			if (null != br)
			{
				try
				{
					br.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			if (null != is)
			{
				try
				{
					is.close();
				} catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
			
			// 释放连接
			getMethod.releaseConnection();
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
		// 获取输入流
		InputStream is = null;
		BufferedReader br = null;
		String result = null;
		
		// 创建 HttpClient 实例对象
		HttpClient httpClient = new HttpClient();
		// 设置 HTTP 连接主机服务超时时间：15000毫秒
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
		// 创建 POST 请求方法实例对象
		PostMethod postMethod = new PostMethod();
		// 设置 post 请求超时时间：60000
		postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
		
		NameValuePair[] nvp = null;
		// 判断参数 Map 集合 mapParam 是否为空
		if (null != paramMap && paramMap.size() > 0)
		{
			// 创建键值参数对象数组，大小为参数个数
			nvp = new NameValuePair[paramMap.size()];
			// 循环遍历参数集合 Map
			Set<java.util.Map.Entry<String, Object>> entrySet = paramMap.entrySet();
			// 获取迭代器
			Iterator<java.util.Map.Entry<String, Object>> iterator = entrySet.iterator();
			int index = 0;
			while (iterator.hasNext())
			{
				Map.Entry<String, Object> mapEntry = iterator.next();
				try
				{
					// 从 mapEntry 中获取 key 和 value 创建键值对象存放到数组中
					nvp[index] = new NameValuePair(mapEntry.getKey(), new String(mapEntry.getValue().toString()
							.getBytes("UTF-8"), "UTF-8"));
				} catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
				index++;
			}
		}
		
		// 判断 nvp 数组是否为空
		if (null != nvp && nvp.length > 0)
		{
			// 将参数存放到 requestBody 对象中
			postMethod.setRequestBody(nvp);
			try
			{
				// 执行 post 方法
				int statusCode = httpClient.executeMethod(postMethod);
				// 判断是否成功
				if (statusCode != HttpStatus.SC_OK)
				{
					System.out.println("Method faild: " + postMethod.getStatusLine());
				} else
				{
					// 获取远程返回数据
					is = postMethod.getResponseBodyAsStream();
					// 封装输入流
					br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					StringBuffer sb = new StringBuffer();
					String temp = null;
					while ((temp = br.readLine()) != null)
					{
						sb.append(temp).append("\r\n");
					}
					result = sb.toString();
				}
			} catch (HttpException e)
			{
				e.printStackTrace();
			} catch (IOException e1)
			{
				e1.printStackTrace();
			} finally
			{
				// 关闭资源
				if (null != br)
				{
					try
					{
						br.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				
				if (null != is)
				{
					try
					{
						is.close();
					} catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
				
				// 释放连接
				postMethod.releaseConnection();
			}
		}

		return result;
	}
}
