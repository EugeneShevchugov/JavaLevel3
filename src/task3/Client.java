package task3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class Client extends JFrame {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private JTextArea chatTextArea;
    private String userName, userLogin;
    private final String ABOUTNIKNAME = "You changed your nickname.";
    private ClientHistory history;


    public Client() {
        try {
            socket = new Socket("localhost", Server.PORT);
            prepareGUI();
            System.out.println("Connected to server: " + socket);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void getMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isLogIn = false;
                while (true) {
                    try {
                        String message = in.readUTF();
                        if (!message.isBlank()) {
                            message = message.trim();
                            if (message.startsWith("You") && message.endsWith("logged-in.")) {
                                userName = message.substring(message.indexOf('(') + 1, message.indexOf(')'));
                                userLogin = message.substring(message.indexOf('[') + 1, message.indexOf(']'));
                                setTitle(userName);
                                chatTextArea.append("You has logged in.\n");
                                history = new ClientHistory(userLogin);
                                chatTextArea.append(history.getArchive());
                            } else if (message.startsWith(ABOUTNIKNAME)) {
                                userName = message.substring(message.indexOf('(') + 1, message.indexOf(')'));
                                setTitle(userName);
                            } else {
                                if (message.startsWith(userName + ':'))
                                    message = "You" + message.substring(message.indexOf(':'));
                                chatTextArea.append(message + "\n");
                                if (history != null) history.addIntoArchive(message + "\n");
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Server is unavailable.");
                    }
                }
            }
        }).start();
    }

    public void sendMessage(String message) {
        if (!message.trim().isBlank()) {
            if (message.trim().equalsIgnoreCase("/end")) {
                System.out.println("Disconnecting from server...");
                try {
                    out.writeUTF(message.trim());
                    closeConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                out.writeUTF(message.trim());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeConnection() {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void prepareGUI() {
        HashMap<String, StringBuilder> chat = new HashMap<>();

        setTitle(String.format("Client, %s", socket.getLocalPort()));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(0, 0, 600, 500);
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        Font font = new Font("Arial", Font.TRUETYPE_FONT, 17);
        chatTextArea.setFont(font);
        chatTextArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(chatTextArea);
        chatPanel.add(scrollPane);
        add(chatPanel);

        JMenuBar mainMenu = new JMenuBar();
        JMenu mTheme = new JMenu("Theme");
        JMenu mHelp = new JMenu("Help");

        JMenuItem mThemeLight = new JMenuItem("Light");
        JMenuItem mThemeDark = new JMenuItem("Dark");

        JMenuItem mAuth = new JMenuItem("/auth Login Password");
        JMenuItem mW = new JMenuItem("/w RecipientNickname MessageText");
        JMenuItem mNick = new JMenuItem("/nick newNickname");
        JMenuItem mEnd = new JMenuItem("/end");

        setJMenuBar(mainMenu);
        mainMenu.add(mTheme);
        mainMenu.add(mHelp);

        mTheme.add(mThemeLight);
        mTheme.add(mThemeDark);

        mHelp.add(mAuth);
        mHelp.add(mW);
        mHelp.add(mNick);
        mHelp.add(mEnd);

        JPanel controlPanel = new JPanel(new BorderLayout());

        JTextField inputField = new JTextField();
        controlPanel.add(inputField);

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(inputField.getText().trim());
                inputField.setText("");
            }
        });

        mThemeDark.addActionListener(new MainMenuThemeListener(chatTextArea, mThemeDark, inputField));
        mThemeLight.addActionListener(new MainMenuThemeListener(chatTextArea, mThemeLight, inputField));

        JButton submitBtn = new JButton("Submit");
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(inputField.getText().trim());
                inputField.setText("");

            }
        });
        controlPanel.add(submitBtn, BorderLayout.EAST);

        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public class MainMenuThemeListener implements ActionListener {
        private JTextArea charTextArea;
        private JMenuItem mTheme;
        private JTextField inputField;

        public MainMenuThemeListener(JTextArea charTextArea, JMenuItem mTheme, JTextField inputField) {
            this.charTextArea = charTextArea;
            this.mTheme = mTheme;
            this.inputField = inputField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (mTheme.getText().equals("Dark")) {
                charTextArea.setBackground(Color.darkGray);
                charTextArea.setForeground(Color.lightGray);
                inputField.setBackground(Color.darkGray);
                inputField.setForeground(Color.lightGray);
            } else {
                charTextArea.setBackground(Color.white);
                charTextArea.setForeground(Color.black);
                inputField.setBackground(Color.white);
                inputField.setForeground(Color.black);
            }
        }

    }
}
