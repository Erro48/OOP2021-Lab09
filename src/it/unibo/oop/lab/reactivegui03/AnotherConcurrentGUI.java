package it.unibo.oop.lab.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AnotherConcurrentGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JLabel display = new JLabel();
    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");
    private final JButton stop = new JButton("stop");
    
    private final CounterAgent agent = new CounterAgent();
    
    public AnotherConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        panel.add(display);
        panel.add(up);
        panel.add(down);
        panel.add(stop);
        this.getContentPane().add(panel);
        this.setVisible(true);
        
        this.up.addActionListener( e -> agent.goUp());
        this.down.addActionListener( e -> agent.goDown());
        this.stop.addActionListener( e -> agent.stopCounter());
        
        new Thread(agent).start();
    }
    
    private class CounterAgent implements Runnable {
        
        private static final int DELAY = 100;
        
        private boolean up = true;
        private int counter;
        private boolean stop = false;

        @Override
        public void run() {
            while (!this.stop) {
                AnotherConcurrentGUI.this.display.setText(Integer.toString(this.counter));;
                if (this.up) {
                    this.incCounter();
                } else {
                    this.decCounter();
                }
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
        private void incCounter() {
            this.counter++;
        }
        
        private void decCounter() {
            this.counter--;
        }
        
        private void goUp() {
            this.up = true;
        }
        
        private void goDown() {
            this.up = false;
        }
        
        private void stopCounter() {
            this.stop = true;
            AnotherConcurrentGUI.this.up.setEnabled(false);
            AnotherConcurrentGUI.this.down.setEnabled(false);
            AnotherConcurrentGUI.this.stop.setEnabled(false);
        }
    }
    
}
