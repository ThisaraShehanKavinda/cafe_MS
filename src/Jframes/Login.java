/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Jframes;

import Database.DBConnection;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.Image;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.sql.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


/**
 *
 * @author USER
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    PreparedStatement pst=null;
    ResultSet rs=null;
    Connection con;
    
     
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
    
    public Login() {
        initComponents();
        lblunhide.setVisible(false);
    }
    
     public boolean validateLogin() {
    String name = txtUsername.getText();
    String pw = txtPassword.getText();

    if (name.equals("")) {
        playnotificationMusic();
        JOptionPane.showMessageDialog(this, "<html><body><h2>Error!</h2>"
                + "<p>Please enter your username.</p></body></html>", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
    if (pw.equals("")) {
        playnotificationMusic();
        JOptionPane.showMessageDialog(this, "<html><body><h2>Error!</h2>"
                + "<p>Please enter your password.</p></body></html>", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
    return true;
}

    
    
    private void itemLoard() {
    try {
        con = DBConnection.getConnection();
        String sql = "SELECT  image FROM signup WHERE username = ?";
         pst = con.prepareStatement(sql);
        pst.setString(1, txtUsername.getText());
         rs = pst.executeQuery();

        if (rs.next()) {
            

            byte[] imageData = rs.getBytes("image");
            ImageIcon imageIcon = new ImageIcon(imageData);
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(lblPhoto.getWidth(), lblPhoto.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
            lblPhoto.setImagenDefault(scaledImageIcon);
        } else {
            // Handle case when no item is found
            
            String imagePath = "C:\\Users\\USER\\Documents\\NetBeansProjects\\Cafe Management System\\src\\Images\\icons8-contacts-125 (1).png";
                    ImageIcon defaultIcon = new ImageIcon(imagePath);
                    lblPhoto.setImagenDefault(defaultIcon);
        }

        // Close the resources
        rs.close();
        pst.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e);
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

        rSLabelImage1 = new rojerusan.RSLabelImage();
        panel1 = new java.awt.Panel();
        panel2 = new java.awt.Panel();
        label1 = new java.awt.Label();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        rSMaterialButtonRectangle2 = new rojerusan.RSMaterialButtonRectangle();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txtUsername = new rojerusan.RSMetroTextPlaceHolder();
        lblPhoto = new rojerusan.RSFotoCircle();
        txtPassword = new rojerusan.RSPasswordTextPlaceHolder();
        label2 = new java.awt.Label();
        rSMaterialButtonRectangle3 = new rojerusan.RSMaterialButtonRectangle();
        lblunhide = new javax.swing.JLabel();
        lblHide = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panel1.setBackground(new java.awt.Color(0, 0, 0));
        panel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel2.setBackground(new java.awt.Color(51, 51, 51));
        panel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label1.setFont(new java.awt.Font("Lucida Fax", 1, 18)); // NOI18N
        label1.setForeground(new java.awt.Color(255, 206, 1));
        label1.setText("WELCOME BACK");
        panel2.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, -1, -1));

        jLabel1.setForeground(new java.awt.Color(255, 204, 0));
        jLabel1.setText("Please signup here");
        panel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, -1, -1));

        jLabel2.setForeground(new java.awt.Color(255, 204, 0));
        jLabel2.setText("If you haven't an account");
        panel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 240, -1, -1));

        rSMaterialButtonRectangle2.setBackground(new java.awt.Color(102, 102, 102));
        rSMaterialButtonRectangle2.setForeground(new java.awt.Color(255, 204, 0));
        rSMaterialButtonRectangle2.setText("SIGNUP");
        rSMaterialButtonRectangle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle2ActionPerformed(evt);
            }
        });
        panel2.add(rSMaterialButtonRectangle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 170, 40));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-waiter-100.png"))); // NOI18N
        panel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 110, 120));

        panel1.add(panel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 380));

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtUsername.setBackground(new java.awt.Color(102, 102, 102));
        txtUsername.setForeground(new java.awt.Color(255, 204, 0));
        txtUsername.setBorderColor(new java.awt.Color(255, 204, 0));
        txtUsername.setBotonColor(new java.awt.Color(255, 204, 0));
        txtUsername.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        txtUsername.setPhColor(new java.awt.Color(255, 204, 0));
        txtUsername.setPlaceholder("User Name");
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUsernameKeyReleased(evt);
            }
        });
        jPanel1.add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 340, 40));

        lblPhoto.setBackground(new java.awt.Color(102, 102, 102));
        lblPhoto.setColorBorde(new java.awt.Color(102, 102, 102));
        lblPhoto.setColorBordePopu(new java.awt.Color(255, 204, 0));
        lblPhoto.setColorButtonOptions(new java.awt.Color(255, 204, 0));
        lblPhoto.setColorButtonOptionsHover(new java.awt.Color(255, 204, 0));
        lblPhoto.setColorFondo(new java.awt.Color(255, 204, 0));
        lblPhoto.setColorTextButtonsPopu(new java.awt.Color(102, 102, 102));
        lblPhoto.setImagenDefault(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-contacts-125 (1).png"))); // NOI18N
        jPanel1.add(lblPhoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 110, 120));

        txtPassword.setBackground(new java.awt.Color(102, 102, 102));
        txtPassword.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 204, 0)));
        txtPassword.setForeground(new java.awt.Color(255, 204, 0));
        txtPassword.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        txtPassword.setPhColor(new java.awt.Color(255, 204, 0));
        txtPassword.setPlaceholder("Password");
        jPanel1.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 340, 40));

        label2.setFont(new java.awt.Font("Lucida Fax", 1, 18)); // NOI18N
        label2.setForeground(new java.awt.Color(255, 206, 1));
        label2.setText("LOGIN");
        jPanel1.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, -1, -1));

        rSMaterialButtonRectangle3.setBackground(new java.awt.Color(102, 102, 102));
        rSMaterialButtonRectangle3.setForeground(new java.awt.Color(255, 204, 0));
        rSMaterialButtonRectangle3.setText("LOGIN");
        rSMaterialButtonRectangle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle3ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonRectangle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 320, 170, 40));

        lblunhide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-padlock-19.png"))); // NOI18N
        lblunhide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblunhideMouseClicked(evt);
            }
        });
        jPanel1.add(lblunhide, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 280, 20, 20));

        lblHide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-lock-19.png"))); // NOI18N
        lblHide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHideMouseClicked(evt);
            }
        });
        jPanel1.add(lblHide, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 280, 20, 20));

        panel1.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, 600, 380));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 930, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, 930, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 420, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void rSMaterialButtonRectangle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle2ActionPerformed
        signUP signup = new signUP();
        signup.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_rSMaterialButtonRectangle2ActionPerformed

    private void rSMaterialButtonRectangle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle3ActionPerformed
        if (validateLogin()) {
    String username = txtUsername.getText();
    String pword = new String(txtPassword.getPassword());
    String password;
    
    try {
        String sql = "SELECT `password` FROM `signup` WHERE username = ?";
        con = DBConnection.getConnection();
        pst = con.prepareStatement(sql);
        pst.setString(1, txtUsername.getText());
        rs = pst.executeQuery();

        if (rs.next()) {
            password = (rs.getString("password"));

            if (password.equals(pword)) {
                playnotificationMusic();
                JOptionPane.showMessageDialog(this, "<html><body><h2>Login Successful!</h2>"
                        + "<p>Welcome, " + username + "!</p></body></html>", "Login Success", JOptionPane.INFORMATION_MESSAGE);
                Home home = new Home();
                home.setVisible(true);
                this.dispose();
            } else {
                playnotificationMusic();
                JOptionPane.showMessageDialog(this, "<html><body><h2>Password Incorrect!</h2>"
                        + "<p>Please check your password and try again.</p></body></html>", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            playnotificationMusic();
            JOptionPane.showMessageDialog(this, "<html><body><h2>User not found!</h2>"
                    + "<p>The username you entered does not exist.</p></body></html>", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        playnotificationMusic();
        JOptionPane.showMessageDialog(this, "<html><body><h2>Error!</h2>"
                + "<p>An error occurred while logging in:</p>"
                + "<p>" + e.getMessage() + "</p></body></html>", "Login Error", JOptionPane.ERROR_MESSAGE);
    }
} else {
            playnotificationMusic();
    JOptionPane.showMessageDialog(this, "<html><body><h2>Login Failed!</h2>"
            + "<p>Please enter both username and password.</p></body></html>", "Login Failed", JOptionPane.WARNING_MESSAGE);
}

    }//GEN-LAST:event_rSMaterialButtonRectangle3ActionPerformed

    private void txtUsernameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsernameKeyReleased
      itemLoard();
    }//GEN-LAST:event_txtUsernameKeyReleased

    private void lblunhideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblunhideMouseClicked
      txtPassword.setEchoChar('*');
       lblunhide.setVisible(false);
       lblHide.setVisible(true);
    }//GEN-LAST:event_lblunhideMouseClicked

    private void lblHideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHideMouseClicked
       txtPassword.setEchoChar((char)0);
        lblHide.setVisible(false);
        lblunhide.setVisible(true);
    }//GEN-LAST:event_lblHideMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
          try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private java.awt.Label label1;
    private java.awt.Label label2;
    private javax.swing.JLabel lblHide;
    private rojerusan.RSFotoCircle lblPhoto;
    private javax.swing.JLabel lblunhide;
    private java.awt.Panel panel1;
    private java.awt.Panel panel2;
    private rojerusan.RSLabelImage rSLabelImage1;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle2;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle3;
    private rojerusan.RSPasswordTextPlaceHolder txtPassword;
    private rojerusan.RSMetroTextPlaceHolder txtUsername;
    // End of variables declaration//GEN-END:variables
}
