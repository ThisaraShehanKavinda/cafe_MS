/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Jframes;

import Database.DBConnection;
import Database.GalleryView;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class Menu extends javax.swing.JInternalFrame {

    /**
     * Creates new form DashBoard
     */
    
    
    
    Connection con;
PreparedStatement pst;
ResultSet rs;
public Menu() throws SQLException {
    initComponents();
        
    this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        
    BasicInternalFrameUI bi = (BasicInternalFrameUI)this.getUI();
    bi.setNorthPane(null);
    populateProductNameComboBox(cmbproductname);
        
    con = DBConnection.getConnection();
    String query = "SELECT product_name, stock, price, image FROM inventory";
    pst = con.prepareStatement(query);
    ResultSet resultSet = pst.executeQuery();

    GalleryView galleryView = new GalleryView(resultSet);
    galleryView.displayProducts();

 jScrollPane1.setViewportView(galleryView.getScrollPane());
 
 
 lblTotal.setText("00.00");
 lblcharge.setText("00.00");
 
 
 int maxQuantity = getMaxQuantity();
SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, maxQuantity, 1);
cmbqty.setModel(spinnerModel);

btnPay.setEnabled(false);

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


private int getMaxQuantity() {
    int maxQuantity = 0;

    try {
        // Establish a connection to the database
         con = DBConnection.getConnection();

        // Get the selected product name from the combo box
        String selectedProduct = cmbproductname.getSelectedItem().toString();

        // Create a SQL statement to retrieve the stock quantity for the selected product
        String sql = "SELECT stock FROM inventory WHERE product_name = ?";
         pst = con.prepareStatement(sql);
        pst.setString(1, selectedProduct);
         rs = pst.executeQuery();

        // Retrieve the stock quantity from the result set
        if (rs.next()) {
            maxQuantity = rs.getInt("stock");
        }

        // Close the database resources
        rs.close();
        pst.close();
        
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return maxQuantity;
}



// ...

private void updateInventory() {
    DefaultTableModel tableModel = (DefaultTableModel) tbladd.getModel();

    try {
         con = DBConnection.getConnection();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String productName = (String) tableModel.getValueAt(i, 0);
            int quantity = (int) tableModel.getValueAt(i, 1);

            // Retrieve the product ID from the "inventory" table based on the product name
            String getProductIDQuery = "SELECT product_id FROM inventory WHERE product_name = ?";
            PreparedStatement getProductIDStatement = con.prepareStatement(getProductIDQuery);
            getProductIDStatement.setString(1, productName);
            ResultSet resultSet = getProductIDStatement.executeQuery();

            if (resultSet.next()) {
                String productID = resultSet.getString("product_id");

                // Update the "inventory" table by subtracting the quantity from the stock column
                String updateInventoryQuery = "UPDATE inventory SET stock = stock - ? WHERE product_id = ?";
                PreparedStatement updateInventoryStatement = con.prepareStatement(updateInventoryQuery);
                updateInventoryStatement.setInt(1, quantity);
                updateInventoryStatement.setString(2, productID);
                updateInventoryStatement.executeUpdate();
                updateInventoryStatement.close();
                
                
            }

            getProductIDStatement.close();
            resultSet.close();
        }

       
    } catch (SQLException e) {
        e.printStackTrace();
    }
}




private void enablePayButton() {
    String chargeText = lblcharge.getText();
    double chargeValue = Double.parseDouble(chargeText);

    if (chargeValue >= 0) {
        btnPay.setEnabled(true);
    } else {
        btnPay.setEnabled(false);
    }
}



private void printReceipt(String receipt) {
    // Create a new Printable object for the receipt
    Printable printable = new Printable() {
        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex != 0) {
                return NO_SUCH_PAGE;
            }
            
            // Create a Graphics2D object from the provided Graphics
            Graphics2D g2d = (Graphics2D) graphics;
            
            // Set the default font and size for the receipt
            Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
            g2d.setFont(font);
            
            // Set the margin and line spacing for the receipt
            int margin = 20;
            int lineHeight = g2d.getFontMetrics().getHeight();
            
            // Split the receipt into lines
            String[] lines = receipt.split("\n");
            
            // Calculate the starting y-coordinate for printing
            int y = margin;
            
            // Print each line of the receipt
            for (String line : lines) {
                g2d.drawString(line, margin, y);
                y += lineHeight;
            }
            
            // Signal that the page has been printed successfully
            return PAGE_EXISTS;
        }
    };
    
    // Get the default printer
    PrinterJob printerJob = PrinterJob.getPrinterJob();
    
    // Set the printable object to be printed
    printerJob.setPrintable(printable);
    
    // Show the print dialog to the user
    if (printerJob.printDialog()) {
        try {
            // Start the printing process
            printerJob.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }
}










