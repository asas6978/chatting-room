package chatting.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SportServer {
    protected static final int SPORT_SERVER_PORT = 9998;
    protected static final String ANONYMOUS = "익명 ";
    protected static int count;
    protected static List<Thread> users;
    protected static ServerSocket serverSocket;
    protected Socket socket;

    public static void main(String[] args) {
        SportServer sportServer = new SportServer();
        sportServer.start();
    }

    public SportServer() {
        count = 0;
        users = new ArrayList<>();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(SPORT_SERVER_PORT);
            serverSocket.getReuseAddress();
            while (true) {
                socket = serverSocket.accept();
                SportThread thread = new SportThread(this, socket);
                addClient(thread);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void addClient(SportThread thread) {
        users.add(thread);
    }

    public synchronized void removeClient(Thread thread) {
        users.remove(thread);
    }

    public synchronized void broadCasting(String message) {
        for (Thread user : users) {
            SportThread thread = (SportThread) user;
            thread.sendMessage(message);
        }
    }
}

class SportThread extends Thread {
    Socket socket;
    SportServer sportServer;
    BufferedReader in;
    PrintWriter out;
    String name;
    String threadName;

    public SportThread(SportServer server, Socket socket) {
        this.sportServer = server;
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

            SportServer.count++;
            name = SportServer.ANONYMOUS + SportServer.count;
            sportServer.broadCasting(name + " 님이 접속하셨습니다.");
            while (true) {
                String input = in.readLine();
                sportServer.broadCasting("[" + name + "] " + input);
            }
        } catch (IOException e) {
            sportServer.removeClient(this);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
