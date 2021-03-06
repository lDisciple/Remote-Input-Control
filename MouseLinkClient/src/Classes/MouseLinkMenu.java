/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class MouseLinkMenu extends javax.swing.JFrame implements Runnable{
    MouseLinkClient ml;
    boolean connected;
    /**
     * Creates new form NewJFrame
     */
    public MouseLinkMenu() {
        initComponents();
        connected = false;
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
        btnCloseConnection = new javax.swing.JButton();
        btnConnectLocal = new javax.swing.JButton();
        btnConnectTo = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MouseLink Menu");

        btnCloseConnection.setText("Close Connection");
        btnCloseConnection.setEnabled(false);
        btnCloseConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseConnectionActionPerformed(evt);
            }
        });

        btnConnectLocal.setText("Connect to LAN");
        btnConnectLocal.setEnabled(false);
        btnConnectLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectLocalActionPerformed(evt);
            }
        });

        btnConnectTo.setText("Connect to...");
        btnConnectTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectToActionPerformed(evt);
            }
        });

        lblStatus.setText("Status: None");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnConnectTo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnConnectLocal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCloseConnection, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                    .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConnectTo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConnectLocal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCloseConnection)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnectToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectToActionPerformed
        String ip = "";
        boolean isIP;
        do{
            isIP = true;
            ip = JOptionPane.showInputDialog("Enter IP address");
            if (ip != null) {
                String newip = ip.replace('.', ':');
                System.out.println(newip.split(":").length);
                if (newip.split(":").length != 4) {
                    isIP = false;
                }else{
                    try{
                        Integer.parseInt(newip.split(":")[0]);
                        Integer.parseInt(newip.split(":")[1]);
                        Integer.parseInt(newip.split(":")[2]);
                        Integer.parseInt(newip.split(":")[3]);
                        isIP = true;
                    }catch(NumberFormatException x){
                        isIP = false;
                    } 
                }
                
            }
        }while(!isIP && ip != null);
        if (ip != null) {
            ml = new MouseLinkClient(ip);
            ml.startRunning();
            btnConnectTo.setEnabled(false);
            btnConnectLocal.setEnabled(false);
            btnCloseConnection.setEnabled(true);
            connected = true;
        }
    }//GEN-LAST:event_btnConnectToActionPerformed

    private void btnConnectLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectLocalActionPerformed
        
    }//GEN-LAST:event_btnConnectLocalActionPerformed

    private void btnCloseConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseConnectionActionPerformed
        connected = false;
        ml.closeConnecion();
    }//GEN-LAST:event_btnCloseConnectionActionPerformed

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
            java.util.logging.Logger.getLogger(MouseLinkMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MouseLinkMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MouseLinkMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MouseLinkMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MouseLinkMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCloseConnection;
    private javax.swing.JButton btnConnectLocal;
    private javax.swing.JButton btnConnectTo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblStatus;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        while (connected) {
            lblStatus.setText(ml.getStatus());
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(MouseLinkMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
