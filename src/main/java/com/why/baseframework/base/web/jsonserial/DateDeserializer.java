
package com.why.baseframework.base.web.jsonserial;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.why.baseframework.constants.DateConstants;
import com.why.baseframework.constants.IntConstants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author W
 * @Description:
 * @Title: DateConstants
 * @ProjectName WHY-Core
 * @Date 2021/4/19
 * @Company  WHY-Group
 */
public class DateDeserializer extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		if (StringUtils.isBlank(jp.getText())) {
			return null;
		}

		String strDate = jp.getText().trim();
		Date dtDate;

		try {
			// 初始化大写T和Z
			char capitalT = IntConstants.INT_84;
			char capitalZ = IntConstants.INT_90;
			// 初始化短横线
			char hyphen = IntConstants.INT_45;
			// 初始化冒号
			char doppelpunkt = IntConstants.INT_58;
			// 初始化斜线
			char diagonal = IntConstants.INT_47;

			// 格式化器
			SimpleDateFormat formatter;
			if (strDate.contains(String.valueOf(capitalT)) && strDate.endsWith(String.valueOf(capitalZ))) {
				formatter = new SimpleDateFormat(DateConstants.DATE_TIME_UTC_FORMAT);
				dtDate = formatter.parse(strDate.replace(String.valueOf(capitalZ), DateConstants.UTC_STR));
				return dtDate;
			} else if (strDate.contains(String.valueOf(hyphen))) {
				if (strDate.contains(String.valueOf(doppelpunkt))) {
					formatter = new SimpleDateFormat(DateConstants.DEFAULT_DATE_TIME_FORMAT);
				} else {
					formatter = new SimpleDateFormat(DateConstants.DEFAULT_DATE_FORMAT);
				}
				dtDate = formatter.parse(strDate);
				return dtDate;
			} else if (strDate.contains(String.valueOf(diagonal))) {
				if (strDate.contains(String.valueOf(doppelpunkt))) {
					formatter = new SimpleDateFormat(DateConstants.DATE_TIME_OBLIQUE_FORMAT);
				} else {
					formatter = new SimpleDateFormat(DateConstants.DATE_OBLIQUE_FORMAT);
				}
				dtDate = formatter.parse(strDate);
				return dtDate;
			}
		} catch (Exception e) {
			throw new RuntimeException(String.format("parser %s to Date fail", strDate));
		}

		throw new RuntimeException(String.format("parser %s to Date fail", strDate));
	}

}
