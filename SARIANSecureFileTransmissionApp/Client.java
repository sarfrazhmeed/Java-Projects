import java.io.*;
import java.net.Socket;

public class Client {

    public static void sendFile(String filePath, String ipAddress, int port, String key) throws Exception {
        long startTime = System.currentTimeMillis();

        if (!FileManager.isValidFile(filePath)) {
            throw new IllegalArgumentException("Invalid file: " + filePath);
         }

        File file = new File(filePath);
        byte[] raw = FileManager.readFileBytes(file);
        byte[] encrypted = Encryptor.encrypt(raw, key);

        try (Socket socket = new Socket(ipAddress, port);
             BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
             DataOutputStream dos = new DataOutputStream(bos)) {

            System.out.println("Sending the file: " + file.getName());

            dos.writeUTF(file.getName());
            dos.writeInt(encrypted.length);
            dos.write(encrypted);
            dos.flush();

            System.out.println(" File sent successfully.");

        } catch (IOException e) {
            System.err.println(" Error sending file: " + e.getMessage());
            throw e;
        }

        long endTime = System.currentTimeMillis();
        System.out.println(" Time taken to send: " + (endTime - startTime) + " ms");
        Logger.log("Sent file '" + file.getName() + "' to " + ipAddress + ":" + port);
    }
}
