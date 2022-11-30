package chatting.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    protected static final int GAME_SERVER_PORT = 9999;
    protected static final String ANONYMOUS = "익명 ";
    protected static int count;  // 채팅방에 참여한 유저 수
    protected static List<Thread> users;  // 클라이언트 스레드 리스트
    protected static ServerSocket serverSocket;
    protected Socket socket;

    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        gameServer.start();
    }

    public GameServer() {  // 서버가 시작할 때 유저 수 및 스레드 리스트 초기화
        count = 0;
        users = new ArrayList<>();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(GAME_SERVER_PORT);
            serverSocket.getReuseAddress();
            while (true) {
                socket = serverSocket.accept();
                GameThread thread = new GameThread(this, socket);  // 유저 스레드 생성
                addClient(thread);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void addClient(GameThread thread) {
        users.add(thread);
    }

    public synchronized void removeClient(Thread thread) {
        users.remove(thread);
    }

    public synchronized void broadCasting(String message) {
        for (Thread user : users) {
            GameThread thread = (GameThread) user;
            thread.sendMessage(message);
        }
    }
}

class GameThread extends Thread {
    Socket socket;
    GameServer mainServer;
    BufferedReader in;
    PrintWriter out;
    String name;
    String threadName;

    public GameThread(GameServer server, Socket socket) {
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

            GameServer.count++;
            name = GameServer.ANONYMOUS + GameServer.count;
            mainServer.broadCasting(name + " 님이 접속하셨습니다.");
            while (true) {  // 유저 채팅 입력 수신 대기
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
