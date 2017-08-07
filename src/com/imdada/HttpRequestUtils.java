package com.imdada;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
		String actual = null;
		try {
			// 获取url对象
			URL url = new URL(pathParam);
			// 打开链接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置提交类型
			conn.setRequestMethod("GET");
			// 获取相应数据
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			actual = br.readLine();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
			URLConnection conn = url.openConnection();
			// 是否为json格式
			if ("Y".equals(isJson.toUpperCase())) {
				conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			}
			// 发送post请求必须设置如下两行,设置了这两行，就可以对URL连接进行输入/输出
			conn.setDoInput(true);
			conn.setDoOutput(true);
			// 向服务器写出数据流
			OutputStream os = conn.getOutputStream();
			os.write(param.getBytes());
			os.flush();
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			actual = br.readLine();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			try {
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return actual;
	}
}
