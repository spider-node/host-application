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
package cn.spider.framework.host.application.base.plugin.param;

import javax.annotation.Nonnull;

/**
 * Identity
 *
 * @author dds
 */
public interface Identity {

    /**
     * 获取 资源ID
     *
     * @return 资源ID
     */
    @Nonnull
    String getIdentityId();

    /**
     * 获取 资源类型
     *
     * @return 资源类型
     */
    @Nonnull
    IdentityTypeEnum getIdentityType();
}
