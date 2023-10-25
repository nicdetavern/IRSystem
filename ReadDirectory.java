/*By Nicolas D.
 * Iterates through raw data, deprecated and included in the main IRSystem java file
 * */
import java.util.*;
import java.io.*;

class ReadDirectory
{
  public static void main(String[]  args)
  {
    try
    {
      System.out.print("Enter name of a directory> ");
      Scanner scan = new Scanner(System.in);
      File dir = new File(scan.nextLine());
      File[] fileList = dir.listFiles();

      Map<String, Integer> frequency = new HashMap<>();

      
      System.out.print("Enter name of output file> ");
      File outputdir = new File(scan.nextLine());
      FileWriter fw = new FileWriter(outputdir);

      for (File f : fileList)
      {
        Scanner sc = new Scanner(f);
        while (sc.hasNextLine())
        {
          StringTokenizer st = new StringTokenizer(sc.nextLine());
          while (st.hasMoreTokens())
          {
            String word = st.nextToken();
            word = tokenize(word);
            
            if (frequency.containsKey(word)) {
            	frequency.put(word, frequency.get(word) + 1);
            }
            else {
            	frequency.put(word, 1);
            }
            
          }
        }
      }
      String output = new String("");
      
      for(String key : frequency.keySet()) {
    	  output += key + "\n" + "\t" +frequency.get(key) +"\n\n";
      }
      
      fw.write(output);
      fw.close();
    }
    catch(Exception e)
    {
      System.out.println("Error:  " + e.toString());
    }
    
  }
  public static String tokenize(String word) {
	  
	  word = word.toLowerCase().trim();
	  word = word.replaceAll("[^abcdefghijklmnopqrstuvwxyz0123456789'-]*", "");
	  return word;	  
  }

}
