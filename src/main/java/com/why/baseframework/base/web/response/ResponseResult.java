
package com.why.baseframework.base.web.response;



public class ResponseResult<T> {

	/**
	 * 返回代码
	 */
	private int resCode;

	/**
	 * 返回信息
	 */
	private String resMsg;

	/**
	 * 详细信息
	 */
	private String detailMsg;

	/**
	 * 返回数据
	 */
	private T resData;

	/**
	 * @return the resCode
	 */
	public int getResCode() {
		return resCode;
	}

	/**
	 * @param resCode
	 *            the resCode to set
	 */
	public void setResCode(int resCode) {
		this.resCode = resCode;
	}

	/**
	 * @return the resMsg
	 */
	public String getResMsg() {
		return resMsg;
	}

	/**
	 * @param resMsg
	 *            the resMsg to set
	 */
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	/**
	 * @return the detailMsg
	 */
	public String getDetailMsg() {
		return detailMsg;
	}

	/**
	 * @param detailMsg
	 *            the detailMsg to set
	 */
	public void setDetailMsg(String detailMsg) {
		this.detailMsg = detailMsg;
	}

	/**
	 * @return the resData
	 */
	public T getResData() {
		return resData;
	}

	/**
	 * @param resData
	 *            the resData to set
	 */
	public void setResData(T resData) {
		this.resData = resData;
	}

	@Override
	public String toString() {
		return "ResponseResult [resCode=" + resCode + ", resMsg=" + resMsg + ", detailMsg=" + detailMsg + ", resData="
				+ resData + "]";
	}

}
