package it.unibo.oop.lab.reactivegui03;

import javax.swing.JFrame;

public class AnotherConcurrentGUI extends JFrame {
    package it.unibo.oop.lab.reactivegui02;

    import java.awt.Dimension;
    import java.awt.Toolkit;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.lang.reflect.InvocationTargetException;

    import javax.swing.*;

    public class ConcurrentGUI extends JFrame {

        private static final long serialVersionUID = 1L;
        private static final double WIDTH_PERC = 0.2;
        private static final double HEIGHT_PERC = 0.1;
        private final JLabel display = new JLabel();
        private final JButton up = new JButton("up");
        private final JButton down = new JButton("down");
        private final JButton stop = new JButton("stop");
        
        public ConcurrentGUI() {
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
            
            final Agent agent = new Agent();
            new Thread(agent).start();
            
            stop.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    agent.stopCounting();
                    up.setEnabled(false);
                    down.setEnabled(false);
                    stop.setEnabled(false);
                }
            });
            up.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    agent.increment();
                }
            });
            down.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    agent.decrement();
                }
            });
        }
        
        private class Agent implements Runnable {
            /*
             * Stop is volatile to ensure visibility. Look at:
             * 
             * http://archive.is/9PU5N - Sections 17.3 and 17.4
             * 
             * For more details on how to use volatile:
             * 
             * http://archive.is/4lsKW
             * 
             */
            private static final int DELAYMS = 100;
            private volatile boolean stop;
            private volatile int counter;
            private boolean countingDirection = true;

            @Override
            public void run() {
                while (!this.stop) {
                    try {
                        /*
                         * All the operations on the GUI must be performed by the
                         * Event-Dispatch Thread (EDT)!
                         */
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                // This will happen in the EDT: since i'm reading counter it needs to be volatile.
                                ConcurrentGUI.this.display.setText(Integer.toString(Agent.this.counter));
                            }
                        });
                        /*
                         * SpotBugs shows a warning because the increment of a volatile variable is not atomic,
                         * so the concurrent access is potentially not safe. In the specific case of this exercise,
                         * we do synchronization with invokeAndWait, so it can be ignored.
                         *
                         * EXERCISE: Can you think of a solution that doesn't require counter to be volatile?
                         */
                        if (this.countingDirection) {
                            this.counter++;
                        } else {
                            this.counter--;
                        }
                        Thread.sleep(DELAYMS);
                    } catch (InvocationTargetException | InterruptedException ex) {
                        /*
                         * This is just a stack trace print, in a real program there
                         * should be some logging and decent error reporting
                         */
                        ex.printStackTrace();
                    }
                }
            }

            /**
             * External command to stop counting.
             */
            public void stopCounting() {
                this.stop = true;
            }
            
            public void increment() {
                this.countingDirection = true;
            }
            
            public void decrement() {
                this.countingDirection = false;
            }
        }
    }

}
