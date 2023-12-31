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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.*;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

/**
 *
 * @author Mrz
 */
public class penyewaan extends javax.swing.JFrame {
    public Connection conn;
    public Statement cn;
    String idJenis = "";
    public ArrayList<String> idPenyewa = new ArrayList<>();
    public ArrayList<String> idKendaraan = new ArrayList<>();
    public ArrayList<String> tahunKendaraan = new ArrayList<>();
    
    /**
     * Creates new form penyewaan
     */
    public penyewaan() {
        initComponents();
        tampilData();
        validateAvailabilty();
        txtIdPenyewa.setVisible(false);
        txtIdKendaraan.setVisible(false);
        txtHarga.getDocument().addDocumentListener(new DocumentListener() {
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                Date startDate = jDateChooser1.getDate();
                Date endDate = jDateChooser2.getDate();
                if (endDate != null && startDate != null) {
                    double harga = Double.parseDouble(txtHarga.getText());
                    double hari = Double.parseDouble(txtHari.getText());
                    txtTotal.setText(String.valueOf(harga * hari));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!txtTotal.getText().trim().isEmpty()){
                    txtTotal.setText("");
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });
    }
    
    public void dbConn(){
         try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection
            ("jdbc:mysql://localhost:3306/db_rentalkendaraan","root","");
            cn = conn.createStatement();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"koneksi gagal..");
            System.out.println(e.getMessage());
        }
    }
    
    public void tampilData(){
        DefaultTableModel tabelnyo = new DefaultTableModel();
        tabelnyo.addColumn("ID Sewa");
        tabelnyo.addColumn("Nama Penyewa");
        tabelnyo.addColumn("Merek Kendaraan");
        tabelnyo.addColumn("Model");
        tabelnyo.addColumn("Nomor Polisi");
        tabelnyo.addColumn("Warna");
        tabelnyo.addColumn("Tanggal Sewa");
        tabelnyo.addColumn("Tanggal Selesai");
        tabelnyo.addColumn("Jumlah Hari");
        tabelnyo.addColumn("Harga per hari");
        tabelnyo.addColumn("Total Biaya");

        try{
            dbConn();
            String sql = "Select tbl_penyewaan.id_penyewaan, tbl_penyewa.id_penyewa, tbl_penyewa.nama, tbl_merek.merek, tbl_kendaraan.id_kendaraan, tbl_kendaraan.model, tbl_kendaraan.tahun_pembuatan,"
                    + "tbl_kendaraan.nomor_polisi, tbl_kendaraan.warna, tbl_penyewaan.tanggal_sewa, tbl_penyewaan.tanggal_pengembalian, "
                    + "tbl_penyewaan.jumlah_hari, tbl_kendaraan.harga_sewa_per_hari, tbl_penyewaan.total_biaya from tbl_penyewaan "
                    + "join tbl_penyewa on tbl_penyewa.id_penyewa = tbl_penyewaan.id_penyewa "
                    + "join tbl_kendaraan on tbl_kendaraan.id_kendaraan = tbl_penyewaan.id_kendaraan "
                    + "join tbl_merek on tbl_merek.id_merek = tbl_kendaraan.id_merek";
            ResultSet rs = cn.executeQuery(sql);
            while(rs.next()){
                idPenyewa.add(rs.getString(2));
                idKendaraan.add(rs.getString(5));
                tahunKendaraan.add(rs.getString(6));
                tabelnyo.addRow(new Object[]{
                rs.getString(1),
                rs.getString(3),
                rs.getString(4),
                rs.getString(6),
                rs.getString(8),
                rs.getString(9),
                rs.getString(10),
                rs.getString(11),    
                rs.getString(12),
                rs.getString(13),
                rs.getString(14),
                });
            }
            jTable1.setModel(tabelnyo);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Ada Kesalahan" + e.getMessage());
        }
        
        try{
            jComboBox1.removeAllItems();
            dbConn();
            String sql = "select * from tbl_jenis";
            ResultSet rs = cn.executeQuery(sql);
            
            while (rs.next()) {
                String item = rs.getString("jenis");
                jComboBox1.addItem(item);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Ada Kesalahan" + e.getMessage());
        }
    }
    
    public void validateAvailabilty(){
        try {
            dbConn();
            String sql = "WITH RankedData AS (SELECT id_penyewaan, id_kendaraan, tanggal_pengembalian, ROW_NUMBER() OVER "
                    + "(PARTITION BY id_kendaraan ORDER BY tanggal_pengembalian DESC) AS rn FROM tbl_penyewaan) "
                    + "SELECT id_penyewaan, id_kendaraan, tanggal_pengembalian FROM RankedData WHERE rn = 1";
            ResultSet rs = cn.executeQuery(sql);

            while (rs.next()) {
                String endDateString = rs.getString("tanggal_pengembalian");
                Date endDate = java.sql.Date.valueOf(endDateString);
                Date currentDate = new java.sql.Date(System.currentTimeMillis());

                if (endDate.before(currentDate)) {
                    try (Statement updateStatement = conn.createStatement()) {
                        updateStatement.executeUpdate("UPDATE tbl_kendaraan SET status_tersedia = '1' WHERE id_kendaraan = '" + rs.getString(2) + "'");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ada Kesalahan" + e.getMessage());
                    }   
                }
            }

            // Close the ResultSet after the loop
            rs.close();

            // Close the main connection
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ada Kesalahan" + e.getMessage());
        }
    }
    
    public void clearForm(){
        txtIdSewa.setText("");
        txtNama.setText("");
        txtIdPenyewa.setText("");
        txtModel.setText("");
        txtMerek.setText("");
        txtTahun.setText("");
        txtNopol.setText("");
        txtWarna.setText("");
        txtHarga.setText("");
        txtHari.setText("");
        txtTotal.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtIdSewa = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtMerek = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtModel = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtNopol = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtWarna = new javax.swing.JTextField();
        txtTahun = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtHarga = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel12 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        txtHari = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        txtIdPenyewa = new javax.swing.JTextField();
        txtIdKendaraan = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("ID Penyewaan");

        jLabel2.setText("Pilih Penyewa");

        jLabel3.setText("Nama Penyewa");

        txtNama.setEditable(false);
        txtNama.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("Jenis Kendaraan");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jComboBox1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jComboBox1PropertyChange(evt);
            }
        });

        jLabel5.setText("Pilih Kendaraan");

        jButton1.setText("..");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel6.setText("Merek");

        txtMerek.setEditable(false);
        txtMerek.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setText("Model");

        txtModel.setEditable(false);
        txtModel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setText("Nomor Polisi");

        txtNopol.setEditable(false);
        txtNopol.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setText("Warna");

        txtWarna.setEditable(false);
        txtWarna.setBackground(new java.awt.Color(255, 255, 255));

        txtTahun.setEditable(false);
        txtTahun.setBackground(new java.awt.Color(255, 255, 255));

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

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("History Penyewaan");

        jLabel11.setText("Harga Sewa/Hari");

        txtHarga.setEditable(false);
        txtHarga.setBackground(new java.awt.Color(255, 255, 255));
        txtHarga.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtHargaPropertyChange(evt);
            }
        });

        jLabel12.setText("Tanggal Sewa");

        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jLabel13.setText("Tanggal Selesai");

        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        jLabel14.setText("Jumlah Hari");

        txtHari.setEditable(false);
        txtHari.setBackground(new java.awt.Color(255, 255, 255));
        txtHari.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                txtHariComponentShown(evt);
            }
        });
        txtHari.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtHariPropertyChange(evt);
            }
        });

        jLabel15.setText("Total Biaya");

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(255, 255, 255));

        jButton2.setText("Simpan");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Update");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Hapus");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("..");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jMenu1.setText("Data");

        jMenu2.setText("Kendaraan");

        jMenuItem3.setText("Jenis");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setText("Merek");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem1.setText("Model");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenu1.add(jMenu2);

        jMenuItem2.setText("Penyewa");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtIdSewa)
                                    .addComponent(txtNama, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                                    .addComponent(jButton5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIdPenyewa, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel11))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton1)
                                    .addComponent(jComboBox1, 0, 119, Short.MAX_VALUE)
                                    .addComponent(txtMerek)
                                    .addComponent(txtModel)
                                    .addComponent(txtNopol)
                                    .addComponent(txtWarna)
                                    .addComponent(txtHarga))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTahun, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtIdKendaraan, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel15)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel13)
                                            .addComponent(jLabel14))
                                        .addGap(25, 25, 25))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addGap(32, 32, 32)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTotal)
                                    .addComponent(txtHari, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtIdSewa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jButton5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdPenyewa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jButton1)
                            .addComponent(txtIdKendaraan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtMerek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtNopol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtWarna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtHari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton3)
                            .addComponent(jButton4))
                        .addContainerGap(17, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1)
                        .addContainerGap())))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        new tambahMerek().show();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new kendaraan().show();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        try{
            int row;
            row=jTable1.getSelectedRow();
            txtIdSewa.setText(jTable1.getValueAt(row, 0).toString());
            txtNama.setText(jTable1.getValueAt(row, 1).toString());
            txtIdPenyewa.setText(idPenyewa.get(row));
            txtIdKendaraan.setText(idKendaraan.get(row));
            txtMerek.setText(jTable1.getValueAt(row, 2).toString());
            txtModel.setText(jTable1.getValueAt(row, 3).toString());
            txtTahun.setText(tahunKendaraan.get(row));
            txtNopol.setText(jTable1.getValueAt(row, 4).toString());
            txtWarna.setText(jTable1.getValueAt(row, 5).toString());
            txtHari.setText(jTable1.getValueAt(row, 8).toString());
            jDateChooser1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(jTable1.getValueAt(row, 6).toString()));
            jDateChooser2.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(jTable1.getValueAt(row, 7).toString()));
            txtHarga.setText(jTable1.getValueAt(row, 9).toString());
            txtTotal.setText(jTable1.getValueAt(row, 10).toString());
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        new penyewa().show();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        Date startDate = jDateChooser1.getDate();
        Date endDate = jDateChooser2.getDate();

        if (endDate != null && startDate != null) {
            // Calculate the difference in milliseconds
            long differenceInMilliseconds = endDate.getTime() - startDate.getTime();

            // Convert milliseconds to days
            long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMilliseconds);

            // Set the result to the JTextField
            txtHari.setText(String.valueOf(differenceInDays));
            
            if (!txtHarga.getText().trim().isEmpty()){
            double harga = Double.parseDouble(txtHarga.getText());
            // Calculate the result
            double result = harga * differenceInDays;
            
            txtTotal.setText(String.valueOf(result));
            }else{
                System.out.println("harga empty");
            }
        }
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        Date startDate = jDateChooser1.getDate();
        Date endDate = jDateChooser2.getDate();

        if (endDate != null && startDate != null) {
            // Calculate the difference in milliseconds
            long differenceInMilliseconds = endDate.getTime() - startDate.getTime();

            // Convert milliseconds to days
            long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMilliseconds);

            // Set the result to the JTextField
            txtHari.setText(String.valueOf(differenceInDays));
            
            if (!txtHarga.getText().trim().isEmpty()){
            double harga = Double.parseDouble(txtHarga.getText());
            // Calculate the result
            double result = harga * differenceInDays;
            
            txtTotal.setText(String.valueOf(result));
            }else{
                System.out.println("harga empty");
            }
        }
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void txtHariComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_txtHariComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHariComponentShown

    private void jComboBox1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jComboBox1PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1PropertyChange

    private void txtHariPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtHariPropertyChange

    }//GEN-LAST:event_txtHariPropertyChange

    private void txtHargaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtHargaPropertyChange

    }//GEN-LAST:event_txtHargaPropertyChange

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        txtModel.setText("");
        txtMerek.setText("");
        txtTahun.setText("");
        txtNopol.setText("");
        txtWarna.setText("");
        txtHarga.setText("");
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");
            String startDate = dateFormat.format(jDateChooser1.getDate());
            String endDate = dateFormat.format(jDateChooser2.getDate());
            
            dbConn();
            String sql = "insert into tbl_penyewaan values('"+ txtIdSewa.getText() +"', '"+ txtIdKendaraan.getText() +"', '"+ txtIdPenyewa.getText() +"', '"+ startDate +"', '"+ endDate +"', '"+ txtHari.getText() +"', '"+ txtTotal.getText() +"')";
            cn.executeUpdate(sql);
            conn.close();
            tampilData();
            clearForm();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Ada Kesalahan" + e.getMessage());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        new tambahKendaraan().show();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        new tambahPenyewa().show();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        new tambahJenis().show();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            getToolkit().beep();
            int keluar = JOptionPane.showConfirmDialog(this, "Anda Yakin Ingin Meghapus Ini..?","PERINGATAN",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if(keluar==JOptionPane.YES_OPTION){
                try {
                    dbConn();
                    String sql = "delete from tbl_penyewaan where id_penyewaan='"+ txtIdSewa.getText() +"'";
                    cn.executeUpdate(sql);
                    cn.close();
                    tampilData();
                    clearForm();
                    JOptionPane.showMessageDialog(null,"Data berhasil dihapus");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,"Deleting failed..");
                }
            }
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");
            String startDate = dateFormat.format(jDateChooser1.getDate());
            String endDate = dateFormat.format(jDateChooser2.getDate());
            
            dbConn();
            String sql = "update tbl_penyewaan set id_kendaraan='"+ txtIdKendaraan.getText() +"', id_penyewa='"+ txtIdPenyewa.getText() +"', tanggal_sewa='"+ startDate +"', tanggal_pengembalian='"+ endDate +"', jumlah_hari='"+ txtHari.getText() +"', total_biaya='"+ txtTotal.getText() +"' where id_penyewaan='" + txtIdSewa.getText() + "'";
            cn.executeUpdate(sql);
            conn.close();
            tampilData();
            clearForm();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Ada Kesalahan" + e.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(penyewaan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(penyewaan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(penyewaan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(penyewaan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new penyewaan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    public static javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    public static javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtHari;
    public static javax.swing.JTextField txtIdKendaraan;
    public static javax.swing.JTextField txtIdPenyewa;
    private javax.swing.JTextField txtIdSewa;
    public static javax.swing.JTextField txtMerek;
    public static javax.swing.JTextField txtModel;
    public static javax.swing.JTextField txtNama;
    public static javax.swing.JTextField txtNopol;
    public static javax.swing.JTextField txtTahun;
    private javax.swing.JTextField txtTotal;
    public static javax.swing.JTextField txtWarna;
    // End of variables declaration//GEN-END:variables
}
