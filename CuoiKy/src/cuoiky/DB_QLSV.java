package cuoiky;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.border.SoftBevelBorder;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.swing.JRadioButton;

public class DB_QLSV {

	JFrame frame;
	private JTextField txtSearch;
	private JTextField txtID;
	private JTextField txtTen;
	private JTextField txtNgaySinh;
	private JTextField txtDiaChi;
	private JTextField txtQue;
	private JTextField txtSDT;
	private JTable table;
	private JComboBox<CBBItem> cbbLop;
	private DefaultTableModel model;
	private JScrollPane scrollPane;
	private JRadioButton rdBtnNam;
	private JRadioButton rdBtnNu;

	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DB_QLSV window = new DB_QLSV();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	Class<?> cl = Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	Connection conn = DriverManager.getConnection(
			"jdbc:sqlserver://localhost:1433;databaseName=dbQLSV;integratedSecurity=true;useUnicode=true;characterEncoding=UTF-8");

	public DB_QLSV() throws Exception {
		initialize();
		File();
		SetCBB();
		SetNull();
		ReadFile();
	}

	private void initialize() throws Exception {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.GRAY, 2, true));
		frame.getContentPane().add(panel, BorderLayout.NORTH);

		JLabel lblNewLabel = new JLabel("TH\u00D4NG TIN SINH VI\u00CAN");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 35));
		panel.add(lblNewLabel);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.GRAY, 2, true));
		frame.getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(1, 2, 5, 5));

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(Color.GRAY, 2, true));
		panel_1.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		panel_2.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = table.getSelectedRow();
				TableModel model = table.getModel();
				txtID.setText(model.getValueAt(i, 0).toString());
				txtTen.setText(model.getValueAt(i, 1).toString());
				txtNgaySinh.setText(model.getValueAt(i, 3).toString());
				txtDiaChi.setText(model.getValueAt(i, 4).toString());
				txtQue.setText(model.getValueAt(i, 5).toString());
				txtSDT.setText(model.getValueAt(i, 6).toString());
				rdBtnNu.setSelected(true);
				rdBtnNam.setSelected(true);

				if (model.getValueAt(i, 7) == "Nam") {
					rdBtnNu.setSelected(false);
				} else {
					rdBtnNam.setSelected(false);
				}

				for (int j = 0; j < cbbLop.getItemCount(); j++) {
					int temp = ((CBBItem) cbbLop.getItemAt(j)).getIdLop();
					if (temp == Integer.parseInt(model.getValueAt(i, 2).toString())) {
						cbbLop.setSelectedItem(((CBBItem) cbbLop.getItemAt(j)));
					}
				}

			}
		});

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(Color.GRAY, 2, true));
		panel_1.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_3.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new GridLayout(2, 3, 30, 20));

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_4.add(panel_5);
		panel_5.setLayout(new GridLayout(1, 0, 0, 0));

		txtSearch = new JTextField();
		panel_5.add(txtSearch);
		txtSearch.setColumns(10);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ShowData("select * from SinhVien where TenSinhVien like '" + txtSearch.getText() + "%'");

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(btnSearch, "Can not search!", "Message", 2);
				}
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 17));
		panel_5.add(btnSearch);

		JButton btnSort = new JButton("Sort");
		btnSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ShowData("select * from SinhVien order by TenSinhVien asc, IDSinhvien desc");
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(btnSort, "Can not show data!", "Message", 2);

				}
			}
		});
		btnSort.setFont(new Font("Tahoma", Font.PLAIN, 17));
		panel_5.add(btnSort);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_4.add(panel_6);
		panel_6.setLayout(new GridLayout(0, 4, 10, 20));

		JButton btnShow = new JButton("Show");
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					ShowData("select * from SinhVien");
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(btnShow, "Can not show data!", "Message", 2);

				}

			}
		});
		btnShow.setFont(new Font("Tahoma", Font.PLAIN, 17));
		panel_6.add(btnShow);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String gioitinh = "";
				if (rdBtnNu.isSelected())
					gioitinh = "Nữ";
				else
					gioitinh = "Nam";
				try {
					Statement st = conn.createStatement();

					st.execute("insert into SinhVien values (" + txtID.getText() + ",'" + txtTen.getText() + "','"
							+ txtNgaySinh.getText() + "','" + txtDiaChi.getText() + "','" + txtQue.getText() + "','"
							+ txtSDT.getText() + "','" + gioitinh + "',"
							+ ((CBBItem) cbbLop.getSelectedItem()).getIdLop() + ")");
					JOptionPane.showMessageDialog(btnAdd, "Add record success!", "Message", 1);
					ShowData("select * from SinhVien");
					SetNull();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(btnAdd, "Can not add record!", "Message", 2);
				}
			}
		});

		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 17));
		panel_6.add(btnAdd);

		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String gioitinh = "";
					if (rdBtnNu.isSelected())
						gioitinh = "Nữ";
					else
						gioitinh = "Nam";
					Statement st = conn.createStatement();
					st.execute("update SinhVien set TenSinhVien='" + txtTen.getText() + "',NgaySinh='"
							+ txtNgaySinh.getText() + "',DiaChi='" + txtDiaChi.getText() + "',QueQuan='"
							+ txtQue.getText() + "',SDT='" + txtSDT.getText() + "',GioiTinh='" + gioitinh + "',IDLop="
							+ ((CBBItem) cbbLop.getSelectedItem()).getIdLop() + " where IDSinhVien=" + txtID.getText());
					JOptionPane.showMessageDialog(btnEdit, "Edit record success!", "Message", 1);
					ShowData("select * from SinhVien");
					SetNull();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(btnEdit, "Can not edit record!", "Message", 2);
				}
			}
		});
		btnEdit.setFont(new Font("Tahoma", Font.PLAIN, 17));
		panel_6.add(btnEdit);

		JButton btnDel = new JButton("Delete");
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int i = table.getSelectedRow();
					TableModel model = table.getModel();
					String id = model.getValueAt(i, 0).toString();
					System.out.print(id);
					Statement st = conn.createStatement();
					st.execute("delete from SinhVien where IDSinhVien=" + id);
					JOptionPane.showMessageDialog(btnDel, "Delete record success!", "Message", 1);
					ShowData("select * from SinhVien");
					SetNull();

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(btnDel, "Can not delete record!", "Message", 2);
				}
			}
		});
		btnDel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		panel_6.add(btnDel);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_3.add(panel_7, BorderLayout.CENTER);
		panel_7.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("ID Sinh vi\u00EAn");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(41, 11, 103, 20);
		panel_7.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("H\u1ECD v\u00E0 t\u00EAn");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_2.setBounds(41, 53, 103, 20);
		panel_7.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("L\u1EDBp");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_3.setBounds(41, 95, 103, 20);
		panel_7.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("Ng\u00E0y sinh");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_4.setBounds(41, 144, 103, 20);
		panel_7.add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("\u0110\u1ECBa ch\u1EC9");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_5.setBounds(41, 189, 103, 20);
		panel_7.add(lblNewLabel_5);

		JLabel lblNewLabel_6 = new JLabel("Qu\u00EA qu\u00E1n");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_6.setBounds(41, 239, 103, 20);
		panel_7.add(lblNewLabel_6);

		JLabel lblNewLabel_7 = new JLabel("S\u1ED1 \u0111i\u1EC7n tho\u1EA1i");
		lblNewLabel_7.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_7.setBounds(41, 294, 103, 20);
		panel_7.add(lblNewLabel_7);

		JLabel lblNewLabel_8 = new JLabel("Gi\u1EDBi t\u00EDnh");
		lblNewLabel_8.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_8.setBounds(41, 336, 103, 20);
		panel_7.add(lblNewLabel_8);

		txtID = new JTextField();
		txtID.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		txtID.setBounds(154, 13, 259, 20);
		panel_7.add(txtID);
		txtID.setColumns(10);

		txtTen = new JTextField();
		txtTen.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		txtTen.setBounds(154, 55, 259, 20);
		panel_7.add(txtTen);
		txtTen.setColumns(10);

		txtNgaySinh = new JTextField();
		txtNgaySinh.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		txtNgaySinh.setBounds(154, 146, 259, 20);
		panel_7.add(txtNgaySinh);
		txtNgaySinh.setColumns(10);

		txtDiaChi = new JTextField();
		txtDiaChi.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		txtDiaChi.setBounds(154, 191, 259, 20);
		panel_7.add(txtDiaChi);
		txtDiaChi.setColumns(10);

		txtQue = new JTextField();
		txtQue.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		txtQue.setBounds(154, 241, 259, 20);
		panel_7.add(txtQue);
		txtQue.setColumns(10);

		txtSDT = new JTextField();
		txtSDT.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		txtSDT.setBounds(154, 296, 259, 20);
		panel_7.add(txtSDT);
		txtSDT.setColumns(10);

		cbbLop = new JComboBox<CBBItem>();
		cbbLop.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		cbbLop.setBounds(154, 96, 259, 22);
		panel_7.add(cbbLop);

		rdBtnNu = new JRadioButton("Nữ");
		rdBtnNu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdBtnNu.isSelected()) {
					rdBtnNam.setSelected(false);
				}
			}
		});
		rdBtnNu.setBounds(153, 337, 109, 23);
		panel_7.add(rdBtnNu);

		rdBtnNam = new JRadioButton("Nam");
		rdBtnNam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdBtnNam.isSelected()) {
					rdBtnNu.setSelected(false);
				}
			}
		});
		rdBtnNam.setBounds(278, 337, 109, 23);
		panel_7.add(rdBtnNam);

		JLabel lblNewLabel_9 = new JLabel("yy-mm-dd");
		lblNewLabel_9.setBounds(154, 166, 74, 14);
		panel_7.add(lblNewLabel_9);
	}

	public void SetCBB() throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from Lop");
		while (rs.next()) {
			String tenLop = rs.getString("TenLop");
			int idLop = rs.getInt("IDLop");
			cbbLop.addItem(new CBBItem(idLop, tenLop));
		}
	}

	public void SetNull() {
		txtID.setText(null);
		txtTen.setText(null);
		txtNgaySinh.setText(null);
		txtDiaChi.setText(null);
		txtQue.setText(null);
		txtSDT.setText(null);
		cbbLop.setSelectedIndex(-1);
		rdBtnNam.setSelected(false);
		rdBtnNu.setSelected(false);
	}

	public void SetColumn() {
		model = new DefaultTableModel();
		model.addColumn("ID");
		model.addColumn("Họ và tên");
		model.addColumn("ID lớp");
		model.addColumn("Ngày sinh");
		model.addColumn("Địa chỉ");
		model.addColumn("Quê quán");
		model.addColumn("Số điện thoại");
		model.addColumn("Giới tính");
	}

	public void ShowData(String select) throws SQLException {
		SetColumn();
		Statement st;
		st = conn.createStatement();
		ResultSet rs = st.executeQuery(select);

		while (rs.next()) {
			int id = rs.getInt("IDSinhVien");
			String ten = rs.getString("TenSinhVien");
			int lop = rs.getInt("IDLop");
			String n = rs.getString("NgaySinh");
			String d = rs.getString("DiaChi");
			String q = rs.getString("QueQuan");
			String s = rs.getString("SDT");
			String g = rs.getString("GioiTinh");

			model.addRow(new Object[] { id, ten, lop, n, d, q, s, g });
		}
		table.setModel(model);
		scrollPane.setViewportView(table);
		SetNull();
	}

	public void File() throws SQLException {
		Statement st;
		st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from SinhVien");
		List data = new ArrayList();
		while (rs.next()) {
			int id = rs.getInt("IDSinhVien");
			String ten = rs.getString("TenSinhVien");
			int lop = rs.getInt("IDLop");
			String ngaysinh = rs.getString("NgaySinh");
			String diachi = rs.getString("DiaChi");
			String quequan = rs.getString("QueQuan");
			String sdt = rs.getString("SDT");
			String gioitinh = rs.getString("GioiTinh");
			data.add(id + " " + ten + " " + lop + " " + ngaysinh + " " + diachi + " " + quequan + " " + sdt + " "
					+ gioitinh);

		}
		writeToFile(data, "DSSinhVien.txt");
		rs.close();
		st.close();
	}

	private static void writeToFile(List<?> list, String path) {
		BufferedWriter out = null;
		try {
			File file = new File(path);
			FileOutputStream output = new FileOutputStream(file);
			out = new BufferedWriter(new FileWriter(file, true));

			for (Object s : list) {
				out.write((String) s);
				out.newLine();

			}
			out.close();
			// System.out.print("ok");
		} catch (IOException e) {
		}
	}

	public void ReadFile() throws IOException {
		File file = new File("Input.txt");
		BufferedReader br = new BufferedReader(new FileReader("Input.txt"));
		String line;
		Statement st;
		int LineError = 0;
		PrintWriter writer = new PrintWriter("Error.txt");
		while ((line = br.readLine()) != null) {
			String id = line.substring(0, 2).trim();
			String ten = line.substring(2, 15).trim();
			String lop = line.substring(15, 17).trim();
			String ngaysinh = line.substring(17, 28).trim();
			String diachi = line.substring(28, 36).trim();
			String quequan = line.substring(36, 43).trim();
			String sdt = line.substring(43, 54).trim();
			String gioitinh = line.substring(54).trim();
			try {
				st = conn.createStatement();
				LineError++;
				st.execute("insert into SinhVien values (" + id + ",'" + ten + "','" + ngaysinh + "','" + diachi + "','"
						+ quequan + "','" + sdt + "','" + gioitinh + "'," + lop + ")");
			} catch (SQLException e1) {
				writer.write("Dong loi: " + LineError + "\n");
			}
			
		}
		writer.close();
		System.out.println("OK");
	}

}

class CBBItem {
	private String tenLop;
	private int idLop;

	@Override
	public String toString() {
		return this.getTenLop();
	}

	public CBBItem(int id, String ten) {
		this.idLop = id;
		this.tenLop = ten;
	}

	public String getTenLop() {
		return tenLop;
	}

	public void setTenLop(String tenLop) {
		this.tenLop = tenLop;
	}

	public int getIdLop() {
		return idLop;
	}

	public void setIdLop(int idLop) {
		this.idLop = idLop;
	}
}
