public interface PortMappingListener {
    void onSuccess();
    void onFailure(String reason);
}