package net.iamsilver.fireflies.ipc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Runnable {

    private ServerSocket server;
    private Socket socket;
    private BufferedReader reader;
    private SocketListener listener;

    private boolean shouldStop = false;

    public SocketServer(SocketListener listener) {
        try {
            this.listener = listener;
            server = new ServerSocket(12_001);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Starting socket listener...");
        try {
            while (!shouldStop) {
                if (socket == null) {
                    // if socket is not initialized, accept (wait for) a new connection
                    System.out.println("Waiting for socket...");

                    socket = server.accept();
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    System.out.println("Socket connected.");
                    long time = System.currentTimeMillis();
                    while (System.currentTimeMillis() - time < 1_000 * 5) {
                        if (reader.ready()) {
                            time = System.currentTimeMillis();
                            listener.received(Markers.fromString(reader.readLine()));

                            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
                            writer.write("ok");
                            writer.flush();
                        }
                    }
                    System.out.println("Socket closed due to timeout...");
                }

                // dispose current socket (will create new one in next iteration)
                if (!socket.isClosed()) socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was an error, re-running socket thread...");
            this.run(); // re-run thread upon error
        }
    }

    public void dispose() {
        System.out.println("Disposing socket server...");
        this.shouldStop = true;
        try {
            socket.close();
            server.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }



}