private void insertSellData() {
    DefaultTableModel tableModel = (DefaultTableModel) tbladd.getModel();
int rowCount = tableModel.getRowCount();
double totalAmount = Double.parseDouble(lblTotal.getText());
LocalDate currentDate = LocalDate.now();

int totalProducts = 0;

// Calculate the total number of products sold
for (int i = 0; i < rowCount; i++) {
    int quantity = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
    totalProducts += quantity;
}

try {
    playnotificationMusic();
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to proceed with the payment?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

    if (result == JOptionPane.YES_OPTION) {
        con = DBConnection.getConnection();
        String query = "INSERT INTO sell (no_of_products, total, date) VALUES (?, ?, ?)";
        pst = con.prepareStatement(query);

        pst.setInt(1, totalProducts);
        pst.setDouble(2, totalAmount);
        pst.setDate(3, Date.valueOf(currentDate));

        pst.executeUpdate();
playnotificationMusic();
        JOptionPane.showMessageDialog(this, "<html><body><h2>Successfully Paid!</h2>"
                + "<p>The payment has been processed successfully.</p></body></html>", "Payment Successful", JOptionPane.INFORMATION_MESSAGE);

        pst.close();
    }
} catch (SQLException e) {
    e.printStackTrace();
}

}








private void addDetailsToTable() {
    String price = txtPrice.getText();
    String productName = (String) cmbproductname.getSelectedItem();
    int quantity = (int) cmbqty.getValue();

    // Create an array with the details
    Object[] row = {productName, quantity,price};

    // Get the table model
    DefaultTableModel tableModel = (DefaultTableModel) tbladd.getModel();

    // Add the row to the table model
    tableModel.addRow(row);

    // Clear the input fields
    txtPrice.setText("");
    cmbproductname.setSelectedIndex(0);
    cmbqty.setValue(0);
}

private void clearSelectedRow() {
    DefaultTableModel tableModel = (DefaultTableModel) tbladd.getModel();
    int selectedRow = tbladd.getSelectedRow();

    if (selectedRow != -1) {
        tableModel.removeRow(selectedRow);
        calculateTotal(); // Recalculate the total after removing the row
    }
}

private void calculateTotal() {
    DefaultTableModel tableModel = (DefaultTableModel) tbladd.getModel();
    int rowCount = tableModel.getRowCount();
    int total = 0;

    for (int i = 0; i < rowCount; i++) {
        String priceStr = tableModel.getValueAt(i, 1).toString();
        double price = Double.parseDouble(priceStr);
        int quantity = Integer.parseInt(tableModel.getValueAt(i, 2).toString());

        total += price * quantity;
    }

    lblTotal.setText(""+total);
    
  
}



    
    
    
   public void displayProductPrice(JComboBox<String> comboBox, JTextField textField) throws SQLException {
    String selectedProductName = comboBox.getSelectedItem().toString();
    String sql = "SELECT price FROM inventory WHERE product_name = ?";
    
    con = DBConnection.getConnection(); 
    pst= con.prepareStatement(sql) ;
        
        pst.setString(1, selectedProductName);
        
         rs = pst.executeQuery();
            if (rs.next()) {
                int price = rs.getInt("price");
                textField.setText(String.valueOf(price));
            } else {
                textField.setText("");
                playnotificationMusic();
                JOptionPane.showMessageDialog(null, "Price not found for the selected product.");
            }
        
        
    //}catch (SQLException ex) {
        //JOptionPane.showMessageDialog(null, "Error retrieving product price: " + ex.getMessage());
    //}
}



