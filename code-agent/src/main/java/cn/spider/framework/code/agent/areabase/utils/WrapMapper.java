package cn.spider.framework.code.agent.areabase.utils;

import cn.hutool.core.util.StrUtil;

public class WrapMapper {

	/**
	 * Instantiates a new wrap mapper.
	 */
	private WrapMapper() {
	}

	/**
	 * Wrap.
	 *
	 * @param <E>     the element type
	 * @param code    the code
	 * @param message the message
	 * @param o       the o
	 *
	 * @return the wrapper
	 */
	public static <E> Wrapper<E> wrap(int code, String message, E o) {
		return new Wrapper<>(code, message, o);
	}

	/**
	 * Wrap.
	 *
	 * @param <E>     the element type
	 * @param code    the code
	 * @param message the message
	 *
	 * @return the wrapper
	 */
	public static <E> Wrapper<E> wrap(int code, String message) {
		return wrap(code, message, null);
	}

	/**
	 * Wrap.
	 *
	 * @param <E>  the element type
	 * @param code the code
	 *
	 * @return the wrapper
	 */
	public static <E> Wrapper<E> wrap(int code) {
		return wrap(code, null);
	}

	/**
	 * Wrap.
	 *
	 * @param <E> the element type
	 * @param e   the e
	 *
	 * @return the wrapper
	 */
	public static <E> Wrapper<E> wrap(Exception e) {
		return new Wrapper<>(Wrapper.ERROR_CODE, e.getMessage());
	}

	/**
	 * Un wrapper.
	 *
	 * @param <E>     the element type
	 * @param wrapper the wrapper
	 *
	 * @return the e
	 */
	public static <E> E unWrap(Wrapper<E> wrapper) {
		return wrapper.getResult();
	}

	/**
	 * Wrap ERROR. code=400
	 *
	 * @param <E> the element type
	 *
	 * @return the wrapper
	 */
	public static <E> Wrapper<E> illegalArgument() {
		return wrap(Wrapper.ILLEGAL_ARGUMENT_CODE_, Wrapper.ILLEGAL_ARGUMENT_MESSAGE);
	}

	/**
	 * Wrap ERROR. code=400
	 *
	 * @param <E>     the type parameter
	 * @param message the message
	 *
	 * @return the wrapper
	 */
	public static <E> Wrapper<E> illegalArgument(String message) {
		return wrap(Wrapper.ILLEGAL_ARGUMENT_CODE_, Wrapper.ILLEGAL_ARGUMENT_MESSAGE + ":" + message);
	}

	/**
	 * Wrap ERROR. code=500
	 *
	 * @param <E> the element type
	 *
	 * @return the wrapper
	 */
	public static <E> Wrapper<E> error() {
		return wrap(Wrapper.ERROR_CODE, Wrapper.ERROR_MESSAGE);
	}


	/**
	 * Error wrapper.
	 *
	 * @param <E>     the type parameter
	 * @param message the message
	 *
	 * @return the wrapper
	 */
	public static <E> Wrapper<E> error(String message) {
		return wrap(Wrapper.ERROR_CODE, StrUtil.isBlank(message) ? Wrapper.ERROR_MESSAGE : message);
	}

	/**
	 * Wrap SUCCESS. code=200
	 *
	 * @param <E> the element type
	 *
	 * @return the wrapper
	 */
	public static <E> Wrapper<E> ok() {
		return new Wrapper<>();
	}

	/**
	 * Wrap SUCCESS. code=201
	 *
	 * @param <E> the element type
	 *
	 * @return the wrapper
	 */
	public static <E> Wrapper<E> param() {
		return new Wrapper<>();
	}

	/**
	 * Ok wrapper.
	 *
	 * @param <E> the type parameter
	 * @param o   the o
	 *
	 * @return the wrapper
	 */
	public static <E> Wrapper<E> ok(E o) {
		return new Wrapper<>(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, o);
	}
}
