package video;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private JPanel contentPane;
    private JPanel panel;
    private JPanel controlPanel;
    private JProgressBar progressBar;
    private JSlider volumControlerSlider;
    private JMenuBar menuBar;
    private JMenu mnFile;
    private JMenuItem mntmOpenVideo;
    private JMenuItem mntmOpenSubtitle;
    private JButton stopButton;
    private JButton playButton;
    private JButton forwardButton;
    private JButton backwordButton;
    private JButton FullScreenButton;
    private JLabel volumLabel;
    private JPanel progressTimePanel;
    private JLabel currentLabel;
    private JLabel totalLabel;
    private EmbeddedMediaPlayerComponent playerComponent;
    private int flag = 0;
    public static int condition = 0;
    //condition为0时是播放 为1时是暂停

    /**
     * Create the frame.
     */
    public MainFrame() {
        setTitle("媒体播放器");
        //设置图标
        setIconImage(new ImageIcon("picture/resizeApi.png").getImage());
        //为播放窗口设置任务栏图标和小图标

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(600, 150, 800, 600);
        //设置整个界面的布局
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        mnFile = new JMenu("文件");
        menuBar.add(mnFile);


        mntmOpenVideo = new JMenuItem("打开文件");
        mntmOpenVideo.setSelected(true);
        mntmOpenVideo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.openVedio();
            }
        });
        mnFile.add(mntmOpenVideo);


        contentPane = new JPanel();
//        contentPane.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentMoved(ComponentEvent e) {
//                System.out.println("----move----");
//            }
//        });
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);


        JPanel videoPanel = new JPanel();
        contentPane.add(videoPanel, BorderLayout.CENTER);
        videoPanel.setLayout(new BorderLayout(0, 0));

        //vlcj提供的EmbeddedMediaPlayerComponent，用来创建一个播放组件
        playerComponent = new EmbeddedMediaPlayerComponent();


        final Canvas videoSurface = playerComponent.getVideoSurface();

        videoSurface.addMouseListener(new MouseAdapter() {
            Timer mouseTime;
            @Override
            public void mouseClicked(MouseEvent e) {
                int i = e.getButton();
                if (i == MouseEvent.BUTTON1) {
                    if (e.getClickCount() == 1) {
                        mouseTime = new Timer(350, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (condition == 1) {
                                    //播放
                                    if(flag==1){
                                        Player.play(1);
                                    }
                                    else {
                                        Player.play();
                                    }
                                    condition = 0;
                                } else {
                                    //暂停
                                    if(flag==1){
                                        Player.pause(1);
                                    }
                                    else {
                                        Player.pause();
                                    }
                                    condition = 1;
                                }
                                mouseTime.stop();
                            }
                        });
                        mouseTime.restart();
                    } else if (e.getClickCount() == 2 && mouseTime.isRunning()) {
                        mouseTime.stop();
                        if (flag == 0) {
                            //全屏
                            Player.fullScreen();
                        } else if (flag == 1) {
                            //原始大小
                            Player.originalScreen();
                        }
                    }
                }
            }

        });
        videoPanel.add(playerComponent, BorderLayout.CENTER);
        //添加播放视频的窗口

        panel = new JPanel();
        videoPanel.add(panel, BorderLayout.SOUTH);
        panel.setLayout(new BorderLayout(0, 0));

        progressTimePanel = new JPanel();
        progressTimePanel.setPreferredSize(new Dimension(800,20));
        panel.add(progressTimePanel, BorderLayout.NORTH);
        progressTimePanel.setLayout(new BorderLayout(0, 0));

        progressBar = new JProgressBar();
        progressTimePanel.add(progressBar, BorderLayout.CENTER);
        progressBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                //鼠标拖动进度条事件
                Player.jumpTo((float) x / progressBar.getWidth());
            }
        });

        currentLabel = new JLabel("00:00:00");
        progressTimePanel.add(currentLabel, BorderLayout.WEST);
        totalLabel = new JLabel("00:00:00");
        progressTimePanel.add(totalLabel, BorderLayout.EAST);
        controlPanel = new JPanel();
        panel.add(controlPanel);

        ImageIcon icon = new ImageIcon("picture/play.png");
        Image temp = icon.getImage().getScaledInstance(50
                , 31, icon.getImage().SCALE_DEFAULT);
        icon = new ImageIcon(temp);
        playButton = new JButton(icon);
        playButton.setFocusPainted(false);
        playButton.setBorder(BorderFactory.createLoweredBevelBorder());
        playButton.setContentAreaFilled(false);
        playButton.setBorderPainted(false);
        playButton.setIcon(icon);
        //当视频正在播放时，暂停界面的图片显示为play.png

        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (condition == 1) {
                    //播放
                    Player.play();
                } else {
                    //暂停
                    playButton.setBackground(Color.black);
                    Player.pause();
                }
            }
        });

        Font f = new Font("微软雅黑", Font.BOLD, 15);

        backwordButton = new JButton();
        backwordButton.setPreferredSize(new Dimension(70, 31));
        backwordButton.setFocusPainted(false);
        backwordButton.setContentAreaFilled(false);
        backwordButton.setSelected(false);
        backwordButton.setFont(f);
        backwordButton.setText("快 退");