public void populateProductNameComboBox(JComboBox<String> comboBox) throws SQLException {
    comboBox.removeAllItems();
    String sql = "SELECT product_name FROM inventory";

         con = DBConnection.getConnection();
         pst = con.prepareStatement(sql);
         rs = pst.executeQuery(); 

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        while (rs.next()) {
            String productName = rs.getString("product_name");
            model.addElement(productName);
        }

        comboBox.setModel(model);

    // catch (SQLException ex) {
      //  JOptionPane.showMessageDialog(null, "Error retrieving product names: " + ex.getMessage());
    //}
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
        jScrollPane2 = new javax.swing.JScrollPane();
        tbladd = new rojerusan.RSTableMetro();
        rSMaterialButtonRectangle6 = new rojerusan.RSMaterialButtonRectangle();
        rSMaterialButtonRectangle8 = new rojerusan.RSMaterialButtonRectangle();
        jPanel12 = new javax.swing.JPanel();
        cmbproductname = new javax.swing.JComboBox<>();
        label13 = new java.awt.Label();
        label22 = new java.awt.Label();
        cmbqty = new javax.swing.JSpinner();
        label23 = new java.awt.Label();
        label24 = new java.awt.Label();
        txtPrice = new javax.swing.JTextField();
        rSMaterialButtonRectangle9 = new rojerusan.RSMaterialButtonRectangle();
        panel1 = new java.awt.Panel();
        label17 = new java.awt.Label();
        label16 = new java.awt.Label();
        lblTotal = new java.awt.Label();
        label15 = new java.awt.Label();
        txtAmount = new javax.swing.JTextField();
        label19 = new java.awt.Label();
        label20 = new java.awt.Label();
        lblcharge = new java.awt.Label();
        btnPay = new rojerusan.RSMaterialButtonRectangle();
        jScrollPane1 = new javax.swing.JScrollPane();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBackground(new java.awt.Color(51, 51, 51));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbladd.setBackground(new java.awt.Color(51, 51, 51));
        tbladd.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 0), 1, true));
        tbladd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Name", "Quantity", "Price(RS)"
            }
        ));
        tbladd.setColorBackgoundHead(new java.awt.Color(102, 102, 102));
        tbladd.setColorFilasBackgound1(new java.awt.Color(0, 0, 0));
        tbladd.setColorFilasBackgound2(new java.awt.Color(51, 51, 51));
        tbladd.setColorFilasForeground1(new java.awt.Color(255, 204, 0));
        tbladd.setColorFilasForeground2(new java.awt.Color(255, 204, 0));
        tbladd.setColorForegroundHead(new java.awt.Color(255, 204, 0));
        tbladd.setColorSelBackgound(new java.awt.Color(102, 102, 102));
        tbladd.setColorSelForeground(new java.awt.Color(255, 204, 0));
        tbladd.setFuenteFilas(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tbladd.setFuenteFilasSelect(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tbladd.setFuenteHead(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tbladd.setRowHeight(15);
        tbladd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbladdMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbladd);

        jPanel11.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 280, 400));

        rSMaterialButtonRectangle6.setBackground(new java.awt.Color(102, 102, 102));
        rSMaterialButtonRectangle6.setForeground(new java.awt.Color(255, 204, 0));
        rSMaterialButtonRectangle6.setText("Receipt");
        rSMaterialButtonRectangle6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle6ActionPerformed(evt);
            }
        });
        jPanel11.add(rSMaterialButtonRectangle6, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 440, 130, 40));

        rSMaterialButtonRectangle8.setBackground(new java.awt.Color(102, 102, 102));
        rSMaterialButtonRectangle8.setForeground(new java.awt.Color(255, 204, 0));
        rSMaterialButtonRectangle8.setText("Clear");
        rSMaterialButtonRectangle8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle8ActionPerformed(evt);
            }
        });
        jPanel11.add(rSMaterialButtonRectangle8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 130, 40));

        jPanel1.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 10, 300, 500));

        jPanel12.setBackground(new java.awt.Color(51, 51, 51));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmbproductname.setBackground(new java.awt.Color(102, 102, 102));
        cmbproductname.setForeground(new java.awt.Color(255, 204, 0));
        cmbproductname.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        cmbproductname.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 0), 1, true));
        cmbproductname.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbproductnameItemStateChanged(evt);
            }
        });
        cmbproductname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbproductnameFocusLost(evt);
            }
        });
        cmbproductname.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cmbproductnameMouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cmbproductnameMouseReleased(evt);
            }
        });
        cmbproductname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbproductnameActionPerformed(evt);
            }
        });
        cmbproductname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmbproductnameKeyReleased(evt);
            }
        });
        jPanel12.add(cmbproductname, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 220, 30));

        label13.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        label13.setForeground(new java.awt.Color(255, 204, 0));
        label13.setText("Total:");
        jPanel12.add(label13, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, -1, -1));

        label22.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        label22.setForeground(new java.awt.Color(255, 204, 0));
        label22.setText("Quantity :");
        jPanel12.add(label22, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, 70, 20));

        cmbqty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbqtyFocusGained(evt);
            }
        });
        cmbqty.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbqtyMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cmbqtyMouseEntered(evt);
            }
        });
        cmbqty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbqtyKeyPressed(evt);
            }
        });
        jPanel12.add(cmbqty, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 110, 30));

        label23.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        label23.setForeground(new java.awt.Color(255, 204, 0));
        label23.setText("Price :");
        jPanel12.add(label23, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 40, 30));

        label24.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        label24.setForeground(new java.awt.Color(255, 204, 0));
        label24.setText("Product Name :");
        jPanel12.add(label24, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 100, 30));

        txtPrice.setBackground(new java.awt.Color(102, 102, 102));
        txtPrice.setForeground(new java.awt.Color(255, 204, 0));
        txtPrice.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 0), 1, true));
        txtPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPriceActionPerformed(evt);
            }
        });
        jPanel12.add(txtPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 220, 30));

        rSMaterialButtonRectangle9.setBackground(new java.awt.Color(102, 102, 102));
        rSMaterialButtonRectangle9.setForeground(new java.awt.Color(255, 204, 0));
        rSMaterialButtonRectangle9.setText("ADD");
        rSMaterialButtonRectangle9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle9ActionPerformed(evt);
            }
        });
        jPanel12.add(rSMaterialButtonRectangle9, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 160, 40));

        jPanel1.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 380, 180));

        panel1.setBackground(new java.awt.Color(51, 51, 51));
        panel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label17.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        label17.setForeground(new java.awt.Color(255, 204, 0));
        label17.setText(" Total :");
        panel1.add(label17, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, -1, 30));

        label16.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        label16.setForeground(new java.awt.Color(255, 204, 0));
        label16.setText("RS");
        panel1.add(label16, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 20, 30));

        lblTotal.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(255, 204, 0));
        lblTotal.setText("10000");
        panel1.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 180, 30));

        label15.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        label15.setForeground(new java.awt.Color(255, 204, 0));
        label15.setText("Amount :");
        panel1.add(label15, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 70, 30));

        txtAmount.setBackground(new java.awt.Color(102, 102, 102));
        txtAmount.setForeground(new java.awt.Color(255, 204, 0));
        txtAmount.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 0), 1, true));
        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAmountKeyReleased(evt);
            }
        });
        panel1.add(txtAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 160, 30));

        label19.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        label19.setForeground(new java.awt.Color(255, 204, 0));
        label19.setText("Charge :");
        panel1.add(label19, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        label20.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        label20.setForeground(new java.awt.Color(255, 204, 0));
        label20.setText("RS");
        panel1.add(label20, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, -1, -1));

        lblcharge.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        lblcharge.setForeground(new java.awt.Color(255, 204, 0));
        lblcharge.setText("10000");
        panel1.add(lblcharge, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, 170, -1));

        btnPay.setBackground(new java.awt.Color(102, 102, 102));
        btnPay.setForeground(new java.awt.Color(255, 204, 0));
        btnPay.setText("pay");
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });
        panel1.add(btnPay, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 160, 40));

        jPanel1.add(panel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 330, 350, 180));
        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 720, 310));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1060, 520));

        setBounds(0, 0, 1070, 550);
    }// </editor-fold>//GEN-END:initComponents

    private void rSMaterialButtonRectangle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rSMaterialButtonRectangle3ActionPerformed

    private void rSMaterialButtonRectangle4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rSMaterialButtonRectangle4ActionPerformed

    private void rSMaterialButtonRectangle5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rSMaterialButtonRectangle5ActionPerformed

    private void cmbproductnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbproductnameActionPerformed
        Object selectedProduct = cmbproductname.getSelectedItem();
        if (selectedProduct != null) {
            try {
                displayProductPrice(cmbproductname, txtPrice);
                int maxQuantity = getMaxQuantity();
SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, maxQuantity, 1);
cmbqty.setModel(spinnerModel);
            } catch (SQLException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        

    }//GEN-LAST:event_cmbproductnameActionPerformed

    private void rSMaterialButtonRectangle6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle6ActionPerformed
        StringBuilder receipt = new StringBuilder();

