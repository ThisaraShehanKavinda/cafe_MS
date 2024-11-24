/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Jframes;

import Database.DBConnection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.Timer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


/**
 *
 * @author USER
 */
public class DashBoard extends javax.swing.JInternalFrame {

    /**
     * Creates new form DashBoard
     */
    public DashBoard() {
        initComponents();
        
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        
        BasicInternalFrameUI bi = (BasicInternalFrameUI)this.getUI();
        bi.setNorthPane(null);
        updateNoOfCustomersLabel();
        updateTodayIncomeLabel();
        updateTotalIncomeLabel();
        updateTotalProductsLabel();
        updateSoldProductProgressBar();
        updateRemainProductProgressBar();
        displayCustomerBarGraph();
        displayIncomeLineGraph();
        updateTotalPercentage();
    }
    
    
    
   private void updateTotalPercentage() {
    try {
        // Establish a connection to the database
        Connection connection = DBConnection.getConnection();

        // Create a SQL statement to calculate the total number of products for each date
        String sql = "SELECT date, SUM(stock) AS total_products FROM inventory GROUP BY date";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        // Calculate the total number of products for all dates
        int totalProductsAllDays = 0;
        while (resultSet.next()) {
            int totalProducts = resultSet.getInt("total_products");
            totalProductsAllDays += totalProducts;
        }

        // Close the database resources
        resultSet.close();
        

        // Get the total number of products for the current date
        String currentDate = LocalDate.now().toString();
        int totalProductsCurrentDate = 0;
        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT SUM(stock) AS total_products FROM inventory WHERE date = '" + currentDate + "'");
        if (resultSet.next()) {
            totalProductsCurrentDate = resultSet.getInt("total_products");
        }

        // Calculate the percentage
        double percentage = (double) totalProductsCurrentDate / totalProductsAllDays * 100;

        // Create a Timer to animate the progress bar
        int animationDelay = 50; // Delay between each animation step in milliseconds
        int animationStep = 1; // Value increment for each animation step
        int finalProgressValue = (int) Math.round(percentage);

        Timer timer = new Timer(animationDelay, new ActionListener() {
            private int currentProgressValue = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                currentProgressValue += animationStep;
                if (currentProgressValue > finalProgressValue) {
                    currentProgressValue = finalProgressValue;
                    ((Timer) e.getSource()).stop();
                }

                // Update the progress bar value
                progressbartotal.setValue(currentProgressValue);
                // Update the lblTotal label
                lbltotal.setText(String.format("%.2f%%", (double) currentProgressValue));

            }
        });

        // Start the Timer
        timer.start();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}



    
    
    
    
    
    private void displayIncomeLineGraph() {
    // Create a dataset for the line graph
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    try {
        // Establish a connection to the database
        Connection connection = DBConnection.getConnection();

        // Create a SQL statement to calculate the total income for each day
        String sql = "SELECT date, SUM(total) AS total_income FROM sell GROUP BY date";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        // Add data to the dataset
        while (resultSet.next()) {
            String date = resultSet.getString("date");
            double totalIncome = resultSet.getDouble("total_income");
            dataset.addValue(0, "Total Income", date);  // Start with zero value
            dataset.addValue(totalIncome, "Total Income", date);
        }

        // Close the database resources
        resultSet.close();
       

    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Create the line chart
    JFreeChart lineChart = ChartFactory.createLineChart(
            "Income Statistics",    // Chart title
            "Date",                 // X-axis label
            "Total Income",         // Y-axis label
            dataset,                // Dataset
            PlotOrientation.VERTICAL,
            true,                   // Include legend
            true,                   // Include tooltips
            false                   // Include URLs
    );

    // Customize the chart appearance
    lineChart.getTitle().setPaint(new Color(255, 204, 0));              // Set chart title color
    lineChart.setBackgroundPaint(new Color(51,51,51));                         // Set chart background color

    CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
    plot.setBackgroundPaint(Color.BLACK);                              // Set plot background color
    plot.getRenderer().setSeriesPaint(0, new Color(255, 204, 0));      // Set line color

    CategoryAxis domainAxis = plot.getDomainAxis();
    domainAxis.setTickLabelPaint(new Color(255, 204, 0));              // Set X-axis tick labels color
    domainAxis.setLabelPaint(new Color(255, 204, 0));                  // Set X-axis label color

    ValueAxis rangeAxis = plot.getRangeAxis();
    rangeAxis.setTickLabelPaint(new Color(255, 204, 0));               // Set Y-axis tick labels color
    rangeAxis.setLabelPaint(new Color(255, 204, 0));                   // Set Y-axis label color

    // Create a chart panel to hold the line chart
    ChartPanel chartPanel = new ChartPanel(lineChart);

    // Set the preferred size of the chart panel
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));

