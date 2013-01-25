package test;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener{
	String ip="192.168.28.2";
	String group="public";//������֤��
	JTabbedPane tabbedPane;
	JPanel p1,p2;
	JScrollPane jsp1,jsp2;
	JTable table1,table2;
	JLabel label;
	boolean state=false;   //��ǰ�Ƿ���ɨ��
	String state_m="";
	public GUI(){
		super("SNMPӦ�����");
		Container topContainer = this.getContentPane();
		topContainer.setLayout(new BorderLayout());
		tabbedPane=new JTabbedPane(JTabbedPane.TOP);
		
		initShowPanel();
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout());
		label = new JLabel("��ʼ״̬����ѡ�����ѡ����в�����");
		panel.add(label);
		this.add(panel,BorderLayout.SOUTH);
		this.add(tabbedPane,BorderLayout.CENTER);
		
		
		JMenuBar bar =new JMenuBar();
		JMenu operate = new JMenu("����");
		JMenuItem o1 = new JMenuItem("��ȡ�ӿ���Ϣ");
		o1.addActionListener(this);
		JMenuItem o2 = new JMenuItem("��ȡת����");
		o2.addActionListener(this);
		operate.add(o1);
		operate.add(o2);
		bar.add(operate);
		
		JMenu set = new JMenu("����");
		JMenuItem s1 = new JMenuItem("����·������ַ");
		s1.addActionListener(this);
		set.add(s1);
		bar.add(set);
		
		this.setBounds(200, 100, 800, 700);
		this.setJMenuBar(bar);
		this.setVisible(true);
		this.validate();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void initShowPanel(){
		tabbedPane.remove(p1);
		p1 =getInfo();
		tabbedPane.add("�鿴�ӿ���Ϣ", p1);
		tabbedPane.remove(p2);
		p2=getTran();
		tabbedPane.add("�鿴ת����", p2);
	}
	public JPanel getInfo(){
		JPanel panel = new JPanel();
		jsp1=new JScrollPane();
		table1 = new JTable();
		jsp1.getViewport().add(table1);
		panel.setLayout(new BorderLayout());
		panel.add(jsp1,BorderLayout.CENTER);
		return panel;
	}
	public JPanel getTran(){
		JPanel panel = new JPanel();
		jsp2=new JScrollPane ();
		table2 = new JTable();
		jsp2.getViewport().add(table2);
		panel.setLayout(new BorderLayout());
		panel.add(jsp2,BorderLayout.CENTER);
		return panel;
	}
	public JTable getTable1(){
		state=true;
		String name[]={"���","����","״̬","�ٶ�","�����ַ"};
		NetManagerTest_ nmt=new NetManagerTest_(ip,group);
		String result[][]=null;
		try {
			result = nmt.handle_1();
		} catch (Exception e) {
			dialog("����·����ʧ�ܣ�");
		}
		DefaultTableModel model = new DefaultTableModel(result,name);
		JTable table = new JTable(model);
		state=false;
		return table;
	}
	public JTable getTable2(){
		state=true;
		String name[]={"���","����","����","�����ַ"};
		NetManagerTest_ nmt=new NetManagerTest_(ip,group);
		String result[][]=null;
		try {
			result = nmt.handle_2();
		} catch (Exception e) {
			dialog("����·����ʧ�ܣ�");
		}
		DefaultTableModel model = new DefaultTableModel(result,name);
		JTable table = new JTable(model);
		state=false;
		return table;
	}
	public void refresh1(){
		jsp1.getViewport().remove(table1);
		table1 = getTable1();
		jsp1.getViewport().add(table1);
		jsp1.getViewport().validate();
	}
	public void refresh2(){
		jsp2.getViewport().remove(table2);
		table2 = getTable2();
		jsp2.getViewport().add(table2);
		jsp2.getViewport().validate();
	}
	public void setIP(String ip){
		this.ip=ip;
		initShowPanel();
	}
	public void stateRestore(){
		label.setText(state_m);
	}
	public void setState(String s){
		state_m=label.getText();
		label.setText(s);
	}
	public void dialog(){
		JOptionPane.showMessageDialog(null, "��ǰ����ɨ�裬���Եȡ�");
	}
	public void dialog(String message){
		JOptionPane.showMessageDialog(null, message);
	}
	public static void main(String args[]){
		new GUI();
	}
	public void actionPerformed(final ActionEvent e) {
		new Thread(){
			public void run(){
				if(e.getActionCommand().equals("��ȡ�ӿ���Ϣ")){
					if(state){
						dialog();
					}else{
						setState(ip+"ɨ���С�����");
						String group_=JOptionPane.showInputDialog("������������֤�֡�","public");
						if(group_!=null){
							GUI.this.group=group_;
						}
						refresh1();
						setState(ip+"ɨ�����");
					}
				} else if(e.getActionCommand().equals("��ȡת����")){
					if(state){
						dialog();
					} else{
						setState(ip+"ɨ���С�����");
						String group_=JOptionPane.showInputDialog("������������֤�֡�","public");
						if(group_!=null){
							GUI.this.group=group_;
						}
						refresh2();
						setState(ip+"ɨ�����");
					}
				} else if(e.getActionCommand().equals("����·������ַ")){
					if(state){
						dialog();
					} else{
						setState("����Ŀ���ַ��");
						String ip_ = JOptionPane.showInputDialog("��ǰ��Ŀ���ַΪ��"+ip+",�������µĵ�ַ��");
						if(ip_==null){
							stateRestore();
						} else if(ip_.trim().length()<7){
							dialog("�����õĵ�ַ��������������Ч��");
							stateRestore();
						} else{
							setIP(ip_);
							setState("���óɹ���");
						}
					}
				}
			}
		}.start();
	}
}
