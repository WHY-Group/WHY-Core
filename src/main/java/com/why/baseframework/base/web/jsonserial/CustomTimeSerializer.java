
package com.why.baseframework.base.web.jsonserial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.why.baseframework.constants.DateConstants;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author W
 * @Description:
 * @Title: CorsConstants
 * @ProjectName WHY-Core
 * @Date 2021/4/19
 * @Company  WHY-Group
 */
public class CustomTimeSerializer extends JsonSerializer<Date> {

	/**
	 * 日期格式化
	 */
	private final SimpleDateFormat sdf = new SimpleDateFormat(DateConstants.DEFAULT_DATE_TIME_FORMAT);


	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers) throws IOException {

		String strDate = "";
		if (date != null) {
			strDate = sdf.format(date);
		}
		gen.writeString(strDate);

	}

}