    // Clear the existing components in panelincome
    panelincome.removeAll();

    // Add the chart panel to panelincome
    panelincome.setLayout(new BorderLayout());
    panelincome.add(chartPanel, BorderLayout.CENTER);

    // Revalidate panelincome to update the layout
    panelincome.revalidate();
    panelincome.repaint();
// Animate the line graph
    int totalDataPoints = dataset.getColumnCount();
    int animationDelay = 50; // Delay between each animation step in milliseconds
    int animationStep = 1; // Value increment for each animation step

    Timer timer = new Timer(animationDelay, new ActionListener() {
        private int currentIndex = 0;

        @Override
        public void actionPerformed(ActionEvent e) {
            // Get the current value for the current data point
            String date = dataset.getColumnKey(currentIndex).toString();
            double currentValue = dataset.getValue("Total Income", date).doubleValue();

            // Update the dataset with the animated value
            dataset.setValue(currentValue + animationStep, "Total Income", date);

            // Update the chart
            chartPanel.repaint();

            // Check if animation is complete
            if (currentIndex >= totalDataPoints - 1) {
                ((Timer) e.getSource()).stop();
            }

            currentIndex++;
        }
    });

    timer.start();
}


    
    
    
    
    
    
  private void displayCustomerBarGraph() {
    // Create a dataset for the bar graph
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    try {
        // Establish a connection to the database
        Connection connection = DBConnection.getConnection();

        // Create a SQL statement to count the number of customers for each day
        String sql = "SELECT date, COUNT(*) AS customer_count FROM sell GROUP BY date";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        // Add data to the dataset
        while (resultSet.next()) {
            String date = resultSet.getString("date");
            int customerCount = resultSet.getInt("customer_count");
            dataset.addValue(customerCount, "Customers", date);
        }

        // Close the database resources
        resultSet.close();
        

    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Create the bar chart
    JFreeChart barChart = ChartFactory.createBarChart(
            "Customer's Chart",   // Chart title
            "Date",                  // X-axis label
            "Number of Customers",   // Y-axis label
            dataset,                 // Dataset
            PlotOrientation.VERTICAL,
            true,                    // Include legend
            true,                    // Include tooltips
            false                    // Include URLs
    );

    // Customize the chart appearance
    barChart.getTitle().setPaint(new Color(255, 204, 0));              // Set chart title color
    barChart.setBackgroundPaint(new Color(44, 44, 44));                // Set chart background color to dark gray

    CategoryPlot plot = (CategoryPlot) barChart.getPlot();
    plot.setBackgroundPaint(new Color(44, 44, 44));                     // Set plot background color to dark gray
    plot.getRenderer().setSeriesPaint(0, new Color(255, 204, 0));      // Set bar color

    CategoryAxis domainAxis = plot.getDomainAxis();
    domainAxis.setTickLabelPaint(new Color(255, 204, 0));             // Set X-axis tick labels color
    domainAxis.setLabelPaint(new Color(255, 204, 0));                 // Set X-axis label color

    ValueAxis rangeAxis = plot.getRangeAxis();
    rangeAxis.setTickLabelPaint(new Color(255, 204, 0));              // Set Y-axis tick labels color
    rangeAxis.setLabelPaint(new Color(255, 204, 0));                  // Set Y-axis label color

    // Get the legend item for the "Customers" series
    LegendItemCollection legendItems = plot.getLegendItems();
    LegendItem customersLegendItem = legendItems.get(0);  // Assuming "Customers" is the first series
    customersLegendItem.setLabelPaint(Color.BLACK);  // Set legend label color
    customersLegendItem.setShapeVisible(false);

    customersLegendItem.setFillPaint(Color.BLACK);  // Set legend shape fill color

    // Create a chart panel to hold the bar chart
    ChartPanel chartPanel = new ChartPanel(barChart);

    // Set the preferred size of the chart panel
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));

 

    // Clear the existing components in the panelcustomer
    panelcustomer.removeAll();

    // Add the chart panel to the panelcustomer
    panelcustomer.setLayout(new BorderLayout());
    panelcustomer.add(chartPanel, BorderLayout.CENTER);

    // Revalidate the panelcustomer to update the layout
    panelcustomer.revalidate();
    panelcustomer.repaint();
    
  Timer timer = new Timer(25, new ActionListener() {
    private int count = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
        // Increase the height of each bar by a fixed amount
        for (int i = 0; i < dataset.getRowCount(); i++) {
            int value = dataset.getValue(i, 0).intValue();
            ((DefaultCategoryDataset) dataset).setValue(value + 1, dataset.getRowKey(i), dataset.getColumnKey(0));
        }

        // Repaint the chart panel to reflect the updated dataset
        chartPanel.repaint();

        // Stop the animation after a certain number of iterations (e.g., 100)
        count++;
        if (count >= 100) {
            ((Timer) e.getSource()).stop();
        }
    }
});

