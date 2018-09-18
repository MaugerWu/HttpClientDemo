package com.cqupt.mauger.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 如何发起一个 HTTP 请求？
 * 
 * 通过 HttpURLConnection 去实现，HttpURLConnection 是 Java 的标准类，是 Java 比较原生的一种实现方式。
 * 
 * @author Mauger
 * @date 2018年9月4日  
 * @version 1.0
 */
public class HttpClient
{
	/**
	 * doGet
	 * @param httpUrl URL
	 * @return String
	 */
	public String doGet(String httpUrl)
	{
		HttpURLConnection conn = null;
		InputStream is = null;
		BufferedReader br = null;
		String result = null; // 返回结果字符串
		try
		{
			// 创建远程 URL 连接对象
			URL url = new URL(httpUrl);
			// 通过远程 URL 连接对象打开一个连接，强制转成 HttpURLConnection 类
			conn = (HttpURLConnection) url.openConnection();
			// 设置连接方式：GET
			conn.setRequestMethod("GET");
			// 设置连接主机服务器的超时时间：10秒
			conn.setConnectTimeout(10000);
			// 设置读取远程返回的数据时间：1分钟
			conn.setReadTimeout(60000);
			// 发送请求
			conn.connect();
			
			// 通过 Connection 连接，获取输入流
			if (conn.getResponseCode() == 200)
			{
				is = conn.getInputStream();
				// 封装输入流 InputStream，并指定字符集
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				// 存放数据
				StringBuffer sb = new StringBuffer();
				String temp = null;
				while ((temp = br.readLine()) != null)
				{
					sb.append(temp);
					sb.append("\r\n");
				}
				result = sb.toString();
			}
		} catch (MalformedURLException e)
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
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			// 关闭远程连接
			conn.disconnect();
		}
		return result;
	}
	
	/**
	 * doPost
	 * @param httpUrl URL
	 * @param param Parameter
	 * @return String
	 */
	public String doPost(String httpUrl, String param)
	{
		HttpURLConnection conn = null;
		InputStream is = null;
		OutputStream os = null;
		BufferedReader br = null;
		String result = null; // 返回结果字符串
		
		try
		{
			// 创建远程 URL 连接对象
			URL url = new URL(httpUrl);
			// 通过远程 URL 连接对象打开一个连接，强制转成 HttpURLConnection 类
			conn = (HttpURLConnection) url.openConnection();
			// 设置连接方式：POST
			conn.setRequestMethod("POST");
			// 设置连接主机服务器的超时时间：10秒
			conn.setConnectTimeout(10000);
			// 设置读取远程返回的数据时间：1分钟
			conn.setReadTimeout(60000);
			
			// 默认值为 false，当向远程服务器传送数据或写数据时，需要设置为 true
			conn.setDoOutput(true);
			// 默认值为 true，当向远程服务器读取数据时，设置为 true，该参数可有可无
			conn.setDoInput(true);
			// 设置传入参数的格式：name1=value1&name2=value2
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            conn.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            
            // 通过连接对象获取一个输出流
            os = conn.getOutputStream();
            // 通过输出流对象将参数写/传出去，它是通过字节数组写出的
            os.write(param.getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            if (conn.getResponseCode() == 200)
			{
				is = conn.getInputStream();
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				StringBuffer sb = new StringBuffer();
				String temp = null;
				while ((temp = br.readLine()) != null)
				{
					sb.append(temp);
					sb.append("\r\n");
				}
				result = sb.toString();
			}
		} catch (MalformedURLException e)
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

			if (null != os)
			{
				try
				{
					os.close();
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
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			// 关闭远程连接
			conn.disconnect();
		}
		return result;
	}
}
