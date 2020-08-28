package task2.client.GUI;

import task2.client.AuthorizationChecker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginChatFrame extends JDialog {
    private boolean isAuthorized = false;
    private JTextField loginTextField;
    private JTextField passwordTextField;
    private JButton submitButton;
    private JLabel authLabel;


    private AuthorizationChecker authorizationChecker;

    public LoginChatFrame(AuthorizationChecker authorizationChecker, String login, String password){
        this.authorizationChecker = authorizationChecker;

        setModal(true);
        initComponents();

        loginTextField.setText(login);
        passwordTextField.setText(password);

        setVisible(true);
        submitButton.requestFocus();
    }

    private void initComponents(){
        setBounds(800, 500, 350, 150);

        setDefaultCloseOperation(HIDE_ON_CLOSE);

        setResizable(false);

        setTitle("Введите логин и пароль для подключения");

        JPanel mainPanel = new JPanel(new GridLayout(4,1));
        authLabel = new JLabel("Введите логин и пароль");
        loginTextField = new JTextField();
        passwordTextField = new JTextField();
        submitButton = new JButton("Войти");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonOkClicked();
            }
        });

        mainPanel.add(authLabel);
        mainPanel.add(loginTextField);
        mainPanel.add(passwordTextField);
        mainPanel.add(submitButton);
        add(mainPanel);
    }

    private void buttonOkClicked(){
        isAuthorized = authorizationChecker.checkAuthorization(loginTextField.getText(), passwordTextField.getText());
        if (isAuthorized) {
            setVisible(false);
        } else {
            authLabel.setText("Введен некорректный логин или пароль!");
        }

    }

    public boolean isAuthorized() {
        return isAuthorized;
    }
}