//        backwordButton.setBorderPainted(false);

        backwordButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //快退
                Player.jumpTo((float) ((progressBar.getPercentComplete() * progressBar.getWidth() - 10) / progressBar.getWidth()));
            }
        });


        volumControlerSlider = new JSlider();
        volumControlerSlider.setPreferredSize(new Dimension(150, 40));
        volumControlerSlider.setPaintLabels(true);
        volumControlerSlider.setSnapToTicks(true);
        volumControlerSlider.setPaintTicks(true);
        volumControlerSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                volumControlerSlider.setValue((int) (e.getX() * ((float) volumControlerSlider.getMaximum() / volumControlerSlider.getWidth())));
            }

        });
        volumControlerSlider.setValue(50);
        this.getMediaPlayer().setVolume(50);
        volumControlerSlider.setMaximum(100);
        volumControlerSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //设置声音大小
                Player.setVolum(volumControlerSlider.getValue());
            }
        });

        forwardButton = new JButton("快 进");
        forwardButton.setPreferredSize(new Dimension(70, 31));
        forwardButton.setFont(f);
        forwardButton.setContentAreaFilled(false);
        forwardButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //快进
                Player.jumpTo((float) progressBar.getPercentComplete() + 0.1f);
            }
        });

        stopButton = new JButton("停 止");
        stopButton.setPreferredSize(new Dimension(70, 31));
        stopButton.setFont(f);
        stopButton.setContentAreaFilled(false);
        stopButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //停止
                Player.stop();
            }
        });


        FullScreenButton = new JButton("全 屏");
        FullScreenButton.setPreferredSize(new Dimension(70, 31));
        FullScreenButton.setFont(f);
        FullScreenButton.setBorder(new ThreeDimensionalBorder(Color.BLACK, 200, 5));
        FullScreenButton.setContentAreaFilled(false);
        FullScreenButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //全屏
                Player.fullScreen();
            }
        });


        volumLabel = new JLabel("" + volumControlerSlider.getValue());

        controlPanel.setBorder(new EmptyBorder(0, 120, 0, 10));

        FlowLayout flow = (FlowLayout) controlPanel.getLayout();
        flow.setHgap(20);//水平间距
        flow.setVgap(0);//组件垂直间距

        controlPanel.add(stopButton);
        controlPanel.add(backwordButton);
        controlPanel.add(playButton);
        controlPanel.add(forwardButton);
        controlPanel.add(volumControlerSlider);
        controlPanel.add(volumLabel);
        controlPanel.add(FullScreenButton);
    }

    public class RoundBorder implements Border {

        public Insets getBorderInsets(Component c) {
            return new Insets(0, 0, 0, 0);
        }
        public boolean isBorderOpaque() {
            return false;
        }
        public void paintBorder(Component c, Graphics g, int x, int y,
                                int width, int height) {
            //使用黑色在组件的外边缘绘制一个圆角矩形
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
        }
    }
    public EmbeddedMediaPlayer getMediaPlayer() {
        return playerComponent.getMediaPlayer();
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public EmbeddedMediaPlayerComponent getPlayComponent() {
        return playerComponent;
    }

    public JButton getPlayButton() {
        return playButton;
    }

    public JPanel getControlPanel() {
        return controlPanel;
    }


    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
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

    public JPanel getProgressTimePanel() {
        return progressTimePanel;
    }

}

