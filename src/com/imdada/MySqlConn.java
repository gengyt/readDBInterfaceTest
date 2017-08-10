package com.imdada;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySqlConn {

	public Statement sqlConn() {
		Connection conn;
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://192.168.1.250:3307?characterEncoding=utf8&useSSL=false";
		String user = "dev_w";
		String password = "6nvjq0_HW";
		Statement statement = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
			statement = conn.createStatement();

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return statement;
	}

	/**
	 * 查询所有需要执行的接口
	 * 
	 * @return 返回接口结果集
	 */
	public List<Api> selectApi() {
		List<Api> listApi = new ArrayList<Api>();
		Statement stat = sqlConn();
		// 查询所有要执行的接口
		String sql = "select api_id,url,method,is_json from test_team.api;";
		ResultSet rs = null;
		try {
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				Api api = new Api();
				api.setApiId(rs.getString("api_id"));
				api.setUrl(rs.getString("url"));
				api.setMethod(rs.getString("method"));
				api.setIsJson(rs.getString("is_json"));
				listApi.add(api);
			}

			rs.close();
			stat.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return listApi;
	}

	/**
	 * 查询所有接口测试用例
	 * 
	 * @return 接口测试用例结果集
	 */
	public List<ApiCase> selectApiCase(String apiId) {
		List<ApiCase> listApiCase = new ArrayList<ApiCase>();
		Statement stat = sqlConn();
		// 查询所有要执行的接口
		String sql = "select ap.name,ap.case_id,ap.params,ap.expected,ap.result,ap.is_run,ap.next_case_params from test_team.api,test_team.api_params ap "
				+ "where api.api_id = ap.api_id  and ap.is_run = 'Y' and ap.api_id='" + apiId + "';";
		ResultSet rs = null;
		try {
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				ApiCase apiCase = new ApiCase();
				apiCase.setName(rs.getString("name"));
				apiCase.setCaseId(rs.getString("case_id"));
				apiCase.setParams(rs.getString("params"));
				apiCase.setExpected(rs.getString("expected"));
				apiCase.setIsRun(rs.getString("is_run"));
				apiCase.setNextCaseParams(rs.getString("next_case_params"));
				
				listApiCase.add(apiCase);
			}

			rs.close();
			stat.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return listApiCase;
	}

	/**
	 * 更新结果
	 * 
	 * @param result
	 * @param caseId
	 */
	public void updateCaseResult(String result, String caseId) {
		Statement stat = sqlConn();
		String sql = "update test_team.api_params set result='" + result + "' where case_id = " + caseId + ";";
		try {
			stat.executeUpdate(sql);

			stat.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 更新实际值与期待值不符的case
	 * 
	 * @param result
	 * @param caseId
	 */
	public void updateErrorCase(String edr, String caseId) {
		Statement stat = sqlConn();
		String sql = "update test_team.api_params set expected_diff_result='" + edr + "' where case_id = " + caseId
				+ ";";
		try {
			stat.executeUpdate(sql);

			stat.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 清空要所有要执行的测试case的result、expected_diff_result
	 * 
	 */
	public void clear(String apiId) {
		Statement stat = sqlConn();
		String sqlUpdate = "update test_team.api_params set result='',expected_diff_result='' where api_id = '" + apiId
				+ "' and is_run in ('Y','y');";
		try {
			// 先更新result、expected_diff_result为空
			stat.executeUpdate(sqlUpdate);

			stat.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 查询DB
	 * 
	 * @return
	 */
	// public List<ApiCase> inExceptedSql(String sql) {
	// List<ApiCase> listApiCase = new ArrayList<ApiCase>();
	// Statement stat = sqlConn();
	// ResultSet rs = null;
	// try {
	// rs = stat.executeQuery(sql);
	// while(rs.next()) {
	// ApiCase apiCase = new ApiCase();
	// apiCase.setCaseId(rs.getString("case_id"));
	// apiCase.setParams(rs.getString("params"));
	// apiCase.setExpected(rs.getString("expected"));
	// apiCase.setIsRun(rs.getString("is_run"));
	// listApiCase.add(apiCase);
	// }
	// } catch (SQLException e) {
	// try {
	// rs.close();
	// stat.close();
	// } catch (SQLException e1) {
	// e1.printStackTrace();
	// }
	// e.printStackTrace();
	// }
	// return listApiCase;
	// }
}
