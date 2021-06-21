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

public class userPage extends Data{
    private JTextField tfJudul;
    private JTextArea taIsiCerita;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton resetButton;
    private JPanel userPanel;
    private JTable tbCerita;
    private JTextField tfIdCerita;
    private JButton btnLogout;
    private JTextField tfNamaUser;

    connectorDB connectorDB = new connectorDB();

    DefaultTableModel model;

    public userPage(String emailUser, String passUser) {
        JFrame frame = new JFrame("Responsi Praktikum PBO");
        frame.setContentPane(userPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //proses cek detail data user
        email = emailUser;
        password = passUser;
        detailData();
        tfNamaUser.setText(nama);

        //setting tabel
        String [] judul = {"ID","Judul","Isi Cerita"};
        model = new DefaultTableModel(judul,0);
        tbCerita.setModel(model);
        tampilTabel();

        //menyimpan data cerita ke database
        tambahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!getId_Cerita().equals("")) {
                        JOptionPane.showMessageDialog(null, "Reset Data Dahulu!", "Peringatan", JOptionPane.ERROR_MESSAGE);
                    }else if (getJudul().equals("")||getIsi().equals("")){
                        JOptionPane.showMessageDialog(null,"Data Tidak Boleh Kosong!", "Peringatan",JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        connectorDB.statement = connectorDB.koneksi.createStatement();
                        String query = ("INSERT INTO cerita_user VALUES (NULL,'"+id+"','"+getJudul()+"','"+getIsi()+"')");

                        connectorDB.statement.executeUpdate(query);

                        JOptionPane.showMessageDialog(null,"Berhasil Disimpan!");

                        frame.setVisible(false);
                        new userPage(emailUser,passUser);
                    }

                } catch (SQLException er){
                    JOptionPane.showMessageDialog(null, "Data Gagal Disimpan!" + er, "Hasil", JOptionPane.ERROR_MESSAGE );
                }
            }
        });

        //Update data cerita
        ubahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if (getJudul().equals("")||getIsi().equals("")){
                        JOptionPane.showMessageDialog(null,"Data Tidak Boleh Kosong!", "Peringatan",JOptionPane.INFORMATION_MESSAGE);
                    }else {
                        connectorDB.statement = connectorDB.koneksi.createStatement();
                        if (getId_Cerita().equals("")){
                            JOptionPane.showMessageDialog(null, "Tidak Dapat Mengubah Data!", "Hasil", JOptionPane.ERROR_MESSAGE );
                        } else {
                            String query = ("UPDATE cerita_user SET judul = '"+getJudul()+"', isi_cerita = '"+getIsi()+"' WHERE id_cerita = '"+getId_Cerita()+"'");
                            connectorDB.statement.executeUpdate(query);

                            JOptionPane.showMessageDialog(null,"Berhasil Disimpan!");

                            frame.setVisible(false);
                            new userPage(emailUser,passUser);
                        }
                    }
                }catch (SQLException er){
                    JOptionPane.showMessageDialog(null, "Data Gagal Di Ubah!" + er, "Hasil", JOptionPane.ERROR_MESSAGE );
                }
            }
        });

        //Delete data cerita
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (getId_Cerita().equals("")){
                        JOptionPane.showMessageDialog(null, "Tidak Dapat Menghapus Data!", "Hasil", JOptionPane.ERROR_MESSAGE );
                    } else {
                        connectorDB.statement = connectorDB.koneksi.createStatement();
                        String query = ("DELETE FROM cerita_user WHERE cerita_user.id_cerita = '"+getId_Cerita()+"'");
                        connectorDB.statement.executeUpdate(query);

                        JOptionPane.showMessageDialog(null, "Berhasil di Hapus!", "Peringatan", JOptionPane.INFORMATION_MESSAGE);

                        frame.setVisible(false);
                        new userPage(emailUser,passUser);
                    }

                }catch (SQLException er){
                    JOptionPane.showMessageDialog(null, "Data Gagal Di Hapus", "Peringatan!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //membersihkan isian form
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetData();
            }
        });

        //mengirimkan data dari tabel ke form
        tbCerita.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tbCerita.getSelectedRow();
                if (i>-1){
                    tfIdCerita.setText(model.getValueAt(i,0).toString());
                    tfJudul.setText(model.getValueAt(i,1).toString());
                    taIsiCerita.setText(model.getValueAt(i,2).toString());
                }
            }
        });

        //button logout user
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new login();
            }
        });
    }

    //menampilkan isi tabel dari database
    @Override
     void tampilTabel() {
        int row = tbCerita.getRowCount();

        for(int a=0; a<row; a++){
            model.removeRow(0);
        }

        try {
            connectorDB.statement = connectorDB.koneksi.createStatement();

            String query = ("SELECT * FROM cerita_user INNER JOIN user ON cerita_user.id_user = user.id_user WHERE cerita_user.id_user = '"+id+"'");
            ResultSet resultSet = connectorDB.statement.executeQuery(query);
            while (resultSet.next()){
                String data [] = {resultSet.getString(1),resultSet.getString(3),resultSet.getString(4)};
                model.addRow(data);
            }

        }catch (SQLException er){
            JOptionPane.showMessageDialog(null,"Gagal Terhubung Database!");
        }
    }

    @Override
     void detailData(){
        try{
            connectorDB.statement = connectorDB.koneksi.createStatement();
            String query = ("SELECT * FROM user WHERE email = '"+email+"' AND password = '"+password+"'");
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
    void resetData(){
        tfIdCerita.setText("");
        tfJudul.setText("");
        taIsiCerita.setText("");
    }

    private String getId_Cerita(){
        return tfIdCerita.getText();
    }

    private String getJudul(){
        return tfJudul.getText();
    }

    private String getIsi(){
        return taIsiCerita.getText();
    }



}
