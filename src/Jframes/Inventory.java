/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Jframes;

import Database.DBConnection;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author USER
 */
public class Inventory extends javax.swing.JInternalFrame {

    /**
     * Creates new form DashBoard
     */
    
    
    
    String productid;
    String productname;
    String type;
    String status;
    int stock;
    int price;
     String date;
    
    String path =null; 
    
    
    public Inventory() {
        initComponents();
        
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        
        BasicInternalFrameUI bi = (BasicInternalFrameUI)this.getUI();
        bi.setNorthPane(null);
        autoID();
        tableLoard();
    }
    
     
    private void playnotificationMusic() {
    try {
        // Load the audio file
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/Music/Outlook notification sound.wav"));

        // Create a clip to play the audio
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);

        // Play the audio once
        clip.start();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
     private void tableLoard(){
       
        try{
        String sql = "SELECT product_id, product_name, type, stock, price, status, image, date FROM inventory";
        Connection con = DBConnection.getConnection();
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        DefaultTableModel tableModel = resultSetToTableModel(rs);
        tblItem.setModel(tableModel);
        
        // Close the resources
        rs.close();
        pst.close();
       
    } catch (SQLException e) {
        playnotificationMusic();
        JOptionPane.showMessageDialog(this, "Error occurred while loading data: " + e.getMessage());
    }
    }
    
    private DefaultTableModel resultSetToTableModel(ResultSet rs) throws SQLException {
    ResultSetMetaData metaData = rs.getMetaData();

    // Get column names
    int columnCount = metaData.getColumnCount();
    String[] columnNames = new String[columnCount];
    for (int i = 1; i <= columnCount; i++) {
        columnNames[i - 1] = metaData.getColumnName(i);
    }

    // Get row data
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    while (rs.next()) {
        Object[] rowData = new Object[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            rowData[i - 1] = rs.getObject(i);
        }
        tableModel.addRow(rowData);
    }

    return tableModel;
}
    
    
    private void autoID() {
    try {
        String sql = "SELECT product_id FROM inventory ORDER BY product_id DESC LIMIT 1";
        Connection con = DBConnection.getConnection();
        PreparedStatement pst = con.prepareStatement(sql);
         ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            String rnno = rs.getString("product_id");
            int oo = rnno.length();
            String txt = rnno.substring(0, 3);
            String num = rnno.substring(3, oo);
            int n = Integer.parseInt(num);
            n++;
            String snum = Integer.toString(n);
            String ftxt = txt + snum;
            txtproductID.setText(ftxt);
        } else {
            txtproductID.setText("PID1000");
        }
        
        rs.close();
        pst.close();
        
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(rootPane, ex.getMessage());
    }
}
    
    
    
    
    
    
    
     
    private void getData(){
        productid = txtproductID.getText();
        productname = txtproductname.getText();
        type = cmbType.getSelectedItem().toString();
        status = cmbstatus.getSelectedItem().toString();
        stock = Integer.parseInt(txtstock.getText());
        price = Integer.parseInt(txtprice.getText());
       LocalDate ndate = LocalDate.now();
        date = ndate+"";
   
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
        jPanel11 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItem = new rojerusan.RSTableMetro();
        jPanel12 = new javax.swing.JPanel();
        label5 = new java.awt.Label();
        label6 = new java.awt.Label();
        label7 = new java.awt.Label();
        label9 = new java.awt.Label();
        label10 = new java.awt.Label();
        txtprice = new javax.swing.JTextField();
        txtproductID = new javax.swing.JTextField();
        txtproductname = new javax.swing.JTextField();
        txtstock = new javax.swing.JTextField();
        cmbstatus = new javax.swing.JComboBox<>();
        cmbType = new javax.swing.JComboBox<>();
        lblphoto = new rojerusan.RSFotoSquare();
        label11 = new java.awt.Label();
        rSMaterialButtonRectangle2 = new rojerusan.RSMaterialButtonRectangle();
        rSMaterialButtonRectangle3 = new rojerusan.RSMaterialButtonRectangle();
        rSMaterialButtonRectangle4 = new rojerusan.RSMaterialButtonRectangle();
        rSMaterialButtonRectangle5 = new rojerusan.RSMaterialButtonRectangle();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBackground(new java.awt.Color(51, 51, 51));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblItem.setBackground(new java.awt.Color(51, 51, 51));
        tblItem.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 0), 1, true));
        tblItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Product Name", "Type", "Stock", "Price", "Status", "Image", "Date"
            }
        ));
        tblItem.setColorBackgoundHead(new java.awt.Color(102, 102, 102));
        tblItem.setColorFilasBackgound1(new java.awt.Color(0, 0, 0));
        tblItem.setColorFilasBackgound2(new java.awt.Color(51, 51, 51));
        tblItem.setColorFilasForeground1(new java.awt.Color(255, 204, 0));
        tblItem.setColorFilasForeground2(new java.awt.Color(255, 204, 0));
        tblItem.setColorForegroundHead(new java.awt.Color(255, 204, 0));
        tblItem.setColorSelBackgound(new java.awt.Color(153, 153, 153));
        tblItem.setColorSelForeground(new java.awt.Color(255, 204, 0));
        tblItem.setFuenteFilas(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblItem.setFuenteFilasSelect(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblItem.setFuenteHead(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tblItem.setRowHeight(15);
        tblItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblItem);

        jPanel11.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 1025, 210));

        jPanel1.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1040, 230));

        jPanel12.setBackground(new java.awt.Color(51, 51, 51));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label5.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        label5.setForeground(new java.awt.Color(255, 204, 0));
        label5.setText("Type:");
        jPanel12.add(label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 40, -1));

        label6.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        label6.setForeground(new java.awt.Color(255, 204, 0));
        label6.setText("Product ID:");
        jPanel12.add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, -1, -1));

        label7.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        label7.setForeground(new java.awt.Color(255, 204, 0));
        label7.setText("Product Name:");
        jPanel12.add(label7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        label9.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        label9.setForeground(new java.awt.Color(255, 204, 0));
        label9.setText("Price(RS):");
        jPanel12.add(label9, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 90, 70, -1));

        label10.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        label10.setForeground(new java.awt.Color(255, 204, 0));
        label10.setText("Status:");
        jPanel12.add(label10, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 130, -1, -1));

        txtprice.setBackground(new java.awt.Color(102, 102, 102));
        txtprice.setForeground(new java.awt.Color(255, 204, 0));
        txtprice.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 0), 1, true));
        jPanel12.add(txtprice, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 90, 220, 30));

        txtproductID.setBackground(new java.awt.Color(102, 102, 102));
        txtproductID.setForeground(new java.awt.Color(255, 204, 0));
        txtproductID.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 0), 1, true));
        jPanel12.add(txtproductID, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 220, 30));

        txtproductname.setBackground(new java.awt.Color(102, 102, 102));
        txtproductname.setForeground(new java.awt.Color(255, 204, 0));
        txtproductname.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 0), 1, true));
        jPanel12.add(txtproductname, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 220, 30));

        txtstock.setBackground(new java.awt.Color(102, 102, 102));
        txtstock.setForeground(new java.awt.Color(255, 204, 0));
        txtstock.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 0), 1, true));
        jPanel12.add(txtstock, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 50, 220, 30));

        cmbstatus.setBackground(new java.awt.Color(102, 102, 102));
        cmbstatus.setForeground(new java.awt.Color(255, 204, 0));
        cmbstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Available", "Unavailable", " " }));
        cmbstatus.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 0), 1, true));
        jPanel12.add(cmbstatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 130, 220, 30));

        cmbType.setBackground(new java.awt.Color(102, 102, 102));
        cmbType.setForeground(new java.awt.Color(255, 204, 0));
        cmbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Meal", "Drink", " " }));
        cmbType.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 0), 1, true));
        cmbType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTypeActionPerformed(evt);
            }
        });
        jPanel12.add(cmbType, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 220, 30));

        lblphoto.setBackground(new java.awt.Color(102, 102, 102));
        lblphoto.setColorBorde(new java.awt.Color(255, 204, 0));
        lblphoto.setColorBotonCargar(new java.awt.Color(51, 51, 51));
        lblphoto.setColorBotonRemover(new java.awt.Color(51, 51, 51));
        lblphoto.setColorTextBotonCargar(new java.awt.Color(204, 153, 0));
        lblphoto.setColorTextBotonRemover(new java.awt.Color(204, 153, 0));
        lblphoto.setImagenDefault(new javax.swing.ImageIcon(getClass().getResource("/Images/bibimbap.png"))); // NOI18N
        jPanel12.add(lblphoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 40, 200, -1));

        label11.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        label11.setForeground(new java.awt.Color(255, 204, 0));
        label11.setText("Stock:");
        jPanel12.add(label11, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 50, 40, -1));

        rSMaterialButtonRectangle2.setBackground(new java.awt.Color(102, 102, 102));
        rSMaterialButtonRectangle2.setForeground(new java.awt.Color(255, 204, 0));
        rSMaterialButtonRectangle2.setText("DELETE");
        rSMaterialButtonRectangle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle2ActionPerformed(evt);
            }
        });
        jPanel12.add(rSMaterialButtonRectangle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 190, 160, 40));

        rSMaterialButtonRectangle3.setBackground(new java.awt.Color(102, 102, 102));
        rSMaterialButtonRectangle3.setForeground(new java.awt.Color(255, 204, 0));
        rSMaterialButtonRectangle3.setText("ADD");
        rSMaterialButtonRectangle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle3ActionPerformed(evt);
            }
        });
        jPanel12.add(rSMaterialButtonRectangle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, 160, 40));

        rSMaterialButtonRectangle4.setBackground(new java.awt.Color(102, 102, 102));
        rSMaterialButtonRectangle4.setForeground(new java.awt.Color(255, 204, 0));
        rSMaterialButtonRectangle4.setText("UPDATE");
        rSMaterialButtonRectangle4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle4ActionPerformed(evt);
            }
        });
        jPanel12.add(rSMaterialButtonRectangle4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 190, 160, 40));

        rSMaterialButtonRectangle5.setBackground(new java.awt.Color(102, 102, 102));
        rSMaterialButtonRectangle5.setForeground(new java.awt.Color(255, 204, 0));
        rSMaterialButtonRectangle5.setText("CLEAR");
        rSMaterialButtonRectangle5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle5ActionPerformed(evt);
            }
        });
        jPanel12.add(rSMaterialButtonRectangle5, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 190, 160, 40));

        jPanel1.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 1040, 250));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1060, 520));

        setBounds(0, 0, 1070, 550);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTypeActionPerformed

    private void rSMaterialButtonRectangle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle2ActionPerformed
        try {
            playnotificationMusic();
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    
    if (result == JOptionPane.YES_OPTION) {
        String sql = "DELETE FROM inventory WHERE product_id = ?";
        Connection con = DBConnection.getConnection();
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, txtproductID.getText());
        pst.executeUpdate();
        playnotificationMusic();
        JOptionPane.showMessageDialog(this, "<html><body><h2>Successfully deleted!</h2>"
                + "<p>The product has been deleted successfully.</p></body></html>", "Deletion Successful", JOptionPane.INFORMATION_MESSAGE);
        
        tableLoard();
    }
} catch (Exception e) {
    JOptionPane.showMessageDialog(this, e.getMessage());
}

    }//GEN-LAST:event_rSMaterialButtonRectangle2ActionPerformed

    private void rSMaterialButtonRectangle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle3ActionPerformed
       getData();
