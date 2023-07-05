import java.nio.file.Files;
import java.nio.file.Path;
import java.io.*;
import java.util.Scanner;

public class SimpleHashing
{
	public static int count;
	public static final int[] Address_list = new int[100];
	public static final String[] usn_list = new String[100];
	public static Scanner s = new Scanner(System.in);
	public static void main(String[] args)throws IOException
	{
	SimpleHashing obj = new SimpleHashing();
	
	
	obj.insert(22,"xyz",4,3.5,6);
	//obj.requestLeave(7,1,1.0);
	
	
	/*while(true)
	{
	System.out.println("\nPlease enter your choice:");
	ch = s.nextInt();
	s.nextLine();
	switch(ch)
	{
	case 1:
	obj.insert();
	break;
	case 2:
	obj.search();
	break;
	case 3:
	obj.remove();
	break;
	case 4:
	System.out.println("Do you want to exit? (Y/N)");
	if(s.next().equalsIgnoreCase("y"))
	{
	System.out.println("Program Ended");
	System.exit(0);
	}break;
	default:
	System.out.println("Invalid Option");
	}*/
	
	}
	
	public void create_index()throws IOException,ArrayIndexOutOfBoundsException
	{
		count = -1;
		long pos;
		RandomAccessFile file = new RandomAccessFile("details.txt", "r");
		pos = file.getFilePointer();
		String s ;
		while((s = file.readLine())!=null)
		{
		String[] result = s.split("\\|");
		count++;
		usn_list[count] = result[0];
		Address_list[count] = (int)pos;
		pos=file.getFilePointer();
		}
		file.close();
		sort_index();
	}
	public void sort_index()throws IOException
	{
		for(int i=0;i<=count;i++)
		{
			for(int j=i+1;j<=count;j++)
			{
				if(usn_list[i].compareTo(usn_list[j])>0)
				{
					String temp = usn_list[i];
					usn_list[i] = usn_list[j];
					usn_list[j] = temp;
					int temp1 = Address_list[i];
					Address_list[i]=Address_list[j];
					Address_list[j]=temp1;
				}
			}
		}
	}
	public void insert(int empId, String name , double cl, double sl, double el)throws IOException,FileNotFoundException{
				
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
				break;
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
			

			String line = empId+"|"+name+"|"+cl+"|"+sl+"|"+el+"|"+"\n";
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
			}
			catch(IOException ioe)
			{
				System.err.println("IOException: " + ioe.getMessage());
			}
		}
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
			writeBack += empId+"|"+name+"|"+cl+"|"+sl+"|"+pl+"|"+"\n";
		}
		file.seek(0);
		file.write(writeBack.getBytes());
		file.close();
		
	}
	
	public void requestLeave(int empId, int type, double days){
		String arg [] = new String[] {"input.dat", "3", Integer.toString(empId), "output1.dat"};
		
		BPlus.main(arg);
		
	}
	
	public void search()throws IOException
	{
	int pos;
	System.out.println("Enter the usn to be searched");
	String key = s.nextLine();
	pos = search_index(key);
	if(pos!=-1)
	display_record(pos);
	else
	System.out.println("Record not found");

	}
	public int search_index(String key)
	{
	int low = 0, high = count, mid = 0;
	while(low <= high)
	{
	mid = (low + high)/2;
	if(usn_list[mid].equals(key))
	return mid;
	if(usn_list[mid].compareTo(key)>0)
	high = mid - 1;
	if(usn_list[mid].compareTo(key)<0)
	low = mid + 1;

	}
	return -1;
	}
	public void display_record(int pos)throws IOException
	{
	RandomAccessFile file = new RandomAccessFile("details.txt", "r");
	int address = Address_list[pos];
	String usn="",sem="",branch="",name="";
	file.seek(address);
	String s = file.readLine();
	while(s!=null)
	{
	String[] result = s.split("\\|");
	usn = result[0];
	name = result[1];
	sem = result[2];
	branch = result[3];
	System.out.println("\nRecord Details");
	System.out.println("USN: " + usn);
	System.out.println("Name: " + name);
	System.out.println("Sem: " + sem);
	System.out.println("Branch: " + branch);
	break;
	}
	file.close();
	}
	public void remove()throws IOException
	{
	System.out.println("Enter the key to be deleted");
	String key = s.nextLine();
	int pos = search_index(key);
	if(pos != -1)
	{
	delete_from_file(pos);
	create_index();
	}
	else
	System.out.println("Record not found");

	}
	public void delete_from_file(int pos)throws IOException
	{
	display_record(pos);
	RandomAccessFile file = new RandomAccessFile("details.txt", "rw");
	System.out.println("Are you sure you want to delete? (Y/N)");
	String ch = s.nextLine();
	if(ch.equalsIgnoreCase("y"))
	{
	int address= Address_list[pos];
	String del_ch="*";
	file.seek(address);
	file.writeBytes(del_ch);
	System.out.println("Record is deleted");
	}
	file.close();
	}
}