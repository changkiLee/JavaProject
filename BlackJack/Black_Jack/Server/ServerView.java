package Server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ServerView {

	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		new ServerLayout();
	}

}

class ServerLayout extends JFrame implements ActionListener {
	private int xpos, ypos;				// 윈도우 창의 위치
	private Dimension dimen, dimen1;	// 윈도우창의 위치를 조정할 변수
	private Label lb = new Label("*Server Start!*");
	private JButton b_start = new JButton("Start");
	private JButton b_exit = new JButton("Exit");
	private TextArea ta = new TextArea();
	private ServerSocket serverSocket = null;

	// 프레임
	public ServerLayout() throws UnknownHostException {
		super();
		this.init();
		this.start();
		this.setSize(500, 550);
		this.setTitle("Server IP : "
				+ InetAddress.getLocalHost().getHostAddress());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dimen = Toolkit.getDefaultToolkit().getScreenSize();
		dimen1 = this.getSize();
		xpos = (int) (dimen.getWidth() / 2 - dimen1.getWidth() / 2);
		ypos = (int) (dimen.getHeight() / 2 - dimen1.getHeight() / 2);
		this.setLocation(xpos, ypos);
		this.setVisible(true);
	}

	// 화면 구성
	public void init() {
		// 레이아웃 방법
		BorderLayout border = new BorderLayout();
		this.setLayout(border);
		// 라벨
		this.add("North", lb);
		// 채팅창
		ta.setEditable(false);
		this.add("Center", ta);
		// 버튼
		FlowLayout flow = new FlowLayout(FlowLayout.RIGHT);
		Panel p = new Panel(flow);	// 패널
		p.add(b_start);
		p.add(b_exit);
		this.add("South", p);
	}

	// 이벤트 처리
	public void start() {
		b_start.addActionListener(this);
		b_exit.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == b_start) {
			lb.setText("On Server!");
			ta.setText("Server Start!");

			try {
				serverSocket = new ServerSocket(33000);
				Server_ server = new Server_(serverSocket, ta);
				server.start();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			b_start.setEnabled(false);
		} else if (e.getSource() == b_exit) {
			System.exit(0);
		}
	}
}