package RGB;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class RGB extends Thread{
	
	public static void main(String[] args) throws IOException, InterruptedException{
		
		CountDownLatch latch = new CountDownLatch(4);
		Scanner scanner = new Scanner(new File("src/RGB/input.txt"));
		PrintWriter pw = new PrintWriter(new File("src/RGB/output.csv"));
        StringBuilder sb = new StringBuilder();
        int totalLines = totalLines(scanner);
        scanner.close();
        int scanLinesPerThread = totalLines/4;
        
        //firstThread
        int startLine = 0;
        int endLine = startLine + scanLinesPerThread;
		Thread t1 = new Thread(new RGBExecutable(sb,startLine,endLine,latch));
		t1.start();
		//secondThread
		startLine = scanLinesPerThread + 1;
		endLine = scanLinesPerThread * 2;
		Thread t2 = new Thread(new RGBExecutable(sb, startLine,endLine,latch));
		t2.start();
		//thirdThread
		startLine = (scanLinesPerThread * 2) + 1;
		endLine = scanLinesPerThread * 3;
		Thread t3 = new Thread(new RGBExecutable(sb,startLine,endLine,latch));
		t3.start();
		//fourthThread
		startLine = (scanLinesPerThread * 3) + 1;
		endLine = totalLines;
		Thread t4 = new Thread(new RGBExecutable(sb,startLine,endLine,latch));
		t4.start();
		latch.await();
		//create results.csv to put inside
		pw.write(sb.toString());
		pw.close();
		
	} 
	
	
	public static int totalLines(Scanner scanner){
		int count = 0;
		while(scanner.hasNextLine()){
            //process each line
			scanner.nextLine();
			count ++;    
		}
		return count;
	}
	
	
}
