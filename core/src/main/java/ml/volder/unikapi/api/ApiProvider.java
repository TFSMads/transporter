package ml.volder.unikapi.api;

public class ApiProvider<T> {

    private String apiName;
    private T api;

    public ApiProvider(String apiName) {
        this.apiName = apiName;
    }

    public void setApi(T api) {
        this.api = api;
    }

    public T getApi() {
        return this.api;
    }

    /**
     * @return Returns true if the api is not yet specified
     */
    public boolean isApiSpecified() {
        return api != null;
    }

    public String getApiName() {
        return this.apiName;
    }
}
