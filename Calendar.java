import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Calendar extends BasicLayout implements ActionListener {

	JPanel slctM, slctY, screen, evtList, bttn, bttnAndEvt;
	JComboBox years;
	JComboBox months;
	JButton addEvt, edit;
	JLabel lblMonth, lblYear;
	JLabel title;
	JLabel[][] noEvt;
	JTable calendar;
	DefaultTableModel mCalendar; 
	JScrollPane sCalendar, sEvt;
	int pdYear, pdMonth, pdDay, selYear, selMonth;
	String[] month;
	JPopupMenu ss;
	Events qwe;
	GridBagConstraints gc = new GridBagConstraints();
	GridBagLayout layout = new GridBagLayout();
	JPanel[][] cards;

	public Calendar(){

		Font f = new Font("Calibri(Body)", Font.BOLD, 14);
		lblMonth = new JLabel ("Change Month: ");
		lblYear = new JLabel ("Change year: ");
		years = new JComboBox();
		title = new JLabel();

		month = new String[]{"January", "February", "March","April", "May", "June", "July","August","September", "October", "November", "December"};
		months = new JComboBox(month);

		years.setActionCommand("Change Year");
		months.setActionCommand("Change Month");

		lblMonth.setFont(f);
		lblYear.setFont(f);
		years.setFont(f);
		months.setFont(f);

		slctM = new JPanel();
		slctM.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		slctY = new JPanel();
		slctY.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));

		slctM.add(lblMonth);
		slctY.add(lblYear);
		slctY.add(years);
		slctM.add(months);

		getContentPane().setLayout(null);
		getContentPane().add(slctM);
		getContentPane().add(slctY);
		slctM.setBounds(60, 262, 220, 30);
		slctY.setBounds(645, 262, 175, 30);

		mCalendar = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
		calendar = new JTable(mCalendar);
		sCalendar = new JScrollPane(calendar);
		sCalendar.setPreferredSize(new Dimension(400,300));

		addEvt = new JButton("Add Event");
		addEvt.setActionCommand("add");
		addEvt.addActionListener(this);
		edit = new JButton("Edit");

		bttn = new JPanel();
		bttn.add(addEvt);
		bttn.add(edit);

		evtList = new JPanel();

		sEvt = new JScrollPane(evtList);
		sEvt.setPreferredSize(new Dimension(370,325));
		sEvt.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		sEvt.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		evtList.setBackground(Color.WHITE);

		bttnAndEvt = new JPanel();
		bttnAndEvt.setLayout(new BoxLayout(bttnAndEvt,BoxLayout.Y_AXIS));
		bttnAndEvt.add(sEvt);
		bttnAndEvt.add(bttn);

		screen = new JPanel();
		screen.setLayout(new BoxLayout(screen, BoxLayout.X_AXIS));
		screen.add(sCalendar);
		screen.add(Box.createHorizontalGlue());
		screen.add(bttnAndEvt);

		getContentPane().add(screen);
		screen.setBounds(12, 360, 875, 325);

		GregorianCalendar cal = new GregorianCalendar(); 
		pdDay = cal.get(GregorianCalendar.DAY_OF_MONTH); 
		pdMonth = cal.get(GregorianCalendar.MONTH); 
		pdYear = cal.get(GregorianCalendar.YEAR); 
		selMonth = pdMonth; 
		selYear = pdYear;

		String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; 
		for (int i=0; i<7; i++){
			mCalendar.addColumn(headers[i]);
		}

		calendar.getTableHeader().setResizingAllowed(false);
		calendar.getTableHeader().setReorderingAllowed(false);

		calendar.setColumnSelectionAllowed(true);
		calendar.setRowSelectionAllowed(true);
		calendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		calendar.setRowHeight(50);
		mCalendar.setColumnCount(7);
		mCalendar.setRowCount(6);

		for (int i = pdYear-10; i<=pdYear+10; i++){
			years.addItem(i+"");
		}		
		months.setSelectedIndex(selMonth);
		years.setSelectedItem(selYear+"");

		years.addActionListener(this);
		months.addActionListener(this);

		evtList.setLayout(new CardLayout());

		displayCalendar(selMonth,selYear);

		cards = new JPanel[10][12];
		noEvt = new JLabel[10][12];

		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 12; j++){
				cards[i][j] = new JPanel();
				cards[i][j].setBackground(Color.WHITE);

				noEvt[i][j] = new JLabel("There Are No Events For This Month     ");
				cards[i][j].add(noEvt[i][j]);
			}
		}
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 12; j++){
				evtList.add(cards[i][j], i +" " +j);
			}
		}

		this.addMouseListener (new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				edit();
			}
		});
	}
	
	public void edit(){
		
	}

	class calendarColour extends DefaultTableCellRenderer{
		public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
			super.getTableCellRendererComponent(table, value, selected, focused, row, column);
			if (column == 0 || column == 6){ 
				setBackground(new Color(255, 220, 220));
			}
			else{
				setBackground(new Color(255, 255, 255));
			}
			if (value != null){
				if (Integer.parseInt(value.toString()) == pdDay && selMonth == pdMonth && selYear == pdYear){ //Today
					setBackground(new Color(220, 220, 255));
				}
				for (int i = 0; i < calEvt.size(); i++){
					Events temp = new Events();
					temp = calEvt.get(i);
					if (Integer.parseInt(value.toString()) == temp.getDay() && selMonth == temp.getMonth() && selYear == temp.getYear()){ //Today
						setBackground(new Color(127,255,212));
					}
				}
			}
			setBorder(null);
			setForeground(Color.black);
			return this;  
		}
	}


	public void displayCalendar(int gMonth, int gYear){

		int numDays, som;

		title.setText(month[gMonth] + " " + gYear);
		Font f1 = new Font("Calibri(Body)", Font.BOLD, 28);
		FontMetrics fm = getFontMetrics(f1);
		getContentPane().add(title);
		title.setFont(f1);
		title.setBounds(450-(fm.stringWidth(title.getText())/2),25-(fm.getHeight()/2)+300,fm.stringWidth(title.getText()),fm.getHeight());

		for (int i=0; i<6; i++){
			for (int j=0; j<7; j++){
				mCalendar.setValueAt(null, i, j);
			}
		}

		GregorianCalendar cal = new GregorianCalendar(gYear, gMonth, 1);
		numDays = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);

		for (int i=1; i<=numDays; i++){
			int row = new Integer((i+som-2)/7);
			int column  =  (i+som-2)%7;
			mCalendar.setValueAt(i, row, column);
		}

		CardLayout cl = (CardLayout)(evtList.getLayout());
		cl.show(evtList, (gYear-pdYear) + " " + gMonth);

		calendar.setDefaultRenderer(calendar.getColumnClass(0), new calendarColour());
	}
	public void actionPerformed( ActionEvent evt){      

		if (evt.getActionCommand().equals("Change Year"))
			selYear = Integer.parseInt(years.getSelectedItem()+"");
		if (evt.getActionCommand().equals("Change Month"))
			selMonth = (months.getSelectedIndex());
		displayCalendar(selMonth, selYear);

		if (evt.getActionCommand().equals("add")){
			calEvt.add(new Events(null));
			calEvt.get(calEvt.size()-1).setVisible(true);

			gc.ipady = 105;
			gc.ipadx = evtList.getWidth()-1;
			gc.gridx = 0;
			gc.gridy = GridBagConstraints.RELATIVE;
			gc.gridwidth = GridBagConstraints.REMAINDER;
			gc.gridheight = 1;
			gc.weightx = 0.5;
			gc.weighty = 0.5;
			gc.insets = new Insets(0, 0, 15, 15);
			gc.anchor = GridBagConstraints.NORTHWEST;
			gc.fill = GridBagConstraints.NONE;

			JPanel pane = new JPanel();
			pane = cards[calEvt.get(calEvt.size()-1).getYear()-pdYear][calEvt.get(calEvt.size()-1).getMonth()];
			pane.setLayout(layout);
			if(!calEvt.get(calEvt.size()-1).getTitle().equals("")){
				pane.remove(noEvt[calEvt.get(calEvt.size()-1).getYear()-pdYear][calEvt.get(calEvt.size()-1).getMonth()]);
				pane.add(calEvt.get(calEvt.size()-1).draw(pane),gc);
			}
			else
				calEvt.remove(calEvt.size()-1);

			displayCalendar(selMonth, selYear);
		}
		repaint();      
	}    

	public static void main(String[] args) {
		new Calendar();

	}
}
