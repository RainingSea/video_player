package video;

import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_logo_position_e;
import uk.co.caprica.vlcj.player.Logo;
import uk.co.caprica.vlcj.player.embedded.DefaultAdaptiveRuntimeFullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.List;

public class Player {

    private static MainFrame frame;
    private static FullFrame fullFrame;
    private static TimeUtil timeUtil;

    public static void main(String[] args) {

        //找到播放器的组件
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "E:\\course_project\\VLC");

        //打印版本
        System.out.println(LibVlc.INSTANCE.libvlc_get_version());


        //创建播放器
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame = new MainFrame();
                    frame.setVisible(true);
                    //设置显示比例
                    frame.getMediaPlayer().setAspectRatio(16 + ":" + 9);
                    frame.getMediaPlayer().setScale(0);
                    pause();
                    fullFrame = new FullFrame();
                    timeUtil = new TimeUtil();

                    //利用进度条来显示当前进度
                    new SwingWorker<String, Integer>() {
                        @Override
                        protected String doInBackground() throws Exception {
                            while (true) {
                                long totalTime = frame.getMediaPlayer().getLength();
                                long currentTime = frame.getMediaPlayer().getTime();
                                timeUtil.timeFormat(totalTime, currentTime);
                                frame.getCurrentLabel().setText(timeUtil.getHourCurrent() + ":" + timeUtil.getMinitueCurrent() + ":" + timeUtil.getSecondCurrent());
                                frame.getTotalLabel().setText(timeUtil.getHourTotal() + ":" + timeUtil.getMinitueTotal() + ":" + timeUtil.getSecondTotal());
                                fullFrame.getCurrentLabel().setText(frame.getCurrentLabel().getText());
                                fullFrame.getTotalLabel().setText(frame.getTotalLabel().getText());
                                float percent = (float) currentTime / totalTime;
                                publish((int) (percent * 100));
                                Thread.sleep(200);
                            }
                        }

                        protected void process(List<Integer> chunks) {
                            for (int v : chunks) {
                                frame.getProgressBar().setValue(v);
                                fullFrame.getProgressBar().setValue(v);
                            }
                        }

                        ;
                    }.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //播放视频
    public static void play() {
        System.out.println("PLAY");
        ImageIcon icon = new ImageIcon("picture/play.png");
        Image temp = icon.getImage().getScaledInstance(50
                , 31, icon.getImage().SCALE_DEFAULT);
        icon = new ImageIcon(temp);
        frame.getPlayButton().setIcon(icon);

        frame.getMediaPlayer().play();
        MainFrame.condition = 0;
    }
    public static void play(int i) {
        System.out.println("PLAY");
        ImageIcon icon = new ImageIcon("picture/play.png");
        Image temp = icon.getImage().getScaledInstance(50
                , 31, icon.getImage().SCALE_DEFAULT);
        icon = new ImageIcon(temp);
        fullFrame.getPlayButton().setIcon(icon);

        frame.getMediaPlayer().play();
        MainFrame.condition = 0;
    }

    //暂停视频
    public static void pause() {
        System.out.println("PAUSE");
        ImageIcon icon = new ImageIcon("picture/pause.png");
        Image temp = icon.getImage().getScaledInstance(50
                , 31, icon.getImage().SCALE_DEFAULT);
        icon = new ImageIcon(temp);
        frame.getPlayButton().setIcon(icon);

        frame.getMediaPlayer().pause();
        MainFrame.condition = 1;
    }
    public static void pause(int i) {
        System.out.println("PAUSE");
        ImageIcon icon = new ImageIcon("picture/pause.png");
        Image temp = icon.getImage().getScaledInstance(50
                , 31, icon.getImage().SCALE_DEFAULT);
        icon = new ImageIcon(temp);
        fullFrame.getPlayButton().setIcon(icon);

        frame.getMediaPlayer().pause();
        MainFrame.condition = 1;
    }

