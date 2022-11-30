package chatting.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TravelServer {
    protected static final int TRAVEL_SERVER_PORT = 9997;
    protected static final String ANONYMOUS = "익명 ";
    protected static int count;
    protected static List<Thread> users;
    protected static ServerSocket serverSocket;
    protected Socket socket;

    public static void main(String[] args) {
        TravelServer travelServer = new TravelServer();
        travelServer.start();
    }

    public TravelServer() {
        count = 0;
        users = new ArrayList<>();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(TRAVEL_SERVER_PORT);
            serverSocket.getReuseAddress();
            while (true) {
                socket = serverSocket.accept();
                TravelThread thread = new TravelThread(this, socket);
                addClient(thread);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void addClient(TravelThread thread) {
        users.add(thread);
    }

    public synchronized void removeClient(Thread thread) {
        users.remove(thread);
    }

    public synchronized void broadCasting(String message) {
        for (Thread user : users) {
            TravelThread thread = (TravelThread) user;
            thread.sendMessage(message);
        }
    }
}

class TravelThread extends Thread {
    Socket socket;
    TravelServer mainServer;
    BufferedReader in;
    PrintWriter out;
    String name;
    String threadName;

    public TravelThread(TravelServer server, Socket socket) {
        this.mainServer = server;
        this.socket = socket;
        threadName = super.getName();
    }

    public void sendMessage(String message) {
        if (message != null) {
            out.println(message);
        }
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            TravelServer.count++;
            name = TravelServer.ANONYMOUS + TravelServer.count;
            mainServer.broadCasting(name + " 님이 접속하셨습니다.");
            while (true) {
                String input = in.readLine();
                mainServer.broadCasting("[" + name + "] " + input);
            }
        } catch (IOException e) {
            mainServer.removeClient(this);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
