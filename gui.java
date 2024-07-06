import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class gui extends JFrame {
    JPanel panel = new JPanel();
    JLabel l1 = new JLabel("Number of Processes:");
    JLabel l2 = new JLabel("Time Quantum:");
    JTextField t1 = new JTextField(10);
    JTextField t2 = new JTextField(10);
    JButton b1 = new JButton("Submit");
    JTextArea txtOutput = new JTextArea();

    public gui() {
        this.setTitle("Round Robin Scheduler");
        this.setVisible(true);
        this.setSize(800, 800);
        this.setLocation(600, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        panel.setLayout(null);
        panel.setBackground(Color.white);
        l1.setBounds(110, 10, 150, 20);
        panel.add(l1);
        t1.setBounds(370, 10, 100, 20);
        panel.add(t1);
        l2.setBounds(110, 50, 150, 20);
        panel.add(l2);
        t2.setBounds(370, 50, 100, 20);
        panel.add(t2);
        b1.setBounds(240, 100, 100, 25);
        panel.add(b1);
        txtOutput.setBounds(10, 150, 750, 300);
        panel.add(txtOutput);

        this.add(panel, BorderLayout.CENTER);

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              try {
                int numProcesses = Integer.parseInt(t1.getText());
                int timeQuantum = Integer.parseInt(t2.getText());
          
                if (numProcesses <= 0 || timeQuantum <= 0) {
                  throw new NumberFormatException();
                }
          
                SchedulerPanel schedulerPanel = new SchedulerPanel(numProcesses, timeQuantum);
                schedulerPanel.schedule();
                
          
                // Capture the complete Gantt chart with details
                String ganttChart = schedulerPanel.getGanttChart();
          
                txtOutput.setText(ganttChart);
              } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please eenter valid numbers for processes and time quantum.");
              }
            }
          });
          

    }
}