import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.GregorianCalendar;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.StyledDocument;

public class Events extends JDialog implements ActionListener, PropertyChangeListener{
	private int day1, year1;
	private String month1, title1,toDo;

	JLabel title,day,month,year,additional;
	JOptionPane option;
	JComboBox cDay, cMonth, cYear;
	JTextField inTitle;
	JTextArea inAdditional;
	JScrollPane sAdditional;
	JPanel dmyPane, titlePane, additionalPane, bttnPane,all;
	JButton create, cancel;
	int selYear, selMonth,outDay, outYear;
	String inT = "", inA, outMon;
	AbstractDocument doc;
	DocumentFilter docFil;
	GregorianCalendar cal;
	String[] months = new String[]{"January", "February", "March","April", "May", "June", "July","August","September", "October", "November", "December"};


	public Events(JFrame frame){
		super(frame,true);
		super.setSize(500,500);

		all = new JPanel();
		//all.setTitle("New Event");
		all.setPreferredSize(new Dimension(500,500));
		//all.setResizable(false);
		all.setVisible(true);
		//all.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		title = new JLabel("Title:");
		day = new JLabel("Day:");
		month = new JLabel("Month:");
		year = new JLabel("Year:");
		additional = new JLabel("Additional Notes:");

		inTitle  = new JTextField(30);
		inAdditional  = new JTextArea(1,1);
		inTitle.setEditable(true);
		inAdditional.setEditable(true);
		inAdditional.setLineWrap(true);
		inAdditional.setWrapStyleWord(true);
		inTitle.setText("");
		inAdditional.setText("");
		sAdditional = new JScrollPane(inAdditional);
		sAdditional.setPreferredSize(new Dimension(500,300));

		cDay = new JComboBox();
		cal = new GregorianCalendar(); 
		selYear = cal.get(GregorianCalendar.YEAR);
		selMonth = cal.get(GregorianCalendar.MONTH); 
		numDays(selMonth, selYear); 

		cMonth = new JComboBox();

		for (int i = cal.get(GregorianCalendar.MONTH); i<12; i++)
			cMonth.addItem(months[i]);

		cYear = new JComboBox();
		for (int i = cal.get(GregorianCalendar.YEAR); i<= cal.get(GregorianCalendar.YEAR)+10; i++){
			cYear.addItem(i+"");
		}	

		create = new JButton("Create Event");
		cancel = new JButton("Cancel");

		titlePane = new JPanel();
		titlePane.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		titlePane.add(title);
		titlePane.add(inTitle);

		dmyPane = new JPanel();
		dmyPane.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		dmyPane.add(day);
		dmyPane.add(cDay);
		dmyPane.add(month);
		dmyPane.add(cMonth);
		dmyPane.add(year);
		dmyPane.add(cYear);

		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.gridwidth = 1;
		gc.gridheight = 1;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(1, 1, 15, 15);
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.fill = GridBagConstraints.NONE;

		additionalPane = new JPanel();
		additionalPane.setLayout(new GridBagLayout());
		additionalPane.add(additional,gc);

		gc.ipady = 200;
		gc.ipadx = 500;
		gc.gridx = 0;
		gc.gridy = GridBagConstraints.RELATIVE;
		gc.gridwidth = GridBagConstraints.REMAINDER;
		gc.gridheight = GridBagConstraints.REMAINDER;
		gc.weightx = 0.1;
		gc.weighty = 0;
		gc.anchor = GridBagConstraints.NORTHWEST;
		additionalPane.add(sAdditional,gc);

		bttnPane = new JPanel();
		bttnPane.setLayout(new FlowLayout(FlowLayout.CENTER,10,0));
		bttnPane.add(create);
		bttnPane.add(cancel);

		all.setLayout(new BoxLayout(all,BoxLayout.Y_AXIS));
		all.add(Box.createVerticalStrut(15));
		all.add(titlePane);
		all.add(Box.createVerticalStrut(30));
		all.add(dmyPane);
		all.add(Box.createVerticalStrut(30));
		all.add(additionalPane);
		//all.add(Box.createVerticalStrut(30));
		//all.add(bttnPane);
		all.add(Box.createVerticalStrut(15));

		cMonth.setActionCommand("Month Change");
		cYear.setActionCommand("Year Change");
		create.setActionCommand("create");
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);
		create.addActionListener(this);
		cMonth.addActionListener(this);
		cYear.addActionListener(this);

		option = new JOptionPane(all, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, new String[]{"Create Event", "Cancel"}, "default");	
		getContentPane().add(option);
		option.addPropertyChangeListener(this);

