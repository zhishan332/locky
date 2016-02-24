import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

/**
 * 操作页面
 *
 * @author wangqing
 * @since 16-2-23 上午9:00
 */
public class OpeDialog extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(OpeDialog.class);

    private Container con;//容器
    private FileLocker fileLocker;
    private static OpeDialog dialog;

    private JLabel infoLabel;

    private OpeDialog() {
        fileLocker = new FileLockerImpl();
        create();
        build();
    }


    public static OpeDialog getInstance() {
        if (dialog == null) dialog = new OpeDialog();
        return dialog;
    }


    public void create() {
        setPreferredSize(new Dimension(510, 300));
        setSize(new Dimension(510, 300));
        con = this.getContentPane();
        con.setLayout(null);
        this.setTitle("Smart文件加密解密");
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);//设置关闭操作
        toFront();
        pack();
    }


    public void build() {
        JButton lockBtn = new JButton("加密当前文件夹");
        lockBtn.setFont(new Font("宋体", Font.PLAIN, 14));
        lockBtn.setBounds(30, 25, 200, 50);
        lockBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String directory = System.getProperty("user.dir");
                logger.info("正在加密当前文件夹路径" + directory);
                long t1=System.currentTimeMillis();
                List<LockResult> res = fileLocker.lock(new File(directory));
                long t2=System.currentTimeMillis();
                long cost=(t2-t1)/1000;
                String costStr=cost<=0?"小于1秒":cost+"秒";
                if(res==null || res.isEmpty()){
                    OpeDialog.getInstance().getInfoLabel().setText("<html><p>已完成加密，耗时"+costStr+"</p></html>");
                }else{
                    OpeDialog.getInstance().getInfoLabel().setText("<html><p>"+res.toString()+"，耗时"+costStr+"</p></html>");
                }
            }
        });
        JButton dpLockBtn = new JButton("解密当前文件夹");
        dpLockBtn.setFont(new Font("宋体", Font.PLAIN, 14));
        dpLockBtn.setBounds(250, 25, 200, 50);
        dpLockBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String directory = System.getProperty("user.dir");
                logger.info("正在解密当前文件夹路径" + directory);
                long t1=System.currentTimeMillis();
                List<LockResult> res = fileLocker.recLock(new File(directory));
                long t2=System.currentTimeMillis();
                long cost=(t2-t1)/1000;
                String costStr=cost<=0?"小于1秒":cost+"秒";
                if(res==null || res.isEmpty()){
                    OpeDialog.getInstance().getInfoLabel().setText("<html><p>已完成解密，耗时"+costStr+"</p></html>");
                }else{
                    OpeDialog.getInstance().getInfoLabel().setText("<html><p>"+res.toString()+"，耗时"+costStr+"</p></html>");
                }

            }
        });


        con.add(lockBtn);
        con.add(dpLockBtn);


        infoLabel = new JLabel();
        infoLabel.setBounds(20, 76, 470, 200);
        infoLabel.setFont(new Font("宋体", Font.PLAIN, 13));
        infoLabel.setText("<html><p>放到指定文件夹下执行</p></html>");
        con.add(infoLabel);
    }


    public JLabel getInfoLabel() {
        return infoLabel;
    }
}
