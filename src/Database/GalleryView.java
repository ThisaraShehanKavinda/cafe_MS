/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.sql.*;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

/**
 *
 * @author USER
 */
public class GalleryView {
     private JScrollPane scrollPane;
    private JPanel galleryPanel;
    private ResultSet resultSet;

    public GalleryView(ResultSet resultSet) {
        this.resultSet = resultSet;
        galleryPanel = new JPanel(new GridLayout(0, 4, 10, 10)); // Adjust the parameters as needed
        scrollPane = new JScrollPane(galleryPanel);
    }

   public void displayProducts() {
    try {
        while (resultSet.next()) {
            String productName = resultSet.getString("product_name");
            int stock = resultSet.getInt("stock");
            double price = resultSet.getDouble("price");
            byte[] imageBytes = resultSet.getBytes("image");

            // Create a product panel
            JPanel productPanel = new JPanel();
            productPanel.setPreferredSize(new Dimension(110, 160)); // Set the size of the card
            productPanel.setLayout(new BorderLayout());
            productPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Add a border to simulate a card-like appearance
            productPanel.setBackground(Color.DARK_GRAY); // Set the background color to dark gray

            // Convert the byte array to an Image
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            Image image = ImageIO.read(bis);

            // Resize the image to the desired dimensions
            Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);

            // Create a label to display the resized image
            JLabel imageLabel = new JLabel(new ImageIcon(resizedImage));
            productPanel.add(imageLabel, BorderLayout.CENTER);

            // Create a panel for the product information
            JPanel infoPanel = new JPanel(new GridLayout(3, 1));
            infoPanel.setBackground(Color.DARK_GRAY); // Set the background color to dark gray

            // Create a label for the product name
            JLabel nameLabel = new JLabel(productName);
            nameLabel.setForeground(Color.YELLOW);
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER); // Align the text to the center
            infoPanel.add(nameLabel);

            // Create a label for the stock information
            JLabel stockLabel = new JLabel("Stock: " + stock);
            stockLabel.setForeground(Color.YELLOW);
            stockLabel.setHorizontalAlignment(SwingConstants.CENTER); // Align the text to the center
            infoPanel.add(stockLabel);

            // Create a label for the price information
            JLabel priceLabel = new JLabel("Price: RS " + price);
            priceLabel.setForeground(Color.YELLOW);
            priceLabel.setHorizontalAlignment(SwingConstants.CENTER); // Align the text to the center
            infoPanel.add(priceLabel);

            // Add the infoPanel to the productPanel at the bottom (south) position
            productPanel.add(infoPanel, BorderLayout.SOUTH);

            // Add the product panel to the galleryPanel
            galleryPanel.add(productPanel);
        }
    } catch (SQLException | IOException e) {
        e.printStackTrace();
    }

    // Revalidate the galleryPanel to update the layout
    galleryPanel.revalidate();
    galleryPanel.repaint();
}







    public JScrollPane getScrollPane() {
        return scrollPane;
    }
}
