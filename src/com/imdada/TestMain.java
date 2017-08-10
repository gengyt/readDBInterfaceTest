package com.imdada;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TestMain {

	public static void main(String[] args) {
		HttpRequestUtils http = new HttpRequestUtils();
		MySqlConn sql = new MySqlConn();
		// 存放所有需要传递的参数
		Map<String, String> map = new HashMap<String, String>();
		// 获取所有需要执行的接口
		List<Api> listApi = sql.selectApi();
		String result = "";
		try {

			for (int i = 0; i < listApi.size(); i++) {
				List<ApiCase> listApiCase = new ArrayList<ApiCase>();
				// 获取一个接口的所有测试用例
				listApiCase = sql.selectApiCase(listApi.get(i).getApiId());

				if (listApiCase.size() > 0) {
					sql.clear(listApi.get(i).getApiId());
				}
				for (int j = 0; j < listApiCase.size(); j++) {
					// 获取整合后的参数
					String params = getParams(listApiCase, j, map);

					if ("POST".equals(listApi.get(i).getMethod().toUpperCase())) {
						result = http.sendPOST(listApi.get(i).getUrl(), listApi.get(i).getIsJson(), params);
					}
					if ("GET".equals(listApi.get(i).getMethod().toUpperCase())) {
						result = http.sendGet(listApi.get(i).getUrl() + params);
					}

					System.out.println("case_id：" + listApiCase.get(j).getCaseId());
					System.out.println(listApiCase.get(j).getName() + "url:" + listApi.get(i).getUrl());
					System.out.println("参数：" + params);
					System.out.println("返回信息：" + result);

					// 把所有结果更新到数据库result字段
					sql.updateCaseResult(result, listApiCase.get(j).getCaseId());

					// 把数据库的next_case_params值存入map
					putInMap(j, listApiCase, result, map);

					// 实际结果与期待信息中不符合的部分更新api_params.expected_diff_result字段
					sql.updateErrorCase(errDiffMessage(j, listApiCase, result), listApiCase.get(j).getCaseId());
				}
			}

		} catch (Exception e) {
			System.out.println("程序出问题了，请联系相关人员解决");
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 把字符串中的参数替换为实际值。
	 * eg：参数为a=1&b=2&c=$key,其中$key是之前接口返回结果中的值，假如key=3，替换后变为：a=1&b=2&c=3
	 * 
	 * @param listApiCase
	 * @param j
	 * @param map
	 * @return
	 */
	public static String getParams(List<ApiCase> listApiCase, int j, Map<String, String> map) {
		String params = "";
		// 判断参数中是否含有需要取上一个接口返回值作为参数的信息
		if (listApiCase.get(j).getParams().trim().contains("$")) {
			params = listApiCase.get(j).getParams();
			// 循环判断需要传递的参数是否在map中存在
			for (Entry<String, String> entry : map.entrySet()) {
				String key = "$" + entry.getKey();
				// 有匹配的参数
				if (listApiCase.get(j).getParams().contains(key)) {
					params = params.replace(key, entry.getValue());
				}
			}
		} else {
			params = listApiCase.get(j).getParams().trim();
		}

		return params;
	}

	/**
	 * 获取实际结果与期待信息中不符合的错误信息
	 * 
	 * @param j
	 * @param listApiCase
	 * @param result
	 * @return 错误信息
	 */
	public static String errDiffMessage(int j, List<ApiCase> listApiCase, String result) {
		// 更新expected值，以逗号分割
		String[] excep = listApiCase.get(j).getExpected().split(",");
		List<String> list = new ArrayList<String>();
		// 分割后的信息存入list
		for (String str : excep) {
			list.add(str);
		}
		String errMes = "";
		// 循环所有的期待结果，与实际结果比较
		for (int k = 0; k < list.size(); k++) {
			// 更新实际值与期待值不符的case
			if (!result.contains(list.get(k))) {
				errMes += list.get(k) + ",";
			}
		}
		return errMes;
	}

	/**
	 * 把数据库的next_case_params值存入map
	 * 
	 * @param j
	 * @param listApiCase
	 * @param result
	 * @param map
	 * @return
	 */
	public static Map<String, String> putInMap(int j, List<ApiCase> listApiCase, String result,
			Map<String, String> map) {
		String ncp = listApiCase.get(j).getNextCaseParams();
		// 判断是否有参数传递
		if (ncp != null && !"".equals(ncp)) {
			// 多参数的情况
			String[] strNcp = ncp.split(",");
			for (String str : strNcp) {
				String first = result.substring(result.indexOf(str));
				String second = first;
				if (first.indexOf(",") > 1) {
					second = first.substring(0, first.indexOf(","));
				} else {
					second = first.substring(0, first.length() - 1);
				}
				String third = second.replace(str, "");
				String fourth = third.replace(":", "");
				String end = fourth.replaceAll("\"", "");
				// 把需要传递的参数和值存入map
				map.put(str, end);
			}
		}
		return map;
	}
}