// Get the table data from tblAdd
DefaultTableModel tableModel = (DefaultTableModel) tbladd.getModel();
int rowCount = tableModel.getRowCount();
receipt.append(String.format("%-15s %-10s %-10s\n", "Product", "Price", "Quantity"));
for (int i = 0; i < rowCount; i++) {
    String product = tableModel.getValueAt(i, 0).toString();
    String quantity = tableModel.getValueAt(i, 1).toString();
    String total = tableModel.getValueAt(i, 2).toString();
    receipt.append(String.format("%-15s %-10s %-10s\n", product, quantity, total));
}

// Get the payment information
String totalAmount = lblTotal.getText();
String paidAmount = txtAmount.getText();
String balance = lblcharge.getText();

// Append the payment information to the receipt
receipt.append("\n");
receipt.append(String.format("%-10s %10s\n", "Total:", totalAmount));
receipt.append(String.format("%-10s %10s\n", "Paid:", paidAmount));
receipt.append(String.format("%-10s %10s\n", "Balance:", balance));

// Print the receipt
printReceipt(receipt.toString());

    
    }//GEN-LAST:event_rSMaterialButtonRectangle6ActionPerformed

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed
        insertSellData();
        updateInventory();
        
        
        
    con = DBConnection.getConnection();
    String query = "SELECT product_name, stock, price, image FROM inventory";
        try {
            pst = con.prepareStatement(query);
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    ResultSet resultSet = null;
        try {
            resultSet = pst.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }

    GalleryView galleryView = new GalleryView(resultSet);
    galleryView.displayProducts();

 jScrollPane1.setViewportView(galleryView.getScrollPane());
    }//GEN-LAST:event_btnPayActionPerformed

    private void rSMaterialButtonRectangle8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle8ActionPerformed
        playnotificationMusic();
       int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear all fields and the table?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

