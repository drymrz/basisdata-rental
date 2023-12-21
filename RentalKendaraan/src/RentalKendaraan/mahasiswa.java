/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RentalKendaraan;
import java.sql.ResultSet;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.*;
/**
 *
 * @author Mrz
 */
public class mahasiswa extends javax.swing.JFrame {
    public Connection conn;
    public Statement cn;
    /**
     * Creates new form mahasiswa
     */
    public mahasiswa() {
        initComponents();
        tampilData();
        initComboData();
        initComboProdi();
        bersih();
    }
    
    public void dbConn(){
         try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection
            ("jdbc:mysql://localhost:3306/dbakademik_2221037","root","");
            cn = conn.createStatement();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"koneksi gagal..");
            System.out.println(e.getMessage());
        }
    }
    
    public void tampilData(){
        DefaultTableModel tabelnyo = new DefaultTableModel();
        tabelnyo.addColumn("NIM");
        tabelnyo.addColumn("Nama");
        tabelnyo.addColumn("Tempat Lahir");
        tabelnyo.addColumn("Tanggal Lahir");
        tabelnyo.addColumn("No HP");
        tabelnyo.addColumn("Alamat");
        tabelnyo.addColumn("Jenis Kelamin");
        tabelnyo.addColumn("Nama Ortu");
        tabelnyo.addColumn("Fakultas");
        tabelnyo.addColumn("Program Studi");

        try{
            dbConn();
            String sql = "Select * from mahasiswa_2221037";
            ResultSet rs = cn.executeQuery(sql);
            while(rs.next()){
                tabelnyo.addRow(new Object[]{
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getString(6),
                rs.getString(7),
                rs.getString(8),    
                rs.getString(9),
                rs.getString(10)
                });
            }
            jTable1.setModel(tabelnyo);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Ada Kesalahan");
        }
    }
    
    public void initComboData(){
        jComboJK.removeAllItems();
        jComboJK.addItem("Laki-Laki");
        jComboJK.addItem("Perempuan");
        
        jComboFakultas.removeAllItems();
        jComboFakultas.addItem("Fakultas Teknologi Informasi");
        jComboFakultas.addItem("Fakultas Teknologi Industri");
        
    }
    
    public void initComboProdi(){
        jComboProdi.removeAllItems();
        String x = String.valueOf(jComboFakultas.getSelectedItem());

        if (x == "Fakultas Teknologi Informasi"){
            jComboProdi.addItem("Sistem Informasi");
            jComboProdi.addItem("Desain Komunikasi Visual");
            jComboProdi.addItem("Teknik Komputer");
            jComboProdi.addItem("Matematika");
       }else{
            jComboProdi.addItem("Teknik Industri");
            jComboProdi.addItem("Managemen Rekayasa");
            jComboProdi.addItem("Perdagangan Internasional");           
        }
    }
    
    public void bersih(){
        txtNim.setText("");
        txtNama.setText("");
        txtTempat.setText("");
        txtAlamat.setText("");
        txtNoHP.setText("");
        txtOrtu.setText("");
    }
    
    public void validasiMahasiswa(){
        try {
        dbConn();
        String sql = "Select * from mahasiswa_2221037 where nimmhs='"+txtNim.getText()+"'";
        ResultSet rs = cn.executeQuery(sql);
        if (rs.next()){
            txtNim.setText(rs.getString("nimmhs"));
            txtNama.setText(rs.getString("namamhs"));
            txtTempat.setText(rs.getString("tempatlahir"));
            txtAlamat.setText(rs.getString("alamat"));
            txtNoHP.setText(rs.getString("nohp"));
            txtOrtu.setText(rs.getString("namaortu"));
            jComboJK.setSelectedItem(rs.getString("jeniskelamin"));
            jComboFakultas.setSelectedItem(rs.getString("fakultas"));
            jComboProdi.setSelectedItem(rs.getString("prodi"));
            String date = rs.getString("tanggallahir");
            java.util.Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            jDateChooser1.setDate(date2);
            try {
                int kel = JOptionPane.showConfirmDialog(this, "NIM Mahasiswa :"+ txtNim.getText() + " Sudah ada, Mau Di Edit atau di hapus...?","INFORMASI",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(kel==JOptionPane.NO_OPTION){
                    bersih();
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
            } catch (Exception e) {
            }
        }
        } catch (Exception e) {
        }
    }
    
    public void cariMahasiswa(){
        String a = new String(txtSearch.getText());
        if (a.equals("")){
            JOptionPane.showMessageDialog(null, "Field Masih kosong..","INFORMASI",JOptionPane.INFORMATION_MESSAGE);
            txtSearch.requestFocus();
        }else{
            DefaultTableModel tblmahasiswa= new DefaultTableModel();
            tblmahasiswa.addColumn("NIM");
            tblmahasiswa.addColumn("Nama");
            tblmahasiswa.addColumn("Tempat Lahir");
            tblmahasiswa.addColumn("Tanggal Lahir");
            tblmahasiswa.addColumn("No HP");
            tblmahasiswa.addColumn("Alamat");
            tblmahasiswa.addColumn("Jenis Kelamin");
            tblmahasiswa.addColumn("Nama Ortu");
            tblmahasiswa.addColumn("Fakultas");
            tblmahasiswa.addColumn("Program Studi");
            try {
                dbConn();
                String sql="Select * from mahasiswa_2221037 where "+"nimmhs like '%"+ txtSearch.getText() +"%' or"+"namamhs like'%"+ txtSearch.getText()+"%'";
                ResultSet rs = cn.executeQuery(sql);
                while (rs.next())
                {
                    tblmahasiswa.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getString(8),
                    rs.getString(9),
                    rs.getString(10),
                    });
                }
                jTable1.setModel(tblmahasiswa);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Data tidak ditemukan..","INFORMASI",JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtNim = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtTempat = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtNoHP = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtAlamat = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtOrtu = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jComboJK = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jComboFakultas = new javax.swing.JComboBox<>();
        jComboProdi = new javax.swing.JComboBox<>();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        saveBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        exitBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 24)); // NOI18N
        jLabel1.setText("INPUT DATA MAHASISWA");

        jLabel2.setText("CARI BERDASARKAN NAMA/NIM MAHASISWA");

        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(153, 255, 153));

        jLabel3.setText("NIM MAHASISWA");

        txtNim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNimActionPerformed(evt);
            }
        });

        jLabel4.setText("NAMA MAHASISWA");

        jLabel5.setText("TEMPAT LAHIR MAHASISWA");

        jLabel6.setText("TANGGAL LAHIR MAHASISWA");

        jLabel7.setText("NO HP MAHASISWA");

        jLabel8.setText("ALAMAT MAHASISWA");

        jLabel9.setText("JENIS KELAMIN");

        jLabel10.setText("NAMA ORTU");

        jComboJK.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel11.setText("FAKULTAS");

        jLabel12.setText("PROGRAM STUDI");

        jComboFakultas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboFakultas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboFakultasActionPerformed(evt);
            }
        });

        jComboProdi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboProdi.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboProdiItemStateChanged(evt);
            }
        });
        jComboProdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboProdiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtNim, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 109, Short.MAX_VALUE)
                        .addComponent(txtTempat, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtOrtu)
                            .addComponent(jComboJK, 0, 250, Short.MAX_VALUE)
                            .addComponent(jComboFakultas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboProdi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNoHP)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTempat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNoHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jComboJK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOrtu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jComboFakultas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jComboProdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(153, 255, 153));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(153, 255, 153));

        saveBtn.setText("SIMPAN");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        editBtn.setText("EDIT");
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        deleteBtn.setText("HAPUS");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        exitBtn.setText("KELUAR");
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(2, 2, 2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboProdiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboProdiActionPerformed
        
    }//GEN-LAST:event_jComboProdiActionPerformed

    private void jComboProdiItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboProdiItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboProdiItemStateChanged

    private void jComboFakultasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboFakultasActionPerformed
        // TODO add your handling code here:
        initComboProdi();
    }//GEN-LAST:event_jComboFakultasActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");
            String theDate = dateFormat.format(jDateChooser1.getDate());
            dbConn();
            String sql = "insert into mahasiswa_2221037 values('"+ txtNim.getText() +"','"+txtNama.getText() +"','"+ txtTempat.getText() +"','"+ 
                    theDate +"','"+ txtNoHP.getText() +"','"+ txtAlamat.getText() +"','"+ 
                    jComboJK.getSelectedItem() +"', '"+txtOrtu.getText() +"','"+ jComboFakultas.getSelectedItem() +"','"+ jComboProdi.getSelectedItem() +"')";
            cn.executeUpdate(sql);
            conn.close();
            tampilData();
            bersih();
            JOptionPane.showMessageDialog(null,"Data berhasil di simpan");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Proses penyimpanan gagal" + e);
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");
            String theDate = dateFormat.format(jDateChooser1.getDate());
            dbConn();
            String sql = "update mahasiswa_2221037 set namamhs='"+ txtNama.getText() +"'," + "tempatlahir='" + txtTempat.getText() + "', " + "tanggallahir='" + theDate + "'," + 
                         "nohp='" + txtTempat.getText() + "'," + "alamat='" + txtAlamat.getText() + "'," + "jeniskelamin='" + jComboJK.getSelectedItem() + "'," + "namaortu='" + txtOrtu.getText() + "'," + "fakultas='" + jComboFakultas.getSelectedItem() + "'," + "prodi='" + jComboProdi.getSelectedItem() + "'" +
                         "where nimmhs='" + txtNim.getText() + "'";
            cn.executeUpdate(sql);
            cn.close();
            tampilData();
            bersih();
            JOptionPane.showMessageDialog(null,"Data berhasil diedit");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Proses edit gagal" + e);
        }
    }//GEN-LAST:event_editBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        try {
            getToolkit().beep();
            int keluar = JOptionPane.showConfirmDialog(this, "Anda Yakin Ingin MeghapusIni..?","PERINGATAN",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if(keluar==JOptionPane.YES_OPTION){
                try {
                    dbConn();
                    String sql = "delete from mahasiswa_2221037 where nimmhs='"+ txtNim.getText() +"'";
                    cn.executeUpdate(sql);
                    cn.close();
                    tampilData();
                    bersih();
                    JOptionPane.showMessageDialog(null,"Data berhasil dihapus");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,"Deleting failed..");
                }
            }
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void txtNimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNimActionPerformed
        validasiMahasiswa();
    }//GEN-LAST:event_txtNimActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        cariMahasiswa();
    }//GEN-LAST:event_txtSearchActionPerformed

    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
        this.dispose();
        new menu().show();
    }//GEN-LAST:event_exitBtnActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        try{
        int tabel = jTable1.getSelectedRow();
        txtNim.setText(jTable1.getValueAt(tabel, 0).toString());
        txtNama.setText(jTable1.getValueAt(tabel, 1).toString());
        txtTempat.setText(jTable1.getValueAt(tabel, 2).toString());
        
        String date = jTable1.getValueAt(tabel, 3).toString();
        java.util.Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        jDateChooser1.setDate(date2);
        
        txtNoHP.setText(jTable1.getValueAt(tabel, 4).toString());
        txtAlamat.setText(jTable1.getValueAt(tabel, 5).toString());  
        jComboJK.setSelectedItem(jTable1.getValueAt(tabel, 6).toString());
        txtOrtu.setText(jTable1.getValueAt(tabel, 7).toString()); 
        jComboFakultas.setSelectedItem(jTable1.getValueAt(tabel, 8).toString());
        jComboProdi.setSelectedItem(jTable1.getValueAt(tabel, 9).toString());
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_jTable1MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mahasiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mahasiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mahasiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mahasiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mahasiswa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JButton exitBtn;
    private javax.swing.JComboBox<String> jComboFakultas;
    private javax.swing.JComboBox<String> jComboJK;
    private javax.swing.JComboBox<String> jComboProdi;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNim;
    private javax.swing.JTextField txtNoHP;
    private javax.swing.JTextField txtOrtu;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTempat;
    // End of variables declaration//GEN-END:variables
}
