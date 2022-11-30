package chatting.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class View {
    protected static final String SERVER_IP_ADDRESS = "127.0.0.1";
    protected static final int GAME_SERVER_PORT = 9999;
    protected static final int SPORT_SERVER_PORT = 9998;
    protected static final int TRAVEL_SERVER_PORT = 9997;

    public static void main(String[] args) {
        try {
            View view = new View();
            view.execute();  // 메인 화면 입장
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        JFrame frame = new JFrame("Chatting");
        frame.setSize(400, 400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.setContentPane(panel);
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        JLabel label = new JLabel("Finding Hobby");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(label);

        JButton gameButton = new JButton("Game");
        JButton sportButton = new JButton("Sport");
        JButton travelButton = new JButton("Travel");

        ActionListener gamePage = event -> {
            frame.setVisible(false);
            Game game = new Game();
            game.execute();
        };
        panel.add(gameButton);
        gameButton.addActionListener(gamePage);

        ActionListener sportPage = event -> {
            frame.setVisible(false);
            Sport sport = new Sport();
            sport.execute();
        };
        panel.add(sportButton);
        sportButton.addActionListener(sportPage);

        ActionListener travelPage = event -> {
            frame.setVisible(false);
            Travel travel = new Travel();
            travel.execute();
        };
        panel.add(travelButton);
        travelButton.addActionListener(travelPage);
    }
}

class Game extends JFrame implements ActionListener, Runnable {
    Container container = getContentPane();
    JTextArea textArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(textArea);
    JTextField textField = new JTextField();

    Socket socket;
    PrintWriter out;
    BufferedReader in;
    String chat;

    public void execute() {
        setTitle("Game");
        setSize(400, 400);
        initView();
        start();
        setVisible(true);
        initNetwork();
    }

    private void initNetwork() {
        try {
            socket = new Socket(View.SERVER_IP_ADDRESS, View.GAME_SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(this);
        thread.start();
    }

    private void initView() {
        container.setLayout(new BorderLayout());
        container.add("Center", scrollPane);
        container.add("South", textField);
    }

    private void start() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        textField.addActionListener(this);
    }

    @Override
    public void run() {
        while (true) {
            try {
                chat = in.readLine();
                textArea.append(chat + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        chat = textField.getText();
        out.println(chat);
        textField.setText("");
    }
}

class Sport extends JFrame implements ActionListener, Runnable {
    Container container = getContentPane();
    JTextArea textArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(textArea);
    JTextField textField = new JTextField();

    Socket socket;
    PrintWriter out;
    BufferedReader in;
    String chat;

    public void execute() {
        setTitle("Sport");
        setSize(400, 400);
        initView();
        start();
        setVisible(true);
        initNetwork();
    }

    private void initNetwork() {
        try {
            socket = new Socket(View.SERVER_IP_ADDRESS, View.SPORT_SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(this);
        thread.start();
    }

    private void initView() {
        container.setLayout(new BorderLayout());
        container.add("Center", scrollPane);
        container.add("South", textField);
    }

    private void start() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        textField.addActionListener(this);
    }

    @Override
    public void run() {
        while (true) {
            try {
                chat = in.readLine();
                textArea.append(chat + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        chat = textField.getText();
        out.println(chat);
        textField.setText("");
    }
}

class Travel extends JFrame implements ActionListener, Runnable {
    Container container = getContentPane();
    JTextArea textArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(textArea);
    JTextField textField = new JTextField();

    Socket socket;
    PrintWriter out;
    BufferedReader in;
    String chat;

    public void execute() {
        setTitle("Travel");
        setSize(400, 400);
        initView();
        start();
        setVisible(true);
        initNetwork();
    }

    private void initNetwork() {
        try {
            socket = new Socket(View.SERVER_IP_ADDRESS, View.TRAVEL_SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(this);
        thread.start();
    }

    private void initView() {
        container.setLayout(new BorderLayout());
        container.add("Center", scrollPane);
        container.add("South", textField);
    }

    private void start() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        textField.addActionListener(this);
    }

    @Override
    public void run() {
        while (true) {
            try {
                chat = in.readLine();
                textArea.append(chat + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        chat = textField.getText();
        out.println(chat);
        textField.setText("");
    }
}
