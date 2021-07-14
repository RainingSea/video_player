package video;

import java.awt.*;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FullFrame extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel progressTimepanel;

    private JButton playButton;
    private JButton backwordButton;
    private JButton smallButton;

    private JSlider volumControlerSlider;
    private JProgressBar progressBar;

    private JLabel volumLabel;
    private JLabel currentLabel;
    private JLabel totalLabel;
    public static int condition = 0;

    /**
     * Create the frame,a new control frame after enter full screen model.
     */
    public FullFrame() {
        setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        setType(Type.UTILITY);
        setResizable(false);
        setUndecorated(true);
        setOpacity(0.5f);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 623, 66);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        //contentPane.setPreferredSize(new Dimension(600,70));
        setContentPane(contentPane);
        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);


        Font f = new Font("微软雅黑", Font.BOLD, 15);
        backwordButton=new JButton("快 退");
        backwordButton.setFont(f);
        backwordButton.setContentAreaFilled(false);
        backwordButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Player.jumpTo((float)((progressBar.getPercentComplete() * progressBar.getWidth() - 10) / progressBar.getWidth()));
            }
        });


        ImageIcon icon = new ImageIcon("picture/play.png");
        Image temp = icon.getImage().getScaledInstance(50
                , 31, icon.getImage().SCALE_DEFAULT);
        icon = new ImageIcon(temp);
        playButton = new JButton(icon);
        playButton.setContentAreaFilled(false);
        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (MainFrame.condition==1) {
                    ImageIcon icon = new ImageIcon("picture/play.png");
                    Image temp = icon.getImage().getScaledInstance(50
                            , 31, icon.getImage().SCALE_DEFAULT);
                    icon = new ImageIcon(temp);
                    playButton.setIcon(icon);
                    Player.play();
                } else {
                    ImageIcon icon = new ImageIcon("picture/pause.png");
                    Image temp = icon.getImage().getScaledInstance(50
                            , 31, icon.getImage().SCALE_DEFAULT);
                    icon = new ImageIcon(temp);
                    playButton.setIcon(icon);
                    Player.pause();
                }
            }
        });


        JButton stopButton = new JButton("停 止");
        stopButton.setFont(f);
        stopButton.setContentAreaFilled(false);
        stopButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Player.stop(1);
            }
        });


        JButton forwardButton = new JButton("快 进");
        forwardButton.setFont(f);
        forwardButton.setContentAreaFilled(false);
        forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Player.jumpTo((float)progressBar.getPercentComplete() + 0.1f);
            }
        });
        panel.add(forwardButton);

        smallButton = new JButton("退出全屏");
        smallButton.setFont(f);
        smallButton.setContentAreaFilled(false);
        smallButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Player.originalScreen();
            }
        });


        volumControlerSlider = new JSlider();
        volumControlerSlider.setPaintTicks(true);
        volumControlerSlider.setSnapToTicks(true);
        volumControlerSlider.setPaintLabels(true);
        volumControlerSlider.setPreferredSize(new Dimension(150, 40));


        FlowLayout flow = (FlowLayout) panel.getLayout();
        flow.setHgap(20);//水平间距
        flow.setVgap(0);//组件垂直间距

        backwordButton.setPreferredSize(new Dimension(70,30));
        panel.add(backwordButton);

        playButton.setFocusPainted(false);
        panel.add(playButton);
        forwardButton.setPreferredSize(new Dimension(70,30));
        panel.add(forwardButton);

        panel.add(volumControlerSlider);
        volumLabel = new JLabel("0");
        panel.add(volumLabel);
        smallButton.setPreferredSize(new Dimension(100,30));
        panel.add(smallButton);



        volumControlerSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                volumControlerSlider.setValue(volumControlerSlider.getMaximum() / volumControlerSlider.getWidth() * e.getX());
                Player.getFrame().getVolumControlerSlider().setValue(volumControlerSlider.getValue());
            }
        });
        volumControlerSlider.setMaximum(100);
        volumLabel.setText(volumControlerSlider.getValue()+"");

        progressTimepanel = new JPanel();
        //progressTimepanel.setPreferredSize(new Dimension(500,10));
        contentPane.add(progressTimepanel, BorderLayout.NORTH);
        progressTimepanel.setLayout(new BorderLayout(0, 0));

        progressBar = new JProgressBar();
        progressTimepanel.add(progressBar, BorderLayout.CENTER);
        progressBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                Player.jumpTo(((float) x / progressBar.getWidth()));
            }
        });

        currentLabel = new JLabel("00:00:00");
        progressTimepanel.add(currentLabel, BorderLayout.WEST);

        totalLabel = new JLabel("00:00:00");
        progressTimepanel.add(totalLabel, BorderLayout.EAST);
        volumControlerSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Player.setVolum(volumControlerSlider.getValue());
            }
        });


    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public JButton getPlayButton() {
        return playButton;
    }

    public JSlider getVolumControlerSlider() {
        return volumControlerSlider;
    }

    public JLabel getVolumLabel() {
        return volumLabel;
    }

    public JLabel getCurrentLabel() {
        return currentLabel;
    }

    public JLabel getTotalLabel() {
        return totalLabel;
    }

}
