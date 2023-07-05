import java.nio.file.Files;
import java.nio.file.Path;
import java.io.*;
import java.util.Scanner;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class SimpleHashing
{
	public static int count;
	public static final int[] Address_list = new int[100];
	public static final String[] usn_list = new String[100];
	//public static Scanner s = new Scanner(System.in);
	public static void main(String[] args)throws IOException {

        SimpleHashing obj = new SimpleHashing();


        //obj.insert(22,"xyz",4,3.5,6);
        //obj.requestLeave(22,2,0.5);
		

        //SimpleHashing obj = new SimpleHashing();
        boolean exit = false;
        while (!exit) {
            String choice = JOptionPane.showInputDialog(
                    "Leave Management System using Simple Hashing and B-Plus Trees\n\nMenu:\n1. Add Employee\n2. Increment Leaves\n3. Search Employee\n4. Request Leave\n5. Display Employees and Remaining Leaves\n6.About Project\n7. Exit\n\nEnter your choice:");

            switch (choice) {
                case "1":
                    int employeeId = Integer.parseInt(JOptionPane.showInputDialog("Enter employee ID:"));

                    String name = JOptionPane.showInputDialog("Enter employee name:");
                    double cl = Double.parseDouble(JOptionPane.showInputDialog("Enter casual leaves:"));
                    double sl = Double.parseDouble(JOptionPane.showInputDialog("Enter sick leaves:"));
                    double pl = Double.parseDouble(JOptionPane.showInputDialog("Enter personal leaves:"));

                    boolean employeeAdded = obj.insert(employeeId, name, cl, sl, pl);
                    if (employeeAdded) {
                        JOptionPane.showMessageDialog(null, "Employee added successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Employee already exists.");
                    }

                    break;
                case "2":
                    obj.increment();
                    JOptionPane.showMessageDialog(null, "Leaves incremented successfully");

                    break;
                case "3":
                    int employeeIdToSearch = Integer.parseInt(JOptionPane.showInputDialog("Enter employee ID to search:"));
                    if (employeeIdToSearch >= 0) {
                        String employeeDetails = obj.search((employeeIdToSearch));
                        if (employeeDetails != "") {
                            JOptionPane.showMessageDialog(null, "Employee Details:\n\n" + employeeDetails + "\n");
                        } else {
                            JOptionPane.showMessageDialog(null, "Employee not found.\n");
                        }
                    }
                    break;

                case "4":
                    int employeeIdToRequestLeave = Integer.parseInt(JOptionPane
                            .showInputDialog("Enter employee ID to request leave:"));
                    if (employeeIdToRequestLeave >= 0) {
                        //boolean employeeExists = bPlusTree.checkEmployeeExists(employeeIdToRequestLeave);
                        if (true) {
                            String[] leaveOptions = {"Casual", "Sick", "Personal"};
                            String leaveType = (String) JOptionPane.showInputDialog(
                                    null,
                                    "Select leave type:",
                                    "Leave Type",
                                    JOptionPane.PLAIN_MESSAGE,
                                    null,
                                    leaveOptions,
                                    leaveOptions[0]);
                            if (leaveType != null) {
                                String leavesRequiredInput = JOptionPane
                                        .showInputDialog("Enter the number of leaves required:");
                                if (leavesRequiredInput != null) {
                                    try {
                                        double leavesRequired = Double.parseDouble(leavesRequiredInput);
                                        int type = 1;
                                        switch (leaveType){
                                            case "Casual": type = 1;break;
                                            case "Sick"  : type = 2; break;
                                            case "Personal" : type = 3; break;
                                        }
										System.out.println(leaveType + type);
                                        boolean leaveRequested = obj.requestLeave(employeeIdToRequestLeave, type, leavesRequired);
                                        if (leaveRequested) {
                                            //updateRemainingLeave(employeeIdToRequestLeave, leaveType, leavesRequired);
                                            JOptionPane.showMessageDialog(null, "Leave granted.");
                                        } else {
                                            JOptionPane.showMessageDialog(null,
                                                    "Insufficient remaining leaves or leave already requested for the given employee.");
                                        }
                                    } catch (NumberFormatException e) {
										e.printStackTrace(System.out);
                                        JOptionPane.showMessageDialog(null,"Invalid input for number of leaves required.");
                                    }
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Employee does not exist.");
                        }
                    }
                    break;
                case "5":
                    String displayText = obj.displayAll();;
                     JOptionPane.showMessageDialog(null, displayText);

                    JOptionPane pane = new JOptionPane("Show", JOptionPane.INFORMATION_MESSAGE);
                    pane.setPreferredSize(new Dimension(800, 600)); // Set the desired size
                    JDialog dialog = pane.createDialog(null, "Employee Information");
                    dialog.setVisible(true);
                    break;
				
				case "6":
					String credits = "";
					credits += "This Project, 'Leave Management System using B-Plus Trees and Simple Hashing' was developed";
					credits += "\nby Ajay V Kamath and Atharv Kulkarni under the guidance of Dr. Veena N, belonging to ISE Dept.";
					credits += "\nof BMS Institute of Technology and Management as a part of Mini Project for File Structures\nLaboratory";
					JOptionPane.showMessageDialog(null, credits);
					break;
                case "7":
                    exit = true;
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice.");
                    break;
            }

        }
    }
	

	public boolean insert(int empId, String name , double cl, double sl, double el)throws IOException,FileNotFoundException{
				
		int flag = 0;
		long pos;
		int empid_temp;
		RandomAccessFile file = new RandomAccessFile("details.txt", "rw");
		pos = file.getFilePointer();
		String s ;
		while((s = file.readLine())!=null)
		{
			String[] result = s.split("\\|");
			empid_temp = Integer.parseInt(result[0]);
			if (empid_temp == empId) 
			{
				flag = -1;
				return false;

			}
			if (empid_temp > empId) 
			{
				//System.out.println("Flag 1 " + empid_temp + " " + empId);
				flag = 1;
				break;
			}

			pos=file.getFilePointer();
		}
		
		if (flag == -1){
			//empid exists
			System.out.println("Impossible");
		}
		else{
			

			String line = toFixedLength(empId+"|"+name+"|"+cl+"|"+sl+"|"+el+"|");
			file.seek(pos);
			String tempAdd = "";
			if (flag == 1){
				
				while((s = file.readLine())!=null)
				{
					tempAdd += s + "\n";
				}
			}
			file.seek(pos);
			file.write(line.getBytes());
			file.write(tempAdd.getBytes());
			file.close();
			
			try
			{
				Path fileName= Path.of("input.dat");
				String strAppend = Files.readString(fileName);
				String filename= "input.dat";
				FileWriter fw = new FileWriter(filename,false); //the true will append the new data
				fw.write(empId+" " + strAppend);//appends the string to the file
				fw.close();
				return true;
			}
			catch(IOException ioe)
			{
				System.err.println("IOException: " + ioe.getMessage());
				return false;
			}
		}
        return true;
	}
	
	public void increment()throws IOException{
		String writeBack = "" ;
		RandomAccessFile file = new RandomAccessFile("details.txt", "rw");
		
		String s;
		while((s = file.readLine())!=null)
		{
			String[] line = s.split("\\|");
			String empId = line[0];
			String name = line[1];
			float cl = Float.parseFloat(line[2])+1;
			float sl = Float.parseFloat(line[3])+1;
			float pl = Float.parseFloat(line[4])+1;
			writeBack += toFixedLength(empId+"|"+name+"|"+cl+"|"+sl+"|"+pl+"|");
		}
		file.seek(0);
		
		file.write(writeBack.getBytes());
		file.close();
		
	}
	
	public boolean requestLeave(int empId, int type, double days) throws FileNotFoundException, IOException{
		if (type < 1 || type > 3){
			System.out.println("Invalid type");
			return false;
		}
		String arg [] = new String[] {"input.dat", "3", Integer.toString(empId), "output1.dat"};
		
		BPlus.main(arg);
		//read 3rd line
		RandomAccessFile file1 = new RandomAccessFile("output1.dat", "r");
		String s, writeBack = "";
		s = file1.readLine();
		s = file1.readLine();
		s = file1.readLine();//3rd line
		int lineNo = Integer.parseInt(s);
		file1.close();
		
		RandomAccessFile file = new RandomAccessFile("details.txt", "rw");
		for (int i = 1; i < lineNo; i++){
			s = file.readLine();
			
		}
		long pos = file.getFilePointer();
		s = file.readLine();
		String[] line = s.split("\\|");
		String file_empId = line[0];
		String name = line[1];
		float cl = Float.parseFloat(line[2]);
		float sl = Float.parseFloat(line[3]);
		float pl = Float.parseFloat(line[4]);
		
		if (type == 1){
			if (days > cl) return false;
			cl -= days;
		}
		else if (type == 2){
			if (days > sl) return false;
			sl -= days;
		}
		else if (type == 3){
			if (days > pl) return false;
			pl -= days;
		}
		file.seek(pos);
		writeBack += toFixedLength(file_empId+"|"+name+"|"+cl+"|"+sl+"|"+pl+"|");
		file.write(writeBack.getBytes());
		file.close();
		return true;
		
		
		
	}
	
	public String search(int empId)throws IOException

	{	
		String keys;
		RandomAccessFile inpFile = new RandomAccessFile("input.dat","r");
		keys = inpFile.readLine();
		String [] keys_arr = keys.split(" ");
		int flag = 0;
		for (String k : keys_arr){
			if (Integer.parseInt(k) == empId){
				flag  = 1;
				System.out.println("FLAG");
				break;
			}
		}
		if (flag == 0) return "";
		inpFile.close();
		String arg [] = new String[] {"input.dat", "3", Integer.toString(empId), "output1.dat"};
		
		BPlus.main(arg);
		//read 3rd line
		RandomAccessFile file1 = new RandomAccessFile("output1.dat", "r");
		String s, writeBack = "";
		s = file1.readLine();
		s = file1.readLine();
		s = file1.readLine();//3rd line
		int lineNo = Integer.parseInt(s);
		file1.close();
		
		RandomAccessFile file = new RandomAccessFile("details.txt", "r");
		for (int i = 1; i < lineNo; i++){
			s = file.readLine();
			
		}
		s = file.readLine();
		String[] line = s.split("\\|");
		String file_empId = line[0];
		String name = line[1];
		float cl = Float.parseFloat(line[2]);
		float sl = Float.parseFloat(line[3]);
		float pl = Float.parseFloat(line[4]);
		file.close();
		writeBack += "Employee ID: " + empId+"\nEmployee name: "+name+"\nRemaining CL: "+cl;
			writeBack += "\nRemaining SL: "+sl+"\nRemaining PL: "+pl;
		
		return writeBack;

	}
	
	public String displayAll() throws FileNotFoundException, IOException{
		String ret = "";
		RandomAccessFile file = new RandomAccessFile("details.txt", "r");
		
		String s;
		while((s = file.readLine())!=null)
		{
			String[] line = s.split("\\|");
			String empId = line[0];
			String name = line[1];
			float cl = Float.parseFloat(line[2])+1;
			float sl = Float.parseFloat(line[3])+1;
			float pl = Float.parseFloat(line[4])+1;
			ret += "\nEmployee ID: " + empId;
			ret += "\nEmployee Name: " + name;
			ret += "\nRemaining CL: " + cl;
			ret += "\nRemaining SL: " + sl;
			ret += "\nRemaining PL: " + pl;
			ret += "\n-------------------------\n";
						
		}
		
		file.close();
		return ret;
		
	}
	
	public String toFixedLength(String varLengthStr){
		String b = varLengthStr;
		int len = 32;
		int le = b.length();
		String s1 = "-";
		if(le<50)
		{
			for(int j=le;j<=50;j++)
			b = b.concat(s1);
			
		}
		b += "|\n";
		return b;
		
	}
	
	
	

}