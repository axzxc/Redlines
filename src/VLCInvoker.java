import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.*;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.tukaani.xz.XZInputStream;

public class VLCInvoker {
    private static final String VLC_TARBALL_NAME = "/vlc.tar.xz"; // Must be inside resources
    private static final String VLC_DIR_NAME = "vlc-extracted";
    private static final String VLC_VERSION_DIR = "vlc-3.0.18"; // The directory created by the tarball

    public VLCInvoker() {
        try {
            // Step 1: Define a persistent path in the system's temp directory
            Path baseTempDir = Paths.get(System.getProperty("java.io.tmpdir"));
            Path vlcTempDir = baseTempDir.resolve(VLC_DIR_NAME);
            Path libDir = vlcTempDir.resolve(VLC_VERSION_DIR).resolve("lib");

            // Step 2: Check if VLC is already extracted. If not, extract it.
            if (!Files.exists(libDir)) {
                System.out.println("VLC libraries not found. Extracting to: " + vlcTempDir);

                // Ensure the parent directory exists
                Files.createDirectories(vlcTempDir);

                InputStream tarballStream = getClass().getResourceAsStream(VLC_TARBALL_NAME);
                if (tarballStream == null) {
                    throw new FileNotFoundException("VLC tarball not found in resources.");
                }

                extractTarXz(tarballStream, vlcTempDir);
                System.out.println("Extraction complete.");
            } else {
                System.out.println("Found existing VLC libraries in: " + vlcTempDir);
            }

            if (!Files.exists(libDir)) {
                throw new RuntimeException("Missing VLC lib folder after extraction attempt.");
            }

            // Step 3: Detect OS and set proper environment variables
            String os = System.getProperty("os.name").toLowerCase();
            System.setProperty("jna.library.path", libDir.toAbsolutePath().toString());

            if (os.contains("win")) {
                // Windows
                appendToEnv("PATH", libDir.toString());
            } else if (os.contains("mac")) {
                // macOS
                setEnv("DYLD_LIBRARY_PATH", libDir.toString());
            } else {
                // Assume Linux
                setEnv("LD_LIBRARY_PATH", libDir.toString());
            }
            System.out.println("VLC INVOKER: Environment configured successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("VLC INVOKER: Failed to initialize VLC.");
        }
    }

    private void extractTarXz(InputStream input, Path targetDir) throws IOException {
        try (
            InputStream xzIn = new XZInputStream(input);
            TarArchiveInputStream tarIn = new TarArchiveInputStream(xzIn)
        ) {
            TarArchiveEntry entry;
            while ((entry = tarIn.getNextTarEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }

                Path filePath = targetDir.resolve(entry.getName());
                Files.createDirectories(filePath.getParent());

                try (OutputStream out = Files.newOutputStream(filePath)) {
                    tarIn.transferTo(out);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void setEnv(String key, String value) {
        try {
            // This works on most JVMs
            Map<String, String> env = System.getenv();
            Class<?> processEnvClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnv = processEnvClass.getDeclaredField("theEnvironment");
            theEnv.setAccessible(true);
            ((Map<String, String>) theEnv.get(null)).put(key, value);

            Field theCaseInsensitiveEnv = processEnvClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnv.setAccessible(true);
            ((Map<String, String>) theCaseInsensitiveEnv.get(null)).put(key, value);
        } catch (Exception ignored) {
            // Fallback for Linux/Unix
            try {
                Map<String, String> env = System.getenv();
                for (Class<?> cl : Collections.class.getDeclaredClasses()) {
                    if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                        Field field = cl.getDeclaredField("m");
                        field.setAccessible(true);
                        Object obj = field.get(env);
                        Map<String, String> map = (Map<String, String>) obj;
                        map.put(key, value);
                    }
                }
            } catch (Exception ignoreAgain) {
            }
        }
    }

    private void appendToEnv(String key, String appendValue) {
        String current = System.getenv(key);
        String newValue = appendValue + File.pathSeparator + (current != null ? current : "");
        setEnv(key, newValue);
    }
}