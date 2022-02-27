package cart.configs;

import lombok.Data;



@Data
public class CoreServiceIntegrationProperty {
    private String coreServiceUrl="http://localhost:5555/core";
    private Long readTimeout= 2000L;
    private Long writeTimeout = 2000L;
    private Integer connectionTimeout = 10000;

}
