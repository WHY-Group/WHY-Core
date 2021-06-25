package com.why.baseframework.base.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @Author Y
 * @Description: 将地区标记修改放在header中
 * @Title: LocalChangleInterceptor
 * @ProjectName baseframework
 * @Date 2021/4/19
 * @Company WHY-Group
 */
@Slf4j
public class CustomerLocalChangeInterceptor implements HandlerInterceptor {

	/**
	 * Default name of the locale specification parameter: "locale".
	 */
	public static final String DEFAULT_PARAM_NAME = "locale";

	@Nullable
	private String[] httpMethods;

	private boolean ignoreInvalidLocale = false;

	/**
	 * Return the configured HTTP methods.
	 *
	 * @since 4.2
	 */
	@Nullable
	public String[] getHttpMethods() {
		return this.httpMethods;
	}

	/**
	 * Return whether to ignore an invalid value for the locale parameter.
	 *
	 * @since 4.2.2
	 */
	public boolean isIgnoreInvalidLocale() {
		return this.ignoreInvalidLocale;
	}

	/**
	 * Specify whether to parse request parameter values as BCP 47 language tags
	 * instead of Java's legacy locale specification format.
	 * <p>
	 * <b>NOTE: As of 5.1, this resolver leniently accepts the legacy
	 * {@link Locale#toString} format as well as BCP 47 language tags.</b>
	 *
	 * @see Locale#forLanguageTag(String)
	 * @see Locale#toLanguageTag()
	 * @since 4.3
	 * @deprecated as of 5.1 since it only accepts {@code true} now
	 */
	@Deprecated
	public void setLanguageTagCompliant(boolean languageTagCompliant) {
		if (!languageTagCompliant) {
			throw new IllegalArgumentException("LocaleChangeInterceptor always accepts BCP 47 language tags");
		}
	}

	/**
	 * Return whether to use BCP 47 language tags instead of Java's legacy locale
	 * specification format.
	 *
	 * @since 4.3
	 * @deprecated as of 5.1 since it always returns {@code true} now
	 */
	@Deprecated
	public boolean isLanguageTagCompliant() {
		return true;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {

		String newLocale = request.getHeader(DEFAULT_PARAM_NAME);
		if (newLocale != null) {
			if (checkHttpMethod(request.getMethod())) {
				LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
				if (localeResolver == null) {
					throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
				}
				try {
					localeResolver.setLocale(request, response, parseLocaleValue(newLocale));
				} catch (IllegalArgumentException ex) {
					if (isIgnoreInvalidLocale()) {
						if (log.isDebugEnabled()) {
							log.debug("Ignoring invalid locale value [" + newLocale + "]: " + ex.getMessage());
						}
					} else {
						throw ex;
					}
				}
			}
		}
		// Proceed in any case.
		return true;
	}

	private boolean checkHttpMethod(String currentMethod) {
		String[] configuredMethods = getHttpMethods();
		if (ObjectUtils.isEmpty(configuredMethods)) {
			return true;
		}
		for (String configuredMethod : configuredMethods) {
			if (configuredMethod.equalsIgnoreCase(currentMethod)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Parse the given locale value as coming from a request parameter.
	 * <p>
	 * The default implementation calls {@link StringUtils#parseLocale(String)},
	 * accepting the {@link Locale#toString} format as well as BCP 47 language tags.
	 *
	 * @param localeValue the locale value to parse
	 * @return the corresponding {@code Locale} instance
	 * @since 4.3
	 */
	@Nullable
	protected Locale parseLocaleValue(String localeValue) {
		return StringUtils.parseLocale(localeValue);
	}

}
