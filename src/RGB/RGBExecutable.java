package RGB;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

public class RGBExecutable implements Runnable{
	StringBuilder sb; 
	int startLine;
	int endLine;
	CountDownLatch countDownLatch;
	
	public RGBExecutable(StringBuilder stringBuilder, int firstLine,int lastLine, CountDownLatch latch) {
		sb = stringBuilder;
		startLine = firstLine;
		endLine = lastLine;
		countDownLatch = latch;
	}
	
	
	public void run() {
		try {
			Scanner s = new Scanner(new File("src/RGB/input.txt"));
			int count = 0;
			while(s.hasNextLine()){
	            //process each line
	            String line = s.nextLine();
	    		//find 3 most prevalent colors of the file, RGB
	            if(startLine <= count &&  count <= endLine) {
	            	findPrevalentColors(line,sb);
	            }
	            count ++;
	        }
			s.close();
			countDownLatch.countDown();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void findPrevalentColors(String url,StringBuilder sb) throws IOException {
		URL urlInput = new URL(url);
	    BufferedImage image = ImageIO.read(urlInput);
        int height = image.getHeight();
        int width = image.getWidth();
        Map<Integer,Integer> m = new ConcurrentHashMap<Integer,Integer>();
        for(int i=0; i < width ; i++){
            for(int j=0; j < height ; j++){
                int rgb = image.getRGB(i, j);
                Integer counter = (Integer) m.get(rgb);   
                if (counter == null)
                    counter = 0;
                counter++;                                
                m.put(rgb, counter); 
            }
        }        
        getMostPrevalentColour(m, sb, url);
    }


    public  void getMostPrevalentColour(Map<Integer,Integer> m, StringBuilder sb, String url) {
    	List<Map.Entry<Integer, Integer>> list = new LinkedList(m.entrySet());
    	//parallel sorting
    	list = list.parallelStream().sorted((o1, o2) -> 
    		(((Map.Entry<Integer,Integer>)(o1)).getValue().compareTo(((Map.Entry<Integer,Integer>) (o2)).getValue()))).collect(Collectors.toList());   
        Map.Entry<Integer,Integer> most = (Map.Entry<Integer,Integer>)list.get(list.size()-1);
        Map.Entry<Integer,Integer> secondMost = (Map.Entry<Integer,Integer>)list.get(list.size()-2);
        Map.Entry<Integer,Integer> thirdMost = (Map.Entry<Integer,Integer>)list.get(list.size()-3);
        int[] rgb1 = getRGBArr((Integer)most.getKey());
        int[] rgb2 = getRGBArr((Integer)secondMost.getKey());
        int[] rgb3 = getRGBArr((Integer)thirdMost.getKey());
		String mostPrevalentColor = String.format("#%02x%02x%02x", rgb1[0], rgb1[1], rgb1[2]);  
		String secondMostPrevalentColor = String.format("#%02x%02x%02x", rgb2[0], rgb2[1], rgb2[2]);
		String thirdMostPrevalentColor = String.format("#%02x%02x%02x", rgb3[0], rgb3[1], rgb3[2]);
		setStringBuilder(sb, url,mostPrevalentColor,secondMostPrevalentColor,thirdMostPrevalentColor);
    }    

    private void setStringBuilder(StringBuilder sb, String url, 
    		String mostPrevalentColor, String secondMostPrevalentColor, String thirdMostPrevalentColor) {
    	sb.append(url);
        sb.append(',');
        sb.append(mostPrevalentColor);
        sb.append(',');
        sb.append(secondMostPrevalentColor);
        sb.append(',');
        sb.append(thirdMostPrevalentColor);
        sb.append('\n');
		
	}

	public static int[] getRGBArr(int pixel) {
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return new int[]{red,green,blue};
    }
	
	
}