try {
    String productid = txtproductID.getText();
    String productname = txtproductname.getText();
    String type = cmbType.getSelectedItem().toString();
    String stockStr = txtstock.getText();
    String priceStr = txtprice.getText();
    String status = cmbstatus.getSelectedItem().toString();
    String date = LocalDate.now().toString();

    // Validate fields
    if (productid.isEmpty()) {
        playnotificationMusic();
        JOptionPane.showMessageDialog(this, "<html><body><h2>Error!</h2>"
                + "<p>Please enter the product ID.</p></body></html>", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (productname.isEmpty()) {
        playnotificationMusic();
        JOptionPane.showMessageDialog(this, "<html><body><h2>Error!</h2>"
                + "<p>Please enter the product name.</p></body></html>", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (type.isEmpty()) {
        playnotificationMusic();
        JOptionPane.showMessageDialog(this, "<html><body><h2>Error!</h2>"
                + "<p>Please select the product type.</p></body></html>", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (stockStr.isEmpty()) {
        playnotificationMusic();
        JOptionPane.showMessageDialog(this, "<html><body><h2>Error!</h2>"
                + "<p>Please enter the stock quantity.</p></body></html>", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (priceStr.isEmpty()) {
        playnotificationMusic();
        JOptionPane.showMessageDialog(this, "<html><body><h2>Error!</h2>"
                + "<p>Please enter the product price.</p></body></html>", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int stock = Integer.parseInt(stockStr);
    int price = Integer.parseInt(priceStr);

    // Perform the database insertion
    String sql = "INSERT INTO inventory (product_id, product_name, type, stock, price, status, image, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    Connection con = null;
    PreparedStatement pst = null;

    try {
        con = DBConnection.getConnection();
        pst = con.prepareStatement(sql);
        pst.setString(1, productid);
        pst.setString(2, productname);
        pst.setString(3, type);
        pst.setInt(4, stock);
        pst.setInt(5, price);
        pst.setString(6, status);

        path = lblphoto.getRutaImagen();
        InputStream is = new FileInputStream(new File(path));
        pst.setBlob(7, is);

        pst.setString(8, date);

        pst.executeUpdate();
        playnotificationMusic();

        JOptionPane.showMessageDialog(this, "<html><body><h2>Successfully Added!</h2>"
                + "<p>The product has been added successfully.</p></body></html>", "Success", JOptionPane.INFORMATION_MESSAGE);

        tableLoard();

        txtprice.setText("");
        txtproductID.setText("");
        txtproductname.setText("");
        txtstock.setText("");
        String imagePath = "C:\\Users\\USER\\Downloads\\bibimbap.png";
        ImageIcon defaultIcon = new ImageIcon(imagePath);
        lblphoto.setImagenDefault(defaultIcon);
        cmbType.setSelectedItem(null);
        cmbstatus.setSelectedItem(null);
        autoID();

    } catch (SQLException | FileNotFoundException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage());
    } 

} catch (NumberFormatException e) {
    playnotificationMusic();
    JOptionPane.showMessageDialog(this, "<html><body><h2>Error!</h2>"
            + "<p>Please enter valid numeric values for stock and price.</p></body></html>", "Validation Error", JOptionPane.ERROR_MESSAGE);
} catch (Exception e) {
    JOptionPane.showMessageDialog(this, e.getMessage());
}

    }//GEN-LAST:event_rSMaterialButtonRectangle3ActionPerformed

    private void rSMaterialButtonRectangle4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle4ActionPerformed
       try {
           playnotificationMusic();
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this product?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    
    if (result == JOptionPane.YES_OPTION) {
        getData();
        String sql = "UPDATE inventory SET product_name=?, type=?, stock=?, price=? WHERE product_id=?";
        Connection con = DBConnection.getConnection();
        PreparedStatement pst = con.prepareStatement(sql);
        
        pst.setString(1, productname);
        pst.setString(2, type);
        pst.setInt(3, stock);
        pst.setInt(4, price);
        pst.setString(5, txtproductID.getText());
        
        pst.executeUpdate();
        playnotificationMusic();
        JOptionPane.showMessageDialog(this, "<html><body><h2>Successfully Updated!</h2>"
                + "<p>The product has been updated successfully.</p></body></html>", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
        
        tableLoard();
    }
} catch (Exception e) {
    JOptionPane.showMessageDialog(this, e.getMessage());
}

    }//GEN-LAST:event_rSMaterialButtonRectangle4ActionPerformed

    private void rSMaterialButtonRectangle5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle5ActionPerformed
       
                    txtprice.setText("");
                    txtproductID.setText("");
                    txtproductname.setText("");
                    txtstock.setText("");
                    String imagePath = "C:\\Users\\USER\\Downloads\\bibimbap.png";
                    ImageIcon defaultIcon = new ImageIcon(imagePath);
                    lblphoto.setImagenDefault(defaultIcon);
                    cmbType.setSelectedItem(null);
                    cmbstatus.setSelectedItem(null);
        
        
        autoID();
    }//GEN-LAST:event_rSMaterialButtonRectangle5ActionPerformed

    private void tblItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemMouseClicked
         int rowNo = tblItem.getSelectedRow();
            TableModel model = tblItem.getModel();
            
            txtproductID.setText(model.getValueAt(rowNo, 0).toString());
            txtproductname.setText(model.getValueAt(rowNo, 1).toString());
            cmbType.setSelectedItem(model.getValueAt(rowNo, 2).toString());
            txtstock.setText(model.getValueAt(rowNo, 3).toString());
            txtprice.setText(model.getValueAt(rowNo, 4).toString());
             cmbstatus.setSelectedItem(model.getValueAt(rowNo, 5).toString());
              
            
            try{
                
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement("select image from inventory where product_id = ?");
                pst.setString(1,model.getValueAt(rowNo, 0).toString() );
                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    byte[] imageData = rs.getBytes("image");
                    Image image = ImageIO.read(new ByteArrayInputStream(imageData));
                    Image scaledImage = image.getScaledInstance(lblphoto.getWidth(), lblphoto.getHeight(), Image.SCALE_SMOOTH);
                    lblphoto.setImagenDefault(new ImageIcon(scaledImage));
                    
                }
                
               
                
            }catch(Exception ex){
                ex.printStackTrace();
            }
    }//GEN-LAST:event_tblItemMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbType;
    private javax.swing.JComboBox<String> cmbstatus;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.Label label10;
    private java.awt.Label label11;
    private java.awt.Label label5;
    private java.awt.Label label6;
    private java.awt.Label label7;
    private java.awt.Label label9;
    private rojerusan.RSFotoSquare lblphoto;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle2;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle3;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle4;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle5;
    private rojerusan.RSTableMetro tblItem;
    private javax.swing.JTextField txtprice;
    private javax.swing.JTextField txtproductID;
    private javax.swing.JTextField txtproductname;
    private javax.swing.JTextField txtstock;
    // End of variables declaration//GEN-END:variables
}
