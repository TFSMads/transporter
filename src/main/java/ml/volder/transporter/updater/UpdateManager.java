package ml.volder.transporter.updater;

import com.google.gson.Gson;
import jdk.tools.jlink.internal.Platform;
import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.helper.OsCheck;
import ml.volder.transporter.gui.UpdateFailedScreen;
import ml.volder.transporter.utils.ArchDetect;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.api.player.PlayerAPI;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
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
        //Updated through FlintUpdater.
        return true;
    }



    private static void extractUpdater(Path jarFileLocation) throws IOException {
        Path targetFile = getUpdaterPath();
        extractFile(jarFileLocation, "updater/TransporterUpdater.jar", targetFile);
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
            Path path = Paths.get(UnikAPI.getCommonDataFolder() + "/updater/");
            path.toFile().mkdirs();
            String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
            List<String> arguments = new ArrayList();
            arguments.add(javaBin);
            arguments.add("-jar");
            arguments.add(updaterPath.toFile().getAbsolutePath());
            arguments.add(downloadURL);
            arguments.add(jarLocation);
            ProcessBuilder pb = new ProcessBuilder(arguments);
            pb.directory(new File(UnikAPI.getCommonDataFolder() + "/updater/"));
            Process p = pb.start();
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String jarLocation;
    public static String getJarLocation() {
        if(jarLocation == null)
            jarLocation = findPathJar(UpdateManager.class);
        return jarLocation;
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
}