    //停止视频
    public static void stop() {
        System.out.println("STOP");
        ImageIcon icon = new ImageIcon("picture/pause.png");
        Image temp = icon.getImage().getScaledInstance(50
                , 31, icon.getImage().SCALE_DEFAULT);
        icon = new ImageIcon(temp);
        frame.getPlayButton().setIcon(icon);

        frame.getMediaPlayer().stop();
        MainFrame.condition = 1;
    }
    public static void stop(int i) {
        System.out.println("STOP");
        ImageIcon icon = new ImageIcon("picture/pause.png");
        Image temp = icon.getImage().getScaledInstance(50
                , 31, icon.getImage().SCALE_DEFAULT);
        icon = new ImageIcon(temp);
        fullFrame.getPlayButton().setIcon(icon);

        frame.getMediaPlayer().stop();
        MainFrame.condition = 1;
    }


    //打开视频
    public static void openVedio() {
        String option[] = {"--subsdec-encoding=GB18030"};
        JFileChooser chooser = new JFileChooser();
        chooser.setPreferredSize(new Dimension(500,600));
        int v = chooser.showOpenDialog(null);
        if (v == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            frame.getMediaPlayer().playMedia(file.getAbsolutePath(), option);
            play();
        }
    }


    //开启全屏
    public static void fullScreen() {
        frame.getMediaPlayer().setFullScreenStrategy(new DefaultAdaptiveRuntimeFullScreenStrategy(frame));
        frame.getProgressBar().setVisible(false);
        frame.getControlPanel().setVisible(false);
        frame.getProgressTimePanel().setVisible(false);
        frame.getJMenuBar().setVisible(false);
        frame.getMediaPlayer().setFullScreen(true);
        frame.setFlag(1);
        frame.getPlayComponent().getVideoSurface().addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (frame.getFlag() == 1) {
                    fullFrame.setLocation((frame.getWidth() - fullFrame.getWidth()) / 2, frame.getHeight() - fullFrame.getHeight());
                    fullFrame.setVisible(true);
                    fullFrame.getVolumControlerSlider().setValue(frame.getVolumControlerSlider().getValue());
                    if (MainFrame.condition == 1) {
                        ImageIcon icon = new ImageIcon("picture/play.png");
                        Image temp = icon.getImage().getScaledInstance(50
                                , 31, icon.getImage().SCALE_DEFAULT);
                        icon = new ImageIcon(temp);
                        frame.getPlayButton().setIcon(icon);
                    } else {
                        ImageIcon icon = new ImageIcon("picture/pause.png");
                        Image temp = icon.getImage().getScaledInstance(50
                                , 31, icon.getImage().SCALE_DEFAULT);
                        icon = new ImageIcon(temp);
                        frame.getPlayButton().setIcon(icon);
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO
            }
        });
    }

    //退出全屏
    public static void originalScreen() {
        frame.getProgressBar().setVisible(true);
        frame.getControlPanel().setVisible(true);
        frame.getProgressTimePanel().setVisible(true);
        frame.getJMenuBar().setVisible(true);
        frame.getMediaPlayer().setFullScreen(false);
        frame.setFlag(0);
        if (MainFrame.condition == 0) {
            ImageIcon icon = new ImageIcon("picture/play.png");
            Image temp = icon.getImage().getScaledInstance(50
                    , 31, icon.getImage().SCALE_DEFAULT);
            icon = new ImageIcon(temp);
            frame.getPlayButton().setIcon(icon);
        } else {
            ImageIcon icon = new ImageIcon("picture/pause.png");
            Image temp = icon.getImage().getScaledInstance(50
                    , 31, icon.getImage().SCALE_DEFAULT);
            icon = new ImageIcon(temp);
            frame.getPlayButton().setIcon(icon);
        }
        fullFrame.setVisible(false);
    }

    //快进 or 快退
    public static void forword(float to) {
        frame.getMediaPlayer().setTime((long) (to * frame.getMediaPlayer().getLength()));
    }

    //设置进度条时间
    public static void jumpTo(float to) {
        frame.getMediaPlayer().setTime((long) (to * frame.getMediaPlayer().getLength()));
    }

    //设置声音
    public static void setVolum(int v) {
        frame.getMediaPlayer().setVolume(v);
        frame.getVolumLabel().setText(frame.getMediaPlayer().getVolume() + "");
        fullFrame.getVolumLabel().setText(frame.getMediaPlayer().getVolume() + "");
    }

    public static MainFrame getFrame() {
        return frame;
    }

    public static FullFrame getControlFrame() {
        return fullFrame;
    }


}