if (result == JOptionPane.YES_OPTION) {
    txtAmount.setText("");
    lblTotal.setText("00.00");
    lblcharge.setText("00.00");
    DefaultTableModel tableModel1 = (DefaultTableModel) tbladd.getModel();
    tableModel1.setRowCount(0); // Clear all rows in the table
    lblTotal.setText("0.0"); // Reset the total label
    playnotificationMusic();
    JOptionPane.showMessageDialog(this, "<html><body><h2>Fields and table cleared!</h2>"
            + "<p>All fields and the table have been cleared successfully.</p></body></html>", "Clear Successful", JOptionPane.INFORMATION_MESSAGE);
}

    }//GEN-LAST:event_rSMaterialButtonRectangle8ActionPerformed

    private void rSMaterialButtonRectangle9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle9ActionPerformed
        addDetailsToTable();
        calculateTotal();
        
        txtPrice.setText("");
        cmbproductname.setSelectedItem(null);
    }//GEN-LAST:event_rSMaterialButtonRectangle9ActionPerformed

    private void cmbproductnameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbproductnameKeyReleased
       
    }//GEN-LAST:event_cmbproductnameKeyReleased

    private void cmbproductnameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbproductnameItemStateChanged
       
    }//GEN-LAST:event_cmbproductnameItemStateChanged

    private void txtPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPriceActionPerformed

    private void txtAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyReleased
      try {
        double amount = Integer.parseInt(txtAmount.getText());
        double tprice = Integer.parseInt(lblTotal.getText());
        double payable = amount-tprice;
        lblcharge.setText(String.valueOf(payable));
          enablePayButton();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, e);
    }  
    }//GEN-LAST:event_txtAmountKeyReleased

    private void tbladdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbladdMouseClicked
         clearSelectedRow();        
    }//GEN-LAST:event_tbladdMouseClicked

    private void cmbqtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbqtyKeyPressed
    

    }//GEN-LAST:event_cmbqtyKeyPressed

    private void cmbqtyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbqtyMouseClicked
 

    }//GEN-LAST:event_cmbqtyMouseClicked

    private void cmbproductnameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbproductnameFocusLost


    }//GEN-LAST:event_cmbproductnameFocusLost

    private void cmbproductnameMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbproductnameMouseReleased
       

    }//GEN-LAST:event_cmbproductnameMouseReleased

    private void cmbqtyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbqtyFocusGained
       

    }//GEN-LAST:event_cmbqtyFocusGained

    private void cmbqtyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbqtyMouseEntered


    }//GEN-LAST:event_cmbqtyMouseEntered

    private void cmbproductnameMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbproductnameMouseEntered


    }//GEN-LAST:event_cmbproductnameMouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSMaterialButtonRectangle btnPay;
    private javax.swing.JComboBox<String> cmbproductname;
    private javax.swing.JSpinner cmbqty;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private java.awt.Label label13;
    private java.awt.Label label15;
    private java.awt.Label label16;
    private java.awt.Label label17;
    private java.awt.Label label19;
    private java.awt.Label label20;
    private java.awt.Label label22;
    private java.awt.Label label23;
    private java.awt.Label label24;
    private java.awt.Label lblTotal;
    private java.awt.Label lblcharge;
    private java.awt.Panel panel1;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle6;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle8;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle9;
    private rojerusan.RSTableMetro tbladd;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtPrice;
    // End of variables declaration//GEN-END:variables
}
