package com.imdada;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequestUtils {

	/**
	 * 发送get请求
	 * 
	 * @param path
	 *            请求地址
	 * @param param
	 *            请求参数
	 */
	public String sendGet(String pathParam) {
//		HttpClients http
		BufferedReader br = null;
		String actual = null;
		try {
			// 获取url对象
			URL url = new URL(pathParam);
			// 打开链接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置提交类型
			conn.setRequestMethod("GET");
			// 获取相应数据
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			actual = br.readLine();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			try {
				System.out.println("get请求参数：" + pathParam);
				System.out.println("异常情况" + e);
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return actual;
	}

	/**
	 * 发送post请求
	 * 
	 * @param path
	 *            请求地址
	 * @param data
	 *            json格式参数
	 * @param isJson
	 *            参数是否为json格式
	 * @param param
	 *            其他格式（例：key1=value1&key2=value2）
	 */
	public String sendPOST(String path, String isJson, String param) {
		BufferedReader br = null;
		String actual = null;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 是否为json格式
			if ("Y".equals(isJson.toUpperCase())) {
				conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			}
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Apache-HttpClient/4.5.2");
			// 发送post请求必须设置如下两行,设置了这两行，就可以对URL连接进行输入/输出
			conn.setDoInput(true);
			conn.setDoOutput(true);
			if (param != null && !"".equals(param)) {
				// 向服务器写出数据流
				OutputStream os = conn.getOutputStream();
				os.write(param.getBytes());
				os.flush();
			}
			InputStream is = conn.getInputStream();
			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			actual = br.readLine();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			try {
				System.out.println("post请求参数：" + param);
				System.out.println("异常情况" + e);
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return actual;
	}
}
