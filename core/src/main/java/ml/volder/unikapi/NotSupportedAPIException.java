package ml.volder.unikapi;

public class NotSupportedAPIException extends NotSupportedException{
    private String apiName;
    private String packageName;
    private String interfaceName;

    public NotSupportedAPIException(String apiName, String packageName, String interfaceName) {
        this.apiName = apiName;
        this.packageName = packageName;
        this.interfaceName = interfaceName;
    }

    public NotSupportedAPIException(String message, String apiName, String packageName, String interfaceName) {
        super(message);
        this.apiName = apiName;
        this.packageName = packageName;
        this.interfaceName = interfaceName;
    }

    public NotSupportedAPIException(String message, Throwable cause, String apiName, String packageName, String interfaceName) {
        super(message, cause);
        this.apiName = apiName;
        this.packageName = packageName;
        this.interfaceName = interfaceName;
    }

    public NotSupportedAPIException(Throwable cause, String apiName, String packageName, String interfaceName) {
        super(cause);
        this.apiName = apiName;
        this.packageName = packageName;
        this.interfaceName = interfaceName;
    }


    @Override
    public void printStackTrace() {
        super.printStackTrace();
        UnikAPI.LOGGER.warning("[UNIKAPI]\n" +
                "[UNIKAPI] -----------------------------------------\n" +
                "[UNIKAPI]\n" +
                "[UNIKAPI] Kunne ikke finde en understøttet version af\n" +
                "[UNIKAPI] " + apiName + " i packagen (" + packageName + ")\n" +
                "[UNIKAPI] " + apiName + " i packagen (" + packageName + ")\n" +
                "[UNIKAPI]\n" +
                "[UNIKAPI] Ingen implementaioner matchede med følgende data:\n" +
                "[UNIKAPI] clientBrand: " + UnikAPI.getClientBrand() + "\n" +
                "[UNIKAPI] otherVersion: " + UnikAPI.getOtherVersion() + "\n" +
                "[UNIKAPI] minecraftVersion: " + UnikAPI.getMinecraftVersion() + "\n" +
                "[UNIKAPI] søgte efter interface: " + interfaceName + "\n" +
                "[UNIKAPI]\n" +
                "[UNIKAPI] -----------------------------------------\n" +
                "[UNIKAPI]"
        );
    }
}
