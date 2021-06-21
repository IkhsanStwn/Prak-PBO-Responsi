package ResponsiPBO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class login{
    private JTextField tfemail;
    private JButton btnlogin;
    private JButton btnregister;
    private JPanel loginPanel;
    private JPasswordField tfPassword;

    connectorDB connector = new connectorDB();

    public login() {
        JFrame frame = new JFrame("Responsi Praktikum PBO");
        frame.setContentPane(loginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //tombol untuk login ke admin atau user
        btnlogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (getEmail().equals("") || getPassword().equals("")){
                        JOptionPane.showMessageDialog(null,"Username atau Password Tidak Boleh Kosong!", "Peringatan",JOptionPane.INFORMATION_MESSAGE);
                        tfemail.requestFocus();
                    }else if(!Pattern.matches("^[a-zA-Z0-9]+[@]{1}+[a-zA-Z0-9]+[.]{1}+[a-zA-Z0-9]+$",getEmail())) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid email", "Error", JOptionPane.ERROR_MESSAGE);
                        resetData();
                    }else{
                        connector.statement = connector.koneksi.createStatement();

                        String query = ("SELECT * FROM admin WHERE email = '"+getEmail()+"' AND password = '"+getPassword()+"' ");
                        ResultSet resultSet = connector.statement.executeQuery(query);
                        if (resultSet.next()){
                            JOptionPane.showMessageDialog(null,"Berhasil Login Sebagai Admin", "Peringatan", JOptionPane.INFORMATION_MESSAGE);
                            frame.setVisible(false);
                            new adminPage(getEmail(),getPassword());
                        }else {
                            query = ("SELECT * FROM user WHERE email = '"+getEmail()+"' AND password = '"+getPassword()+"'");
                            ResultSet resultSet1 = connector.statement.executeQuery(query);
                            if (resultSet1.next()){
                                JOptionPane.showMessageDialog(null,"Berhasil Login Sebagai User", "Peringatan", JOptionPane.INFORMATION_MESSAGE);
                                frame.setVisible(false);
                                new userPage(getEmail(),getPassword());
                            }else {
                                JOptionPane.showMessageDialog(null,"Username atau Password Salah!", "Peringatan",JOptionPane.ERROR_MESSAGE);
                            }
                        }

                    }

                }catch (SQLException er){
                    JOptionPane.showMessageDialog(null,"Gagal Terhubung Database!");

                }
            }
        });

        //button menuju halaman register
        btnregister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new registerPage();
            }
        });
    }

    public void resetData(){
        tfemail.setText("");
        tfPassword.setText("");
    }

    private String getEmail(){
        return tfemail.getText();
    }

    private String getPassword(){
        return String.valueOf(tfPassword.getPassword());
    }
}
