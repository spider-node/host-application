/*
 *
 *  * Copyright (c) 2020-2023, Lykan (jiashuomeng@gmail.com).
 *  * <p>
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  * <p>
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  * <p>
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package cn.spider.framework.host.application.base.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;


/**
 * @author lykan
 */
public class ProxyUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyUtil.class);

    public static boolean isProxyObject(Object o) {
        if (o == null) {
            return false;
        }
        return AopUtils.isAopProxy(o) || AopUtils.isCglibProxy(o) || AopUtils.isJdkDynamicProxy(o);
    }

    public static Class<?> noneProxyClass(Object candidate) {
        if (candidate == null) {
            return null;
        }
        if (!ProxyUtil.isProxyObject(candidate)) {
            return candidate.getClass();
        }
        return AopUtils.getTargetClass(candidate);
    }
}
