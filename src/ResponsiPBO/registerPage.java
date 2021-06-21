package ResponsiPBO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class registerPage {
    private JPanel regPanel;
    private JTextField tfNama;
    private JTextField tfEmail;
    private JPasswordField tfPassword;
    private JComboBox cbRole;
    private JButton resetButton;
    private JButton simpanButton;
    private JButton batalButton;

    connectorDB connectorDB = new connectorDB();

    public registerPage() {
        JFrame frame = new JFrame("Responsi Praktikum PBO");
        frame.setContentPane(regPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //membersihkan isi form
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        //menyimpan data ke database sesuai role
        simpanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (getEmail().equals("")||getNama().equals("")||getPassword().equals("")){
                        JOptionPane.showMessageDialog(null,"Data Tidak Boleh Kosong!", "Peringatan",JOptionPane.INFORMATION_MESSAGE);
                    } else if(!Pattern.matches("^[a-zA-Z0-9]+[@]{1}+[a-zA-Z0-9]+[.]{1}+[a-zA-Z0-9]+$",getEmail())) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid email", "Error", JOptionPane.ERROR_MESSAGE);
                    }else {
                        connectorDB.statement = connectorDB.koneksi.createStatement();
                        String query;

                        if(getRole().equals("Admin")){
                            query = ("INSERT INTO admin VALUES (NULL,'"+getNama()+"','"+getEmail()+"','"+getPassword()+"')");
                        }else {
                            query = ("INSERT INTO user VALUES (NULL,'"+getNama()+"','"+getEmail()+"','"+getPassword()+"')");
                        }

                        connectorDB.statement.executeUpdate(query);

                        JOptionPane.showMessageDialog(null,"Berhasil Disimpan!");

                        frame.setVisible(false);
                        new login();
                    }

                } catch (SQLException er){
                    JOptionPane.showMessageDialog(null, "Data Gagal Disimpan!" + er, "Hasil", JOptionPane.ERROR_MESSAGE );
                }
            }
        });

        //button kembali ke menu login
        batalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new login();
            }
        });
    }

    private String getRole(){
        return cbRole.getSelectedItem().toString();
    }

    private String getNama(){
        return tfNama.getText();
    }

    private String getEmail(){
        return tfEmail.getText();
    }

    private String getPassword(){
        return String.valueOf(tfPassword.getPassword());
    }
}
