package core.configs;

public class CartServiceIntegrationTimeoutProperties {
    private Long read;
    private Long write;
    private Integer connection;

    public Long getRead() {
        return read;
    }

    public void setRead(Long read) {
        this.read = read;
    }

    public Long getWrite() {
        return write;
    }

    public void setWrite(Long write) {
        this.write = write;
    }

    public Integer getConnection() {
        return connection;
    }

    public void setConnection(Integer connection) {
        this.connection = connection;
    }
}
