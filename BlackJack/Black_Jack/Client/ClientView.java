package Client;

import java.awt.*;
import java.awt.event.*;
import java.net.*;

import javax.swing.*;

import Server.BC_Client_m;
import Server.PROTOCOL;

public class ClientView {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ClientLayout();
	}
}

class ClientLayout extends JFrame implements ActionListener {
	// ������ â
	private Dimension dimen, dimen1;
	private int xpos, ypos;

	// ä��â, ���̵�, ������
	private TextArea ta = new TextArea();
	private JTextField jtf_chat = new JTextField();
	private JTextField jtf_nick = new JTextField();

	// ��ư
	private JButton b_connect = new JButton("Connect");
	private JButton b_ready = new JButton("Ready");
	private JButton b_bet = new JButton("Bet");
	private JButton b_hit = new JButton("Hit");
	private JButton b_stay = new JButton("Stay");

	// ���� ��ư
	private ButtonGroup grp = new ButtonGroup();
	private JRadioButton one_dollor = new JRadioButton("�� 1");
	private JRadioButton five_dollor = new JRadioButton("�� 5");
	private JRadioButton ten_dollor = new JRadioButton("�� 10");

	// ���� ȭ�� �κ�
	private GameView gv = new GameView();
	private String name = "";
	private int money = 100;
	private int bet_money = 0;
	private Label l_money = new Label("Money : ��" + String.valueOf(money));

	// ��Ʈ��ũ ����
	private String ip;
	private String port = "33000";
	private Sender_m th_sender = null;
	private Receiver_m th_receiver = null;

	// ������
	public ClientLayout() {
		super();
		this.init();
		this.start();
		this.setSize(680, 500);
		this.setTitle("Client");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dimen = Toolkit.getDefaultToolkit().getScreenSize();
		dimen1 = this.getSize();
		xpos = (int) (dimen.getWidth() / 2 - dimen1.getWidth() / 2);
		ypos = (int) (dimen.getHeight() / 2 - dimen1.getHeight() / 2);
		this.setLocation(xpos, ypos);
		this.setVisible(true);
	}

	// ȭ�鱸��
	public void init() {
		BorderLayout border = new BorderLayout();
		this.setLayout(border);

		// ��� �г�(����/�غ�)
		Panel p_main_north = new Panel(new FlowLayout(FlowLayout.LEFT));
		p_main_north.add(new Label("Nickname"));
		jtf_nick.setColumns(10);
		p_main_north.add(jtf_nick);
		p_main_north.add(b_connect);
		p_main_north.add(b_ready);
		this.add("North", p_main_north);

		// �ߴ� �г�(���� ȭ��)
		gv.initComponent();
		this.add("Center", gv);

		// �ϴ� �г�
		Panel p_main_south = new Panel(new FlowLayout(FlowLayout.LEFT));
		// ä��â(���/�Է�)
		Panel p_sub1 = new Panel(new BorderLayout());
		ta.setEditable(false);
		p_sub1.add("Center", ta);
		p_sub1.add("South", jtf_chat);
		p_main_south.add(p_sub1);
		// ���� �������̽�
		Panel p_sub2 = new Panel(new BorderLayout());
		// ��
		Panel p_sub2_north = new Panel(new FlowLayout(FlowLayout.LEFT));
		p_sub2_north.add(l_money);
		p_sub2.add("North", p_sub2_north);
		// ��
		Panel p_sub2_center = new Panel(new FlowLayout(FlowLayout.LEFT));
		one_dollor.setEnabled(false);
		five_dollor.setEnabled(false);
		ten_dollor.setEnabled(false);
		b_bet.setEnabled(false);
		grp.add(one_dollor);
		grp.add(five_dollor);
		grp.add(ten_dollor);
		p_sub2_center.add(one_dollor);
		p_sub2_center.add(five_dollor);
		p_sub2_center.add(ten_dollor);
		p_sub2.add("Center", p_sub2_center);
		// ��
		Panel p_sub2_south = new Panel(new FlowLayout(FlowLayout.LEFT));
		b_hit.setEnabled(false);
		b_ready.setEnabled(false);
		b_stay.setEnabled(false);
		p_sub2_south.add(b_bet);
		p_sub2_south.add(b_hit);
		p_sub2_south.add(b_stay);
		p_sub2.add("South", p_sub2_south);
		p_main_south.add("South", p_sub2);
		this.add("South", p_main_south);

	}

