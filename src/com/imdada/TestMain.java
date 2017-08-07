package com.imdada;


import java.util.List;

public class TestMain {

	public static void main(String[] args) {
		HttpRequestUtils http = new HttpRequestUtils();
		MySqlConn sql = new MySqlConn();
		// 获取所有需要执行的接口
		List<Api> listApi = sql.selectApi();
		// 获取所有接口测试用例
		List<ApiCase> listApiCase;
		String result = "";
		for (int i = 0; i < listApi.size(); i++) {
			listApiCase = sql.selectApiCase(listApi.get(i).getApiId());
			for (int j = 0; j < listApiCase.size(); j++) {
				if ("POST".equals(listApi.get(i).getMethod().toUpperCase())) {
					result = http.sendPOST(listApi.get(i).getUrl(), listApi.get(i).getIsJson(),
							listApiCase.get(j).getParams());
				}
				if ("GET".equals(listApi.get(i).getMethod().toUpperCase())) {
					if ("".equals(listApiCase.get(j).getParams())) {
						result = http.sendGet(listApi.get(i).getUrl());
					} else {
						result = http.sendGet(listApi.get(i).getUrl() + listApiCase.get(j).getParams());
					}
					result = http.sendGet(listApi.get(i).getUrl() + listApiCase.get(j).getParams());
				}
				// 把结果更新到数据库
				sql.updateCaseResult(result, listApiCase.get(j).getCaseId());
			}
		}
	}
}