// Start the animation timer
timer.start();




}






    
    
    
    
    
    public void updateRemainProductProgressBar() {
    try {
        // Establish a connection to the database
        Connection connection = DBConnection.getConnection();

        // Create a SQL statement to calculate the total products in stock
        String inventorySql = "SELECT SUM(stock) AS total_stock FROM inventory";
        Statement inventoryStatement = connection.createStatement();
        ResultSet inventoryResultSet = inventoryStatement.executeQuery(inventorySql);

        // Retrieve the total products in stock from the inventory result set
        int totalStock = 0;
        if (inventoryResultSet.next()) {
            totalStock = inventoryResultSet.getInt("total_stock");
        }

        // Create a SQL statement to calculate the total number of sold products
        String sellSql = "SELECT SUM(no_of_products) AS total_sold FROM sell";
        Statement sellStatement = connection.createStatement();
        ResultSet sellResultSet = sellStatement.executeQuery(sellSql);

        // Retrieve the total number of sold products from the sell result set
        int totalSold = 0;
        if (sellResultSet.next()) {
            totalSold = sellResultSet.getInt("total_sold");
        }

        // Calculate the number of remaining products
        int remainingProducts = totalStock - totalSold;

        // Calculate the percentage of remaining products
        int percentageRemaining = (int) ((remainingProducts * 100.0) / (totalStock));

        // Animate the progress bar
      int initialValue = 0;
        int finalValue = percentageRemaining;
        int animationDuration = 500; // Animation duration in milliseconds

        Timer timer = new Timer(animationDuration / finalValue, new ActionListener() {
            int currentValue = initialValue;

            @Override
            public void actionPerformed(ActionEvent e) {
                currentValue++;
                progressbarremainproduct.setValue(currentValue);

                if (currentValue == finalValue) {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.start();

        // Close the database resources
        inventoryResultSet.close();
        sellResultSet.close();
        inventoryStatement.close();
        sellStatement.close();
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    
    
    
   public void updateSoldProductProgressBar() {
    try {
        // Establish a connection to the database
        Connection connection = DBConnection.getConnection();

        // Create a SQL statement to calculate the total products in stock
        String inventorySql = "SELECT SUM(stock) AS total_stock FROM inventory";
        Statement inventoryStatement = connection.createStatement();
        ResultSet inventoryResultSet = inventoryStatement.executeQuery(inventorySql);

        // Retrieve the total products in stock from the inventory result set
        int totalStock = 0;
        if (inventoryResultSet.next()) {
            totalStock = inventoryResultSet.getInt("total_stock");
        }

        // Create a SQL statement to calculate the total number of sold products
        String sellSql = "SELECT SUM(no_of_products) AS total_sold FROM sell";
        Statement sellStatement = connection.createStatement();
        ResultSet sellResultSet = sellStatement.executeQuery(sellSql);

        // Retrieve the total number of sold products from the sell result set
        int totalSold = 0;
        if (sellResultSet.next()) {
            totalSold = sellResultSet.getInt("total_sold");
        }

        // Calculate the percentage of sold products
        int percentageSold = (int) ((totalSold * 100.0) / (totalStock + totalSold));

        // Animate the progress bar
        int initialValue = 0;
        int finalValue = percentageSold;
        int animationDuration = 500; // Animation duration in milliseconds

        Timer timer = new Timer(animationDuration / finalValue, new ActionListener() {
            int currentValue = initialValue;

            @Override
            public void actionPerformed(ActionEvent e) {
                currentValue++;
                circleProgressBarnoofsoldproducts.setValue(currentValue);

                if (currentValue == finalValue) {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.start();

        // Close the database resources
        inventoryResultSet.close();
        sellResultSet.close();
        inventoryStatement.close();
        sellStatement.close();
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    
    
    
    
   public void updateTotalProductsLabel() {
    try {
        // Establish a connection to the database
        Connection connection = DBConnection.getConnection();

        // Create a SQL statement to calculate the total number of products
        String sql = "SELECT SUM(no_of_products) AS total_products FROM sell";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        // Retrieve the total number of products from the result set
        int totalProducts = 0;
        if (resultSet.next()) {
            totalProducts = resultSet.getInt("total_products");
        }

        // Close the database resources
        resultSet.close();
        statement.close();

        // Create a Timer to animate the label
        int animationDelay = 50; // Delay between each animation step in milliseconds
        int animationStep = 1; // Value increment for each animation step
        int finalValue = totalProducts;

        Timer timer = new Timer(animationDelay, new ActionListener() {
            private int currentValue = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                currentValue += animationStep;
                if (currentValue > finalValue) {
                    currentValue = finalValue;
                    ((Timer) e.getSource()).stop();
                }

                // Update the label value
                lblnoofsoldproduct.setText(Integer.toString(currentValue));
            }
        });

        // Start the Timer
        timer.start();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    
 public void updateTotalIncomeLabel() {
    try {
        // Establish a connection to the database
        Connection connection = DBConnection.getConnection();

        // Create a SQL statement to calculate the total income
        String sql = "SELECT SUM(total) AS total_income FROM sell";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        // Retrieve the total income from the result set
        double totalIncome = 0;
        if (resultSet.next()) {
            totalIncome = resultSet.getDouble("total_income");
        }

        // Close the database resources
        resultSet.close();
        statement.close();

        // Create a Timer to animate the label
        int animationDelay = 50; // Delay between each animation step in milliseconds
        double animationStep = totalIncome / 100; // Value increment for each animation step
        double finalValue = totalIncome;

        Timer timer = new Timer(animationDelay, new ActionListener() {
            private double currentValue = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                currentValue += animationStep;
                if (currentValue > finalValue) {
                    currentValue = finalValue;
                    ((Timer) e.getSource()).stop();
                }

                // Update the label value
                lblTotalIncome.setText(Double.toString(currentValue));
            }
        });

        // Start the Timer
        timer.start();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

   



public void updateTodayIncomeLabel() {
    try {
        // Establish a connection to the database
        Connection connection = DBConnection.getConnection();

        // Get today's date
        LocalDate today = LocalDate.now();

        // Create a SQL statement to calculate the total income for today
        String sql = "SELECT SUM(total) AS today_income FROM sell WHERE date = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, today.toString());
        ResultSet resultSet = statement.executeQuery();

        // Retrieve the total income for today from the result set
        double todayIncome = 0;
        if (resultSet.next()) {
            todayIncome = resultSet.getDouble("today_income");
        }

        // Close the database resources
        resultSet.close();
        statement.close();

        // Create a Timer to animate the label
        int animationDelay = 50; // Delay between each animation step in milliseconds
        double animationStep = todayIncome / 100; // Value increment for each animation step
        double finalValue = todayIncome;

        Timer timer = new Timer(animationDelay, new ActionListener() {
            private double currentValue = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                currentValue += animationStep;
                if (currentValue > finalValue) {
                    currentValue = finalValue;
                    ((Timer) e.getSource()).stop();
                }

                // Update the label value
                lbltodayIncome.setText(Double.toString(currentValue));
            }
        });

        // Start the Timer
        timer.start();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    
   public void updateNoOfCustomersLabel() {
    try {
        // Establish a connection to the database
        Connection connection = DBConnection.getConnection();

        // Create a SQL statement to count the number of rows in the "sell" table
        String sql = "SELECT COUNT(*) AS row_count FROM sell";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        // Retrieve the row count from the result set
        int rowCount = 0;
        if (resultSet.next()) {
            rowCount = resultSet.getInt("row_count");
        }

        // Update the label with the row count
        lblNumberofCustomers.setText(Integer.toString(rowCount));

        // Close the database resources
        resultSet.close();
        statement.close();
        
    } catch (SQLException e) {
        e.printStackTrace();
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
        jPanel2 = new javax.swing.JPanel();
        kGradientPanel1 = new keeptoo.KGradientPanel();
        jPanel7 = new javax.swing.JPanel();
        label1 = new java.awt.Label();
        jLabel4 = new javax.swing.JLabel();
        lblNumberofCustomers = new javax.swing.JLabel();
        kGradientPanel2 = new keeptoo.KGradientPanel();
        jPanel8 = new javax.swing.JPanel();
        label7 = new java.awt.Label();
        jLabel5 = new javax.swing.JLabel();
        lbltodayIncome = new javax.swing.JLabel();
        kGradientPanel3 = new keeptoo.KGradientPanel();
        jPanel9 = new javax.swing.JPanel();
        label8 = new java.awt.Label();
        jLabel6 = new javax.swing.JLabel();
        lblTotalIncome = new javax.swing.JLabel();
        kGradientPanel4 = new keeptoo.KGradientPanel();
        jPanel10 = new javax.swing.JPanel();
        label9 = new java.awt.Label();
        jLabel7 = new javax.swing.JLabel();
        lblnoofsoldproduct = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        panelincome = new javax.swing.JPanel();
        label5 = new java.awt.Label();
        panelcustomer = new javax.swing.JPanel();
        label6 = new java.awt.Label();
        jPanel4 = new javax.swing.JPanel();
        progressbarremainproduct = new Database.CircleProgressBar();
        label2 = new java.awt.Label();
        jPanel5 = new javax.swing.JPanel();
        label4 = new java.awt.Label();
        jPanel6 = new javax.swing.JPanel();
        circleProgressBarnoofsoldproducts = new Database.CircleProgressBar();
        jPanel11 = new javax.swing.JPanel();
        progressbartotal = new javax.swing.JProgressBar();
        lbltotal = new javax.swing.JLabel();
        label3 = new java.awt.Label();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        kGradientPanel1.setkEndColor(new java.awt.Color(0, 0, 0));
        kGradientPanel1.setkStartColor(new java.awt.Color(153, 153, 153));
        kGradientPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(102, 102, 102));

        label1.setBackground(new java.awt.Color(102, 102, 102));
        label1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        label1.setForeground(new java.awt.Color(255, 204, 0));
        label1.setText("Number of Customers");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 7, Short.MAX_VALUE)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        kGradientPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 30));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-customers-70.png"))); // NOI18N
        kGradientPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 100, 80));

        lblNumberofCustomers.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lblNumberofCustomers.setForeground(new java.awt.Color(255, 204, 0));
        lblNumberofCustomers.setText("100");
        kGradientPanel1.add(lblNumberofCustomers, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 90, -1));

        jPanel2.add(kGradientPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 240, 130));

        kGradientPanel2.setkEndColor(new java.awt.Color(0, 0, 0));
        kGradientPanel2.setkStartColor(new java.awt.Color(153, 153, 153));
        kGradientPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(102, 102, 102));

        label7.setBackground(new java.awt.Color(102, 102, 102));
        label7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        label7.setForeground(new java.awt.Color(255, 204, 0));
        label7.setText("Today's Income(RS)");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 7, Short.MAX_VALUE)
                .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        kGradientPanel2.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 30));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-income-70.png"))); // NOI18N
        kGradientPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 90, 80));

        lbltodayIncome.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lbltodayIncome.setForeground(new java.awt.Color(255, 204, 0));
        lbltodayIncome.setText("100");
        kGradientPanel2.add(lbltodayIncome, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 120, -1));

        jPanel2.add(kGradientPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, 240, 130));

        kGradientPanel3.setkEndColor(new java.awt.Color(0, 0, 0));
        kGradientPanel3.setkStartColor(new java.awt.Color(153, 153, 153));
        kGradientPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(102, 102, 102));

        label8.setBackground(new java.awt.Color(102, 102, 102));
        label8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        label8.setForeground(new java.awt.Color(255, 204, 0));
        label8.setText("Total Income(RS)");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 7, Short.MAX_VALUE)
                .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        kGradientPanel3.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 30));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-income-70 (1).png"))); // NOI18N
        kGradientPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 100, 80));

        lblTotalIncome.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lblTotalIncome.setForeground(new java.awt.Color(255, 204, 0));
        lblTotalIncome.setText("100");
        kGradientPanel3.add(lblTotalIncome, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 120, -1));

        jPanel2.add(kGradientPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 20, 240, 130));

        kGradientPanel4.setkEndColor(new java.awt.Color(0, 0, 0));
        kGradientPanel4.setkStartColor(new java.awt.Color(153, 153, 153));
        kGradientPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(102, 102, 102));

        label9.setBackground(new java.awt.Color(102, 102, 102));
        label9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        label9.setForeground(new java.awt.Color(255, 204, 0));
        label9.setText("Number of Sold Products");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 7, Short.MAX_VALUE)
                .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        kGradientPanel4.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 30));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-used-product-70.png"))); // NOI18N
        kGradientPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 100, 80));

        lblnoofsoldproduct.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lblnoofsoldproduct.setForeground(new java.awt.Color(255, 204, 0));
        lblnoofsoldproduct.setText("100");
        kGradientPanel4.add(lblnoofsoldproduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 90, -1));

        jPanel2.add(kGradientPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 20, 240, 130));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1040, 170));

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelincome.setBackground(new java.awt.Color(51, 51, 51));

        label5.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        label5.setForeground(new java.awt.Color(255, 204, 0));
        label5.setText("Income's Chart");

        javax.swing.GroupLayout panelincomeLayout = new javax.swing.GroupLayout(panelincome);
        panelincome.setLayout(panelincomeLayout);
        panelincomeLayout.setHorizontalGroup(
            panelincomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelincomeLayout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelincomeLayout.setVerticalGroup(
            panelincomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelincomeLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.add(panelincome, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 330, 290));

        panelcustomer.setBackground(new java.awt.Color(51, 51, 51));

        label6.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        label6.setForeground(new java.awt.Color(255, 204, 0));
        label6.setText("Customer's Chart");

        javax.swing.GroupLayout panelcustomerLayout = new javax.swing.GroupLayout(panelcustomer);
        panelcustomer.setLayout(panelcustomerLayout);
        panelcustomerLayout.setHorizontalGroup(
            panelcustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelcustomerLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelcustomerLayout.setVerticalGroup(
            panelcustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelcustomerLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.add(panelcustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 260, 290));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 620, 310));

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        progressbarremainproduct.setForeground(new java.awt.Color(255, 204, 0));
        progressbarremainproduct.setValue(60);
        jPanel4.add(progressbarremainproduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        label2.setBackground(new java.awt.Color(51, 51, 51));
        label2.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        label2.setForeground(new java.awt.Color(255, 204, 0));
        label2.setText("NO of products Sold");
        jPanel4.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 90, -1, -1));

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));

        label4.setBackground(new java.awt.Color(51, 51, 51));
        label4.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        label4.setForeground(new java.awt.Color(255, 204, 0));
        label4.setText("NO of products remain");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(187, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 190, 220));

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));

        circleProgressBarnoofsoldproducts.setForeground(new java.awt.Color(255, 204, 0));
        circleProgressBarnoofsoldproducts.setValue(60);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(circleProgressBarnoofsoldproducts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addComponent(circleProgressBarnoofsoldproducts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        jPanel4.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 200, 220));

        jPanel11.setBackground(new java.awt.Color(51, 51, 51));

        progressbartotal.setBackground(new java.awt.Color(0, 0, 0));
        progressbartotal.setForeground(new java.awt.Color(255, 204, 0));

        lbltotal.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lbltotal.setForeground(new java.awt.Color(255, 204, 0));
        lbltotal.setText("100");

        label3.setBackground(new java.awt.Color(51, 51, 51));
        label3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        label3.setForeground(new java.awt.Color(255, 204, 0));
        label3.setText("No Of Products Added Today");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(progressbartotal, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addComponent(lbltotal, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbltotal, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(progressbartotal, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))))
        );

        jPanel4.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 390, 70));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 190, 410, 310));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1060, 520));

        setBounds(0, 0, 1070, 550);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Database.CircleProgressBar circleProgressBarnoofsoldproducts;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private keeptoo.KGradientPanel kGradientPanel1;
    private keeptoo.KGradientPanel kGradientPanel2;
    private keeptoo.KGradientPanel kGradientPanel3;
    private keeptoo.KGradientPanel kGradientPanel4;
    private java.awt.Label label1;
    private java.awt.Label label2;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private java.awt.Label label5;
    private java.awt.Label label6;
    private java.awt.Label label7;
    private java.awt.Label label8;
    private java.awt.Label label9;
    private javax.swing.JLabel lblNumberofCustomers;
    private javax.swing.JLabel lblTotalIncome;
    private javax.swing.JLabel lblnoofsoldproduct;
    private javax.swing.JLabel lbltodayIncome;
    private javax.swing.JLabel lbltotal;
    private javax.swing.JPanel panelcustomer;
    private javax.swing.JPanel panelincome;
    private Database.CircleProgressBar progressbarremainproduct;
    private javax.swing.JProgressBar progressbartotal;
    // End of variables declaration//GEN-END:variables
}
