import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    public void receiveFile(int port, String saveDirectory, String key) throws Exception {
        if (saveDirectory == null || saveDirectory.trim().isEmpty()) {
            saveDirectory = "received";
        }

        String dir = FileManager.ensureDirectory(saveDirectory);
        long startTime = System.currentTimeMillis();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Waiting for incoming file on port " + port + "...");
            try (Socket client = serverSocket.accept();
                 BufferedInputStream bis = new BufferedInputStream(client.getInputStream());
                 DataInputStream dis = new DataInputStream(bis)) {

                System.out.println(" Connection established with: " + client.getInetAddress());

                String fileName = dis.readUTF();
                int length = dis.readInt();
                System.out.println(" Receiving file: " + fileName + " (" + length + " bytes)");

                byte[] encrypted = new byte[length];
                dis.readFully(encrypted);

                byte[] decrypted = Encryptor.decrypt(encrypted, key);

                File outFile = new File(dir, fileName);
                FileManager.writeFileBytes(outFile, decrypted);

                System.out.println(" File saved to: " + outFile.getAbsolutePath());

                Logger.log(" Received file '" + fileName + "' and saved to: " + outFile.getAbsolutePath());

                long endTime = System.currentTimeMillis();
                System.out.println(" Time taken to receive: " + (endTime - startTime) + " ms");

            }
        } catch (Exception e) {
            System.err.println(" Error receiving file: " + e.getMessage());
            Logger.log(" Error receiving file: " + e.getMessage());
            throw e;
        }
    }
}
