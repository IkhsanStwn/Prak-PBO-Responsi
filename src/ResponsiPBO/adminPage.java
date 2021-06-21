package ResponsiPBO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class adminPage extends Data{
    private JPanel adminPanel;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton resetButton;
    private JTextField tfNamaUser;
    private JTextField tfEmailUser;
    private JPasswordField tfPassUser;
    private JTable tbUser;
    private JTextField tfIdUser;
    private JTextField tfNamaAdm;
    private JButton btnLogout;

    connectorDB connectorDB = new connectorDB();

    DefaultTableModel model;

    public adminPage(String emailAdmin, String passAdmin) {

        JFrame frame = new JFrame("Responsi Praktikum PBO");
        frame.setContentPane(adminPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //cek detail data admin
        email = emailAdmin;
        password = passAdmin;
        detailData();
        tfNamaAdm.setText(nama);


        //setting tabel
        String [] judul = {"ID","Nama Lengkap","Email","Password"};
        model = new DefaultTableModel(judul,0);
        tbUser.setModel(model);
        tampilTabel();

        //func button

        //menyimpan data ke database data user oleh admin
        tambahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!getId_user().equals("")) {
                        JOptionPane.showMessageDialog(null, "Reset Data Dahulu!", "Peringatan", JOptionPane.ERROR_MESSAGE);
                    }else if (getEmail().equals("")||getNama().equals("")||getPassword().equals("")){
                        JOptionPane.showMessageDialog(null,"Data Tidak Boleh Kosong!", "Peringatan",JOptionPane.INFORMATION_MESSAGE);
                    }else if(!Pattern.matches("^[a-zA-Z0-9]+[@]{1}+[a-zA-Z0-9]+[.]{1}+[a-zA-Z0-9]+$",getEmail())) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid email", "Error", JOptionPane.ERROR_MESSAGE);
                    }else{
                        connectorDB.statement = connectorDB.koneksi.createStatement();
                        String query = ("INSERT INTO user VALUES (NULL,'"+getNama()+"','"+getEmail()+"','"+getPassword()+"')");

                        connectorDB.statement.executeUpdate(query);

                        JOptionPane.showMessageDialog(null,"Berhasil Disimpan!");

                        frame.setVisible(false);
                        new adminPage(emailAdmin,passAdmin);
                    }

                } catch (SQLException er){
                    JOptionPane.showMessageDialog(null, "Data Gagal Disimpan!" + er, "Hasil", JOptionPane.ERROR_MESSAGE );
                }
            }
        });

        //update data user oleh admin
        ubahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (getEmail().equals("")||getNama().equals("")||getPassword().equals("")){
                        JOptionPane.showMessageDialog(null,"Data Tidak Boleh Kosong!", "Peringatan",JOptionPane.INFORMATION_MESSAGE);
                    } else if(!Pattern.matches("^[a-zA-Z0-9]+[@]{1}+[a-zA-Z0-9]+[.]{1}+[a-zA-Z0-9]+$",getEmail())) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid email", "Error", JOptionPane.ERROR_MESSAGE);
                    }else {
                        connectorDB.statement = connectorDB.koneksi.createStatement();
                        if (getId_user().equals("")){
                            JOptionPane.showMessageDialog(null, "Tidak Dapat Mengubah Data!", "Hasil", JOptionPane.ERROR_MESSAGE );
                        } else {
                            String query = ("UPDATE user SET nama = '"+getNama()+"', email = '"+getEmail()+"', password = '"+getPassword()+"' WHERE id_user = '"+getId_user()+"'");
                            connectorDB.statement.executeUpdate(query);

                            JOptionPane.showMessageDialog(null,"Berhasil Disimpan!");

                            frame.setVisible(false);
                            new adminPage(emailAdmin,passAdmin);
                        }

                    }

                } catch (SQLException er){
                    JOptionPane.showMessageDialog(null, "Data Gagal Disimpan!" + er, "Hasil", JOptionPane.ERROR_MESSAGE );
                }
            }
        });

        //hapus data user oleh admin
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (getId_user().equals("")){
                        JOptionPane.showMessageDialog(null, "Tidak Dapat Menghapus Data!", "Hasil", JOptionPane.ERROR_MESSAGE );
                    } else {
                        connectorDB.statement = connectorDB.koneksi.createStatement();
                        String query = ("DELETE FROM user WHERE user.id_user = '" +getId_user()+ "'");
                        connectorDB.statement.executeUpdate(query);

                        JOptionPane.showMessageDialog(null, "Berhasil di Hapus!", "Peringatan", JOptionPane.INFORMATION_MESSAGE);

                        frame.setVisible(false);
                        new adminPage(emailAdmin, passAdmin);
                    }

                }catch (SQLException er){
                    JOptionPane.showMessageDialog(null, "Data Gagal Di Hapus", "Peringatan!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //mereset form
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetData();
            }
        });

        //menampilkan data dari tabel ke form
        tbUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tbUser.getSelectedRow();
                if (i>-1){
                    tfIdUser.setText(model.getValueAt(i,0).toString());
                    tfNamaUser.setText(model.getValueAt(i,1).toString());
                    tfEmailUser.setText(model.getValueAt(i,2).toString());
                    tfPassUser.setText(model.getValueAt(i,3).toString());
                }
            }
        });

        //keluar page admin
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new login();
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    //menampilakn data dari database ke tabel
    @Override
    void tampilTabel(){
        int row = tbUser.getRowCount();

        for(int a=0; a<row; a++){
            model.removeRow(0);
        }

        try {
            connectorDB.statement = connectorDB.koneksi.createStatement();

            String query = ("SELECT * FROM user");
            ResultSet resultSet = connectorDB.statement.executeQuery(query);
            while (resultSet.next()){
                String data [] = {resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)};
                model.addRow(data);
            }

        }catch (SQLException er){
            JOptionPane.showMessageDialog(null,"Gagal Terhubung Database!");
        }
    }

    @Override
    public void detailData(){
        try{
            connectorDB.statement = connectorDB.koneksi.createStatement();
            String query = ("SELECT * FROM admin WHERE email = '"+email+"' AND password = '"+password+"'");
            ResultSet rs = connectorDB.statement.executeQuery(query);
            while (rs.next()){
                id = rs.getString(1);
                nama = rs.getString(2);
            }
        }catch (SQLException er){
            JOptionPane.showMessageDialog(null,"Gagal Terhubung Database!" + er);
        }
    }

    @Override
    public void resetData(){
        tfNamaUser.setText("");
        tfEmailUser.setText("");
        tfPassUser.setText("");
        tfIdUser.setText("");
    }

    private String getId_user(){
        return tfIdUser.getText();
    }

    private String getNama(){
        return tfNamaUser.getText();
    }

    private String getEmail(){
        return tfEmailUser.getText();
    }

    private String getPassword(){
        return String.valueOf(tfPassUser.getPassword());
    }


}
