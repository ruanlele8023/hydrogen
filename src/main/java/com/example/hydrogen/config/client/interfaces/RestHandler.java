package com.example.hydrogen.config.client.interfaces;

import com.example.hydrogen.data.bean.MethodInfoBean;
import com.example.hydrogen.data.bean.ServerInfoBean;

public interface RestHandler {
    /**
     * 初始化服务器信息
     * @param serverInfo
     */
    void init(ServerInfoBean serverInfo);

    /**
     * 调用 rest 请求, 返回接口
     * @param serverInfo
     * @param methodInfo
     * @return
     */
    Object invokeRest(ServerInfoBean serverInfo, MethodInfoBean methodInfo);
}
