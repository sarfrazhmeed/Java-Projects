import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {


    public static boolean isValidFile(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isRegularFile(path) && Files.isReadable(path);
    }


    public static String ensureDirectory(String directoryPath) throws IOException {
        Path dir = Paths.get(directoryPath);

        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        if (!Files.isWritable(dir)) {
            throw new IOException("Directory is not writable: " + directoryPath);
        }

        return dir.toAbsolutePath().toString();
    }


    public static byte[] readFileBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }


    public static void writeFileBytes(File file, byte[] data) throws IOException {
        Files.write(file.toPath(), data);
    }
}
