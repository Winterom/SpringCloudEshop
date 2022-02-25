package cart.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;


@ConstructorBinding
@ConfigurationProperties(prefix = "integrations.core-service")
public class CoreServiceIntegrationProperty {
    private String coreServiceUrl;
    private Long readTimeout;
    private Long writeTimeout;
    private Integer connectionTimeout;

    public CoreServiceIntegrationProperty() {
    }

    public CoreServiceIntegrationProperty(String coreServiceUrl, Long readTimeout, Long writeTimeout, Integer connectionTimeout) {
        this.coreServiceUrl = coreServiceUrl;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
        this.connectionTimeout = connectionTimeout;
    }

    public String getCoreServiceUrl() {
        return coreServiceUrl;
    }

    public void setCoreServiceUrl(String coreServiceUrl) {
        this.coreServiceUrl = coreServiceUrl;
    }

    public Long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Long readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Long getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(Long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
