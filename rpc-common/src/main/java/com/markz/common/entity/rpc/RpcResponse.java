package com.markz.common.entity.rpc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Rpc 响应实体类
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {

    private static final long serialVersionUID = 4792148280206835587L;

    /**
     * 请求 ID（响应的哪个请求）
     */
    private String requestId;

    /**
     * 响应数据
     */
    private Object result;

    /**
     * 响应类型
     */
    private Class<?> resultType;

    /**
     * 错误信息
     */
    private Exception exception;
}
