package com.dinosaur.reactive.webclient.core.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@ConfigurationProperties("hrpc")
public class HRpcConfiguration {

    private int maxInMemorySize = 256;
    private int connect_timeout_mills = 10000;
    private int responseTimeout = 5000;
    private Boolean keepalive;//是否开启keepalive
    private int tcp_keepidle = 300;//单位为秒
    private int tcp_keepintval = 60;//单位为秒
    private int tcp_keepcnt = 8;//最大探测数

    public int getMaxInMemorySize() {
        return maxInMemorySize;
    }

    public void setMaxInMemorySize(int maxInMemorySize) {
        this.maxInMemorySize = maxInMemorySize;
    }

    public int getConnect_timeout_mills() {
        return connect_timeout_mills;
    }

    public void setConnect_timeout_mills(int connect_timeout_mills) {
        this.connect_timeout_mills = connect_timeout_mills;
    }

    public int getResponseTimeout() {
        return responseTimeout;
    }

    public void setResponseTimeout(int responseTimeout) {
        this.responseTimeout = responseTimeout;
    }

    public Boolean getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(Boolean keepalive) {
        this.keepalive = keepalive;
    }

    public int getTcp_keepidle() {
        return tcp_keepidle;
    }

    public void setTcp_keepidle(int tcp_keepidle) {
        this.tcp_keepidle = tcp_keepidle;
    }

    public int getTcp_keepintval() {
        return tcp_keepintval;
    }

    public void setTcp_keepintval(int tcp_keepintval) {
        this.tcp_keepintval = tcp_keepintval;
    }

    public int getTcp_keepcnt() {
        return tcp_keepcnt;
    }

    public void setTcp_keepcnt(int tcp_keepcnt) {
        this.tcp_keepcnt = tcp_keepcnt;
    }
}
