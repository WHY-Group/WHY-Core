package com.why.baseframework.base.web.response;

/**
 * @author W
 */
public final class ResponseUtils {
	private ResponseUtils() {
	}

	public static <T> ResponseResult<T> success(T data) {
		ResponseResult<T> result = new ResponseResult<T>();
		result.setResCode(0);
		result.setResMsg("OK");
		result.setResData(data);
		return result;
	}

	public static <T> ResponseResult<T> success(T data, String msg) {
		ResponseResult<T> result = new ResponseResult<T>();
		result.setResCode(0);
		result.setResMsg(msg);
		result.setResData(data);
		return result;
	}

	public static <T> ResponseResult<T> successMsg(String msg) {
		ResponseResult<T> result = new ResponseResult<T>();
		result.setResCode(0);
		result.setResMsg(msg);
		result.setResData(null);
		return result;
	}
	public static <T> ResponseResult<T> success() {
		ResponseResult<T> result = new ResponseResult<T>();
		result.setResCode(0);
		result.setResMsg("OK");
		return result;
	}

	public static <T> ResponseResult<T> fail(int errCode, String errMsg) {
		ResponseResult<T> result = new ResponseResult<T>();
		result.setResCode(errCode);
		result.setResMsg(errMsg);
		return result;

	}

}
