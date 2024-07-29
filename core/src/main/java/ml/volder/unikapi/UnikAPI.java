package ml.volder.unikapi;

import ml.volder.unikapi.api.ApiReferenceStorage;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.logger.DefaultLogger;
import ml.volder.unikapi.logger.Logger;

import java.io.File;
import java.util.UUID;

public class UnikAPI {

    public static Logger LOGGER = new DefaultLogger("UnikAPI");

    // Client brand - Examples:
    // - labymod
    // - forge
    // - fabric
    // - etc
    private static String clientBrand;
    // If the current client has versions with major differences this may be required. Set this to null if
    // there isn't any version with major differences.
    // For example this could differentiate labymod-3 from labymod-4
    private static String otherVersion;
    //Minecraft version - Examples:
    // - 1.8.8
    // - 1.12.2
    // - etc
    private static String minecraftVersion;

    //Minecraft- and other version can contain a * if all numbers is allowed. NOTE: this only works if the version is sepperated with dots. Here is an example:
    //
    // if the minecraft version is set to 1.8.*, all the following versions would be matched.
    // - 1.8
    // - 1.8.1
    // - 1.8.2
    // - 1.8.3
    // - 1.8.4
    // - 1.8.5
    // - 1.8.6
    // - 1.8.7
    // - 1.8.8
    // - 1.8.9

    private static ApiReferenceStorage apiReferenceStorage;

    public static void registerReferenceStorage(ApiReferenceStorage apiReferenceStorage) {
        UnikAPI.apiReferenceStorage = apiReferenceStorage;
    }

    public static ApiReferenceStorage getApiReferenceStorage() {
        return apiReferenceStorage;
    }

    public static boolean isReferenceStorageSpecified() {
        return apiReferenceStorage != null;
    }

    public static void initAPI(String clientBrand, String otherVersion, String minecraftVersion) {
        UnikAPI.clientBrand = clientBrand;
        UnikAPI.otherVersion = otherVersion;
        UnikAPI.minecraftVersion = minecraftVersion;
    }

    public static boolean matchBrand(String clientBrand) {
        return UnikAPI.clientBrand.equals(clientBrand);
    }

    private static boolean matchDotSepperatedVersion(String matchVersion, String versionToCheck) {
        String[] subVersionsMatch = matchVersion.split("\\.");
        String[] subVersionsCheck = versionToCheck.split("\\.");
        int index = -1;
        for (String subVersion : subVersionsMatch) {
            index++;
            String subVersionCheck = subVersionsCheck[index] != null ? subVersionsCheck[index] : "*";
            if(subVersion.equals("*") || subVersionCheck.equals("*")){
                if(subVersionsMatch.length - 1 >= index + 1)
                    continue;
                return true;
            }
            if(subVersionsCheck.length - 1 >= index && subVersionsCheck[index].equals(subVersion))
                continue;
            return false;
        }
        return true;
    }

    public static boolean matchOtherVersion(String otherVersion) {
        if(UnikAPI.otherVersion == null || otherVersion.equals("*"))
            return true;
        return matchDotSepperatedVersion(UnikAPI.otherVersion, otherVersion);
    }

    public static boolean matchMinecraftVersion(String[] minecraftVersion) {
        for(String version : minecraftVersion) {
            if(matchDotSepperatedVersion(UnikAPI.minecraftVersion, version))
                return true;
        }
        return false;
    }

    public static boolean isSupported(SupportedClient annotation) {
        return matchBrand(annotation.clientBrand()) && matchOtherVersion(annotation.otherVersion()) && matchMinecraftVersion(annotation.minecraftVersion());
    }

    public static String getClientBrand() {
        return clientBrand;
    }

    public static String getOtherVersion() {
        return otherVersion;
    }

    public static String getMinecraftVersion() {
        return minecraftVersion;
    }

    private static File commonDataFolder;
    private static File playerDataFolder;
    private static UUID lastUUID;

    private static boolean hasUUIDChanged() {
        if(lastUUID == null)
            return true;
        return !lastUUID.equals(PlayerAPI.getAPI().getUUID());
    }

    private static void initDataFolders() {
        File file = new File("TransporterAddon/");
        file.mkdirs();
        commonDataFolder = file;
        if(lastUUID != null){
            file = new File("TransporterAddon/" + PlayerAPI.getAPI().getUUID());
            file.mkdirs();
            playerDataFolder = file;
        }
    }

    public static File getCommonDataFolder() {
        if (commonDataFolder == null)
            initDataFolders();
        return commonDataFolder;
    }

    public static File getPlayerDataFolder() {
        if(hasUUIDChanged()){
            lastUUID = PlayerAPI.getAPI().getUUID();
            initDataFolders();
        }
        return playerDataFolder;
    }

    private static class UpdateInfoJson {
        public String version;
    }

    /*public static boolean isUpToDate() {
        Gson gson = new Gson();
        UpdateInfoJson localUpdateInfo;
        UpdateInfoJson remoteUpdateInfo;

        try {
            InputStream updateInfoSteam = UnikAPI.class.getClassLoader().getResourceAsStream("updateInfo.json");
            Reader reader = new InputStreamReader(updateInfoSteam, "UTF-8");
            localUpdateInfo = gson.fromJson(reader, UpdateInfoJson.class);

            InputStream remoteInputStream = new URL("https://github.com/TFSMads/transporter/releases/latest/download/updateInfo.json").openStream();
            reader = new InputStreamReader(remoteInputStream, "UTF-8");
            remoteUpdateInfo = gson.fromJson(reader, UpdateInfoJson.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return localUpdateInfo.version.equals(remoteUpdateInfo.version);
    }*/

}
