package com.github.samyuan1990.FabricJavaPool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class FabricJavaPoolConfig extends GenericObjectPoolConfig {

    private static String configFile = "/FabricJavaPool.properties";

    public void setConfigNetworkPath(String configNetworkPath) {
        this.configNetworkPath = configNetworkPath;
    }

    public String getConfigNetworkPath() {
        return configNetworkPath;
    }

    private String configNetworkPath;
    private Properties properties;

    public void setWalletPath(String walletPath) {
        this.walletPath = walletPath;
    }

    public String getWalletPath() {
        return walletPath;
    }

    private String walletPath;

    public FabricJavaPoolConfig(String configNetworkPath) {
        super();
        this.setConfigNetworkPath(configNetworkPath);
    }

    public FabricJavaPoolConfig() {
        loadConfig(configFile);
    }

    public void loadConfig(String file) {
        try {
            InputStream in = this.getClass().getResourceAsStream(file);
            Properties properties = new Properties();
            properties.load(in);
            this.setConfigNetworkPath(properties.getProperty("configNetworkPath"));
            this.setWalletPath(properties.getProperty("walletPath"));
            this.setMaxTotal(Integer.valueOf(properties.getProperty("maxTotal")).intValue());
            this.setMaxIdle(Integer.valueOf(properties.getProperty("maxIdle")).intValue());
            this.setMinIdle(Integer.valueOf(properties.getProperty("minIdle")).intValue());
            this.setMaxWaitMillis(Integer.valueOf(properties.getProperty("maxWaitMillis")).intValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