		inAdditional.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent evt) {
				if(inAdditional.getText().length()>=300 && !(evt.getKeyChar()==KeyEvent.VK_DELETE||evt.getKeyChar()==KeyEvent.VK_BACK_SPACE)) {
					JOptionPane.showMessageDialog(null, "Character Limit Of 300 Characters Exceeded", "Error - Character Limit Exceeded", JOptionPane.ERROR_MESSAGE);
					option.setValue(null);
					evt.consume();
				}
			}
		});

		inTitle.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent evt) {
				if(inTitle.getText().length()>=30 && !(evt.getKeyChar()==KeyEvent.VK_DELETE||evt.getKeyChar()==KeyEvent.VK_BACK_SPACE)) {
					JOptionPane.showMessageDialog(null, "Character Limit Of 30 Characters Exceeded", "Error - Character Limit Exceeded", JOptionPane.ERROR_MESSAGE);
					option.setValue(null);
					evt.consume();
				}
			}
		});

		this.day1 = 0;
		this.month1 = null;
		this.year1 = 0;
		this.title1 = "";
		this.toDo = null;
	}

	public Events(JFrame frame,int day, String month, int year, String title,String toDo){

		this.day1 = day;
		this.month1 = month;
		this.year1 = year;
		this.title1 = title;
		this.toDo = toDo;
	}
	
	public Events(){

		this.day1 = 0;
		this.month1 = null;
		this.year1 = 0;
		this.title1 = "";
		this.toDo = null;
	}

	public void setDay ( int new_day){
		day1 = new_day;
	}
	public void setYear ( int new_year){
		year1 = new_year;
	}
	public void setMonth ( String new_month){
		month1 = new_month;
	}

	public void setTitle ( String new_title){
		title1 = new_title;
	}
	public void setToDo ( String new_toDo){
		toDo = new_toDo;
	}
	public String getTitle( ){
		return title1;
	}
	public int getYear (){
		return year1;
	}
	public int getDay (){
		return day1;
	}
	public int getMonth(){

		for (int i = 0; i <12; i++){
			if(months[i].equals(month1))
				return i;
		}
		return 0;
	}


	public String toString(){
		return("Title: "+ title1 +". \n Day: "+day1+" Month: "+month1+" Year: "+year1+"\n Notes:" + toDo); 
	}

	public JPanel draw (Container contain){ 

		JLabel dTitle = new JLabel();
		JLabel dDMY = new JLabel();
		JTextArea dInfo = new JTextArea(0, 0);
		dInfo.setEditable(false);
		JPanel single = new JPanel();
		JScrollPane sInfo = new JScrollPane(dInfo);

		Font f = new Font("Calibri(Body)", Font.BOLD, 18);
		dTitle.setFont(f);
		dTitle.setForeground(new Color(0, 0, 139));
		dTitle.setText(title1);

		dTitle.setBackground(new Color(220,220,220));
		dTitle.setOpaque(true);

		dDMY.setForeground(new Color(0,191,255));
		f = new Font("Calibri(Body)", Font.BOLD, 14);
		dDMY.setFont(f);
		dDMY.setText("\n" + day1 + " "+ month1 + " "+ year1);
		dDMY.setBackground(new Color(220,220,220));
		dDMY.setOpaque(true);

		dInfo.setForeground(Color.DARK_GRAY);
		dInfo.setBackground(new Color(200,200,200));
		dInfo.setFont(f);
		dInfo.setText(toDo);
		dInfo.setLineWrap(true);
		dInfo.setWrapStyleWord(true);
		single.setOpaque(false);
		single.setLayout(null);

		dTitle.setBounds(15,(0),370,30);
		dDMY.setBounds(15,(30),370,15);
		sInfo.setBounds(15,(45),370,60);
		sInfo.setBorder(null);

		single.add(dTitle);
		single.add(dDMY);
		single.add(sInfo);
		return single;
	}

	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		if (isVisible() && (e.getSource() == option)){

			if (JOptionPane.VALUE_PROPERTY.equals(prop)){
				if ("Create Event".equals(e.getNewValue())) {
					if (inTitle.getText().equals("")){
						JOptionPane.showMessageDialog(null, "Title Field cannot be left blank", "Error - Title Field Left Blank", JOptionPane.ERROR_MESSAGE);
						option.setValue(null);
						return;
					}
					else{
						title1 = inTitle.getText();
						toDo = inAdditional.getText();
						month1 = cMonth.getSelectedItem()+"";
						day1 = Integer.parseInt(cDay.getSelectedItem()+"");
						year1 = Integer.parseInt(cYear.getSelectedItem()+"");
						dispose();
					}
				}
				else if("Cancel".equals(e.getNewValue())){
					dispose();
				}
				else if (e.getNewValue() == JOptionPane.UNINITIALIZED_VALUE) {
					return;
				}
			}
		}
	}

	public void actionPerformed(ActionEvent evt){

		if (evt.getActionCommand().equals("Year Change")){
			selYear = Integer.parseInt(cYear.getSelectedItem()+"");
			numMonths(selYear);
		}
		if (evt.getActionCommand().equals("Month Change")){
			if (cMonth.getItemCount() > 0 && cMonth.getItemAt(0).equals("January"))
				selMonth = cMonth.getSelectedIndex();
			else
				selMonth = cMonth.getSelectedIndex()+cal.get(GregorianCalendar.MONTH);
			numDays(selMonth, selYear);
		}

		if(evt.getActionCommand().equals("create")){
		}
		if(evt.getActionCommand().equals("cancel")){
			dispose();
		}
		repaint();      
	}
	public void numMonths(int year){

		GregorianCalendar cal = new GregorianCalendar();

		int remMon = cMonth.getItemCount();

		for (int i = remMon-1; i >=0; i--)
			cMonth.removeItemAt(i);

		if (year == cal.get(GregorianCalendar.YEAR)){
			for (int i = cal.get(GregorianCalendar.MONTH); i<12; i++)
				cMonth.addItem(months[i]);
		}
		else{
			for (int i = 0; i<12; i++)
				cMonth.addItem(months[i]);
		}
	}

	public void numDays(int month, int year){
		int maxDays;

		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		maxDays = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		int rem = cDay.getItemCount();

		for (int i = rem-1; i >= 0; i--)
			cDay.removeItemAt(i);


		if (year == new GregorianCalendar().get(GregorianCalendar.YEAR) && month == new GregorianCalendar().get(GregorianCalendar.MONTH)){
			for (int i =  new GregorianCalendar().get(GregorianCalendar.DAY_OF_MONTH); i<= maxDays; i++)
				cDay.addItem(i+"");
		}
		else{
			for (int i = 1; i<=maxDays; i++)
				cDay.addItem(i+"");	
		}	
	}
}

//class Inquire extends JDialog implements ActionListener, PropertyChangeListener{
//	
//	public Inquire(JFrame frame){
//		
//
//			}
//
//	
//}
