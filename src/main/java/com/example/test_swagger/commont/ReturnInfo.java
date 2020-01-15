package com.example.test_swagger.commont;


import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @ClassName: ReturnInfo
 * @Description: TODO
 * @author lovefamily
 * @date 2018年6月21日 下午5:19:53
 *
 */
public class ReturnInfo extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public ReturnInfo() {
		put("code", 0);
	}

	public static ReturnInfo error() {
		return error(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}

	public static ReturnInfo error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}


	public static ReturnInfo error(int code, String msg) {
		ReturnInfo r = new ReturnInfo();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static ReturnInfo ok(String msg) {
		ReturnInfo r = new ReturnInfo();
		r.put("msg", msg);
		return r;
	}

	public static ReturnInfo ok(Map<String, Object> map) {
		ReturnInfo r = new ReturnInfo();
		r.putAll(map);
		return r;
	}

	public static ReturnInfo oJ8K() {
		return new ReturnInfo();
	}

	public ReturnInfo put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
