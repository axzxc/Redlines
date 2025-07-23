import org.cef.CefApp;
import org.cef.CefApp.CefAppState;

import me.friwi.jcefmaven.CefAppBuilder;

import java.io.*;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipFile;

public class JCEFInvoker {
    private static final String JCEF_ZIP_NAME = "/jcef-native.zip"; // must be in resources
    private static final String JCEF_DIR_NAME = "jcef-extracted"+File.separator+"jcef-native";

    private CefApp cefApp;

    public JCEFInvoker() {
        try {
            Path baseTempDir = Paths.get(System.getProperty("java.io.tmpdir"));
            Path jcefTempDir = baseTempDir.resolve(JCEF_DIR_NAME);

            if (!Files.exists(jcefTempDir)) {
                System.out.println("Extracting JCEF to: " + jcefTempDir);
                InputStream zipStream = getClass().getResourceAsStream(JCEF_ZIP_NAME);
                if (zipStream == null)
                    throw new FileNotFoundException("JCEF zip not found in resources.");
                extractZip(zipStream, jcefTempDir);
                System.out.println("JCEF extraction complete.");
            } else {
                System.out.println("Found existing JCEF extraction at: " + jcefTempDir);
            }

            if (CefApp.getState() != CefAppState.NONE)
                throw new IllegalStateException("CefApp already initialized elsewhere!");

            CefAppBuilder builder = new CefAppBuilder();
            builder.setInstallDir(jcefTempDir.toFile());
            builder.addJcefArgs("--disable-gpu"); // optional
            builder.getCefSettings().windowless_rendering_enabled = true;

            cefApp = builder.build();
            System.out.println("JCEF initialized successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("JCEF INVOKER: Failed to initialize.");
        }
    }

    public CefApp getCefApp() {
        return cefApp;
    }

    private void extractZip(InputStream input, Path targetDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(input)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path filePath = targetDir.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    Files.createDirectories(filePath.getParent());
                    try (OutputStream out = Files.newOutputStream(filePath)) {
                        zis.transferTo(out);
                    }
                }
                zis.closeEntry();
            }
        }
    }
}
