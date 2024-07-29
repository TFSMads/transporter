package ml.volder.transporter.updater;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.gui.UpdateFailedScreen;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UpdateManager {

    public static String currentVersion = "ukendt";

    private static class UpdateInfoJson {
        public String version;
        public String forceupdate;
        public String changelog;
    }

    public static boolean isUpToDate() {
        InputStream transporterInputStream = UpdateManager.class.getClassLoader().getResourceAsStream("transporter.json");
        if(transporterInputStream != null){
            try {
                Reader r = new InputStreamReader(transporterInputStream, "UTF-8");
                JsonObject transporterJson = new JsonParser().parse(r).getAsJsonObject();
                if(transporterJson.has("autoUpdate") && !transporterJson.get("autoUpdate").getAsBoolean())
                    return true;
            } catch (Exception ignored) {}
        }
        Gson gson = new Gson();
        UpdateInfoJson localUpdateInfo;
        UpdateInfoJson remoteUpdateInfo;
        try {
            InputStream updateInfoSteam = UpdateManager.class.getClassLoader().getResourceAsStream("transporter/updateInfo.json");
            Reader reader = new InputStreamReader(updateInfoSteam, "UTF-8");
            localUpdateInfo = gson.fromJson(reader, UpdateInfoJson.class);

            InputStream remoteInputStream = new URL("https://github.com/TFSMads/transporter/releases/latest/download/updateInfo.json").openStream();
            reader = new InputStreamReader(remoteInputStream, "UTF-8");
            remoteUpdateInfo = gson.fromJson(reader, UpdateInfoJson.class);
        } catch (IOException ignored) {
            return true;
        }

        currentVersion = localUpdateInfo.version;
        return localUpdateInfo.version.equals(remoteUpdateInfo.version);
    }



    private static void extractUpdater(Path jarFileLocation) throws IOException {
        Path targetFile = getUpdaterPath();
        extractFile(jarFileLocation, "transporter/updater/TransporterUpdater.jar", targetFile);
    }

    /**
     * If the provided class has been loaded from a jar file that is on the local file system, will find the absolute path to that jar file.
     *
     * @param context The jar file that contained the class file that represents this class will be found. Specify {@code null} to let {@code LiveInjector}
     *                find its own jar.
     * @throws IllegalStateException If the specified class was loaded from a directory or in some other way (such as via HTTP, from a database, or some
     *                               other custom classloading device).
     */
    private static String findPathJar(Class<?> context) throws IllegalStateException {
        if (context == null) context = UpdateManager.class;
        String rawName = context.getName();
        String classFileName;
        /* rawName is something like package.name.ContainingClass$ClassName. We need to turn this into ContainingClass$ClassName.class. */ {
            int idx = rawName.lastIndexOf('.');
            classFileName = (idx == -1 ? rawName : rawName.substring(idx+1)) + ".class";
        }

        String uri = context.getResource(classFileName).toString();
        if (uri.startsWith("file:")) throw new IllegalStateException("This class has been loaded from a directory and not from a jar file.");
        if (!uri.startsWith("jar:file:")) {
            int idx = uri.indexOf(':');
            String protocol = idx == -1 ? "(unknown)" : uri.substring(0, idx);
            throw new IllegalStateException("This class has been loaded remotely via the " + protocol +
                    " protocol. Only loading from a jar on the local file system is supported.");
        }

        int idx = uri.indexOf('!');
        //As far as I know, the if statement below can't ever trigger, so it's more of a sanity check thing.
        if (idx == -1) throw new IllegalStateException("You appear to have loaded this class from a local jar file, but I can't make sense of the URL!");

        try {
            String fileName = URLDecoder.decode(uri.substring("jar:file:".length(), idx), Charset.defaultCharset().name());
            return new File(fileName).getAbsolutePath();
        } catch (UnsupportedEncodingException e) {
            throw new InternalError("default charset doesn't exist. Your VM is borked.");
        }
    }

    private static void extractFile(Path zipFile, String fileName, Path outputFile) throws IOException {
        OutputStream out = Files.newOutputStream(outputFile);
        FileInputStream fileInputStream = new FileInputStream(zipFile.toFile());
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        ZipInputStream zin = new ZipInputStream(bufferedInputStream);
        ZipEntry ze = null;
        while ((ze = zin.getNextEntry()) != null) {
            if (ze.getName().equals(fileName)) {
                byte[] buffer = new byte[9000];
                int len;
                while ((len = zin.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.close();
                break;
            }
        }
        zin.close();
    }

    public static void update() {
        String jarLocation = getJarLocation();
        try {
            extractUpdater(Paths.get(jarLocation));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Path updaterPath = getUpdaterPath();
        String downloadURL = getDownloadURL();

        try {
            String javaHome = System.getProperty("java.home");
            if(javaHome == null){
                PlayerAPI.getAPI().openGuiScreen(new UpdateFailedScreen());
                return;
            }
            Path path = Paths.get(UnikAPI.getCommonDataFolder() + "/transporter/updater/");
            path.toFile().mkdirs();
            String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
            List<String> arguments = new ArrayList();
            arguments.add(javaBin);
            arguments.add("-jar");
            arguments.add(updaterPath.toFile().getAbsolutePath());
            arguments.add(downloadURL);
            arguments.add(jarLocation);
            ProcessBuilder pb = new ProcessBuilder(arguments);
            pb.directory(new File(UnikAPI.getCommonDataFolder() + "/transporter/updater/"));
            Process p = pb.start();
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String jarLocation;
    public static String getJarLocation() {
        try {
          if(jarLocation == null)
            jarLocation = findPathJar(UpdateManager.class);
          return jarLocation;
        } catch (Exception ignored) {
          return "Kunne ikke bestemme jar filens placering";
        }
    }

    private static String getDownloadURL() {
        if(UnikAPI.getClientBrand().equals("labymod4"))
            return "https://github.com/TFSMads/transporter/releases/latest/download/transporter-laby4.jar";
        else if (UnikAPI.getClientBrand().equals("labymod3")) {
            if(UnikAPI.matchMinecraftVersion(new String[]{"1.8.*"})) {
                return "https://github.com/TFSMads/transporter/releases/latest/download/transporter-laby3_v1_8_9.jar";
            } else if(UnikAPI.matchMinecraftVersion(new String[]{"1.12.*"})) {
                return "https://github.com/TFSMads/transporter/releases/latest/download/transporter-laby3_v1_12_2.jar";
            } else if(UnikAPI.matchMinecraftVersion(new String[]{"1.16.*"})) {
                return "https://github.com/TFSMads/transporter/releases/latest/download/transporter-laby3_v1_16_5.jar";
            }
        }
        return "https://github.com/TFSMads/transporter/releases/latest/download/transporter-laby4.jar";
    }




    private static Path getUpdaterPath() {
        return Paths.get(UnikAPI.getCommonDataFolder().getPath(), "TransporterUpdater.jar");
    }



    /*
     * Csv update system
     *
     * Auto update csv files from github.
     */

    public static void updateCsvFiles(DataManager<Data> dataManager) {
        updateCsvFile(
                new File(UnikAPI.getCommonDataFolder(), "transporter-items.csv"),
                "https://raw.githubusercontent.com/TFSMads/transporter/master/core/src/main/resources/transporter/transporter-items.csv",
                !dataManager.getSettings().getData().has("updateItemsFromGithub")
                        || dataManager.getSettings().getData().get("updateItemsFromGithub").getAsBoolean()
        );
        updateCsvFile(
                new File(UnikAPI.getCommonDataFolder(), "transporter-messages.csv"),
                "https://raw.githubusercontent.com/TFSMads/transporter/master/core/src/main/resources/transporter/transporter-messages.csv",
                !dataManager.getSettings().getData().has("updateMessagesFromGithub")
                        || dataManager.getSettings().getData().get("updateMessagesFromGithub").getAsBoolean()
        );
        updateCsvFile(
                new File(UnikAPI.getCommonDataFolder(), "transporter-balance-messages.csv"),
                "https://raw.githubusercontent.com/TFSMads/transporter/master/core/src/main/resources/transporter/transporter-balance-messages.csv",
                !dataManager.getSettings().getData().has("updateBalanceMessagesFromGithub")
                        || dataManager.getSettings().getData().get("updateBalanceMessagesFromGithub").getAsBoolean()
        );
    }

    private static void updateCsvFile(File csvFile, String url, boolean update) {
        try {
            updateCsvFile(csvFile, new URL(url), update);
        } catch (Exception ignored) {
            UnikAPI.LOGGER.warning("Failed to update " + csvFile.getName() + " from url: " + url);
        }
    }

    private static void updateCsvFile(File csvFile, URL downloadURL, boolean update) {
        if (csvFile.exists()) {
            try {
                if(!update)
                    return;
                String localHash = getHash(csvFile);
                String remoteHash = getRemoteHash(downloadURL);
                if (localHash.equals(remoteHash)){
                    UnikAPI.LOGGER.finest("Csv file is up to date: " + csvFile.getName());
                    return;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            UnikAPI.LOGGER.info("Updating csv file: " + csvFile.getName());
            InputStream in = downloadURL.openStream();
            csvFile.delete();
            Files.copy(in, csvFile.toPath());
        } catch (IOException e) {
            UnikAPI.LOGGER.warning("Failed updating csv file: " + csvFile.getName());
            //If file is missing use the one packaged with the jar
            if (!csvFile.exists()) {
                InputStream in = TransporterAddon.class.getClassLoader().getResourceAsStream(csvFile.getName());
                try {
                    assert in != null;
                    Files.copy(in, csvFile.toPath());
                } catch (IOException ioException) {
                    UnikAPI.LOGGER.severe("Failed to load csv file from jar: " + csvFile.getName());
                    throw new RuntimeException(ioException);
                }
            }
            throw new RuntimeException(e);
        }
    }

    private static String getRemoteHash(URL downloadURL) {
        try {
            InputStream in = downloadURL.openStream();
            return getHash(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getHash(File file) throws IOException {
        return getHash(Files.newInputStream(file.toPath()));
    }

    private static String getHash(InputStream inputStream) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
            byte[] hash = md.digest();
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