	// �̺�Ʈ ó��
	public void start() {
		b_connect.addActionListener(this);
		b_ready.addActionListener(this);
		b_bet.addActionListener(this);
		b_hit.addActionListener(this);
		b_stay.addActionListener(this);

		one_dollor.addActionListener(this);
		five_dollor.addActionListener(this);
		ten_dollor.addActionListener(this);

		jtf_chat.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		// ����
		if (e.getSource() == b_connect) {
			name = jtf_nick.getText();

			if (!name.equals("") && !name.equals("dealer")) {
				try {
					ip = InetAddress.getLocalHost().getHostAddress();
					Socket socket = new Socket(ip, Integer.parseInt(port));
					th_sender = new Sender_m(socket, name);
					th_receiver = new Receiver_m(socket, ta, gv, l_money, b_ready);
					th_sender.start();
					th_receiver.start();
					
					gv.setPlayer(name);
					jtf_nick.setEditable(false);
					b_connect.setEnabled(false);
					b_ready.setEnabled(true);
				} catch (Exception e1) {
					System.out.println(e1.getMessage());
					JLabel label = new JLabel(e1.getMessage());
					JFrame frame = new JFrame();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.add(label);
					frame.setTitle("Error");
					frame.setSize(250, 100);
					frame.setLocation(xpos, ypos);
					frame.setVisible(true);
				}
			}
		}
		// �غ� ��
		else if (e.getSource() == b_ready) {
			if(money <= 0)		// ���� ������
				System.exit(0);	// ����
			else {
				if(!th_receiver.getIsPlay()){
					th_sender.sendMessage(PROTOCOL.READY + "-");	// �غ�
				
					b_ready.setEnabled(false);
					b_bet.setEnabled(true);
					one_dollor.setEnabled(true);
					five_dollor.setEnabled(true);
					ten_dollor.setEnabled(true);
				}
			}
		} else if (e.getSource() == one_dollor) {
			bet_money = 1;
		} else if (e.getSource() == five_dollor) {
			bet_money = 5;
		} else if (e.getSource() == ten_dollor) {
			bet_money = 10;
		}
		else if (e.getSource() == b_bet) {
			if (bet_money > 0 && money >= bet_money) {
				money = th_receiver.getMoney();
				money -= bet_money;
				l_money.setText("Money : ��" + String.valueOf(money));
				th_sender.sendMessage(PROTOCOL.BET + "-" + String.valueOf(bet_money));
				gv.setBet(bet_money);
				
				b_bet.setEnabled(false);
				b_hit.setEnabled(true);
				b_stay.setEnabled(true);
				
				one_dollor.setEnabled(false);
				five_dollor.setEnabled(false);
				ten_dollor.setEnabled(false);
			}
		} else if (e.getSource() == b_hit) {
			if(th_receiver.getIsHOS()){
				if(gv.getValue(name) > 21)
					b_hit.setEnabled(false);
				else
					th_sender.sendMessage(PROTOCOL.HIT + "-");	// ��Ʈ
			}
		} else if (e.getSource() == b_stay) {
			if(th_receiver.getIsHOS()){
				th_sender.sendMessage(PROTOCOL.STAY + "-");	// ������
				b_hit.setEnabled(false);
				b_stay.setEnabled(false);
			}
		}
		// ä��
		else if (e.getSource() == jtf_chat) {
			if (!name.equals("")) {
				th_sender.sendMessage(PROTOCOL.CHATTING + "-" + jtf_chat.getText());
				jtf_chat.setText("");
			} else {
				jtf_chat.setText("");
			}
		}
	}
}