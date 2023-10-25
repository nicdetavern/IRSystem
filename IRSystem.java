/*By Nicolas D.
 * Iterates through a user-defined folder and parses the data into an output folder
 * the output contains KVP's in a structure as follows
 * '<term>' 
 * //Frequency: <term frequency>
 * //Locations: <term locations>
 * //Soundex Form: <term soundex form>
 * repeats untils all documents' contents are indexed.
 */
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class IRSystem {
	
	public static void start() {
	// TODO Auto-generated method stub
		try
	    {
	      System.out.print("Enter name of a directory> ");
	      Scanner scan = new Scanner(System.in);
	      File dir = new File(scan.nextLine());
	      File[] fileList = dir.listFiles();
	      
	      
	      
	      //Hashmaps to store frequency, location, and soundex form with a common String key
	      Map<String, Integer> frequency = new HashMap<>();
	      Map<String, String> loc = new HashMap<>();
	      Map<String, String> sound = new HashMap<>();
	      
	      
	      
	      System.out.print("Enter name of output file> ");
	      File outputdir = new File(scan.nextLine());
	      FileWriter fw = new FileWriter(outputdir);
	      
	      //Initializes fileID for use
//	      int fileID = 1;
	      
	      for (File f : fileList)
	      {
	    	//Convert fileID to a string to be stored in the location hashmap
	    	String fileName = new String("" +f.getName().toString());
	        Scanner sc = new Scanner(f);
	        while (sc.hasNextLine())
	        {
	          StringTokenizer st = new StringTokenizer(sc.nextLine());
	          while (st.hasMoreTokens())
	          {
	            String word = st.nextToken();
	            //Calls tokenize method with the word string as input
	            word = tokenize(word);
	            
	            //Stores location where the given word occurred in hashmap
	            if(loc.containsKey(word) && !loc.get(word).contains(fileName)) {
	            	loc.put(word, loc.get(word) +", " +fileName);
		        }
		        else {
		            loc.put(word, fileName);
		        }
	            
	            //Updates frequency hashmap for a new or recurring occurrence of the current word
	            if (frequency.containsKey(word)) {
	            	frequency.put(word, frequency.get(word) + 1);
	            }
	            else {
	            	frequency.put(word, 1);
	            }
	            
	          }
	        }
	      }
	      //Initializes string to store inverted index
	      String output = new String("");
	      
	      //Populates soundex hashmap with reduced forms of words present in the inverted index
	      for(String key : frequency.keySet()) {
	    	 sound.put(key, soundex(key));
	      }
	      
	      //Writes formatted inverted index to output string
	      for(String key : frequency.keySet()) {
	    	  output += "'" +key +"'" + "\n" + "\t//Frequency:\n\t\t{" +frequency.get(key) +"}\n"
	    			  +"\t//Locations\n\t\t{" +loc.get(key) +"}" +"\n\t//Soundex Form\n\t\t'" 
	    			  +sound.get(key) +"'"  +"\n\n";
	      }
	      
	      //Writes output to a file
	      fw.write(output);
	      fw.close();
	      int SENTINEL = 0;
	      //Stores user input, or desired search
	      String userQuery = new String("");
	      //Stores soundex reduced form of userQuery
	      String sForm = new String("");
	      
	      //Treemap to automatically store and rank edit distance for given terms
	      Map<Integer, String> relevance = new TreeMap<>();
	      	      
	      //while loop to determine user query correctness and consequently search the inverted index, 
	      //or hashmap, for the query, ended by user typing "-99"
	      while (SENTINEL != -99) {
		      System.out.println("Enter desired single-term query(-99 to exit): ");
		      relevance.clear();
		      userQuery = scan.next().toLowerCase();
		      //Initialize mDistance to 0, will store the calculated edit distance
		      int mDistance = 0;
		      //Checks whether to end the while loop, if not then checks user query against database to 
		      //determine correctness and prompt if potential error is detected
		      
		      if (!userQuery.equals("-99")) {
		    	  //Utilize soundex method to reduce userQuery to soundex form, store in sForm
		    	  sForm = soundex(userQuery);
			      
			      if (sound.containsKey(userQuery)) {
			    	  //Do nothing
			      }
			      else  {
			    	  String relevantWords = new String("");
			    	  
			    	  //Stores the number of times the below for loop has iterated
			    	  int pTermCount = 0;
			    	  
			    	  //Compute edit distance to each potential correct spelling with a similar sound
			    	  //Prompt up to 5 potential alternatives
			    	  for (String key : sound.keySet()) {
			    		  if (sound.get(key).contains(sForm)) {
			    			  mDistance = minDistance(userQuery, key);
			    			  if(!relevance.containsValue(key)) {
			    				  relevance.put(mDistance, key);
			    				  pTermCount++;
			    			  }
			    			  else {
			    				  relevance.put(mDistance, relevance.get(mDistance) +", " +key);
			    				  pTermCount++;
			    			  }
			    			  
			    		  }
			    	  }
			    	  
			    	  //Checks if 5 alternatives are present, and if not, will populate the relevance treemap 
//			    	  	up to a total of 5 term occurences
			    	  if(pTermCount < 5) {
			    		  for (String key : sound.keySet()) {
			    			  if (!key.isEmpty() && !sForm.isEmpty() && key.charAt(0) == sForm.charAt(0)) {
			    				  mDistance = minDistance(userQuery, key);
			    				  if(!relevance.containsValue(key)) {
				    				  relevance.put(mDistance, key);
				    			  }
				    			  else {
				    				  relevance.put(mDistance, relevance.get(mDistance) +", " +key);
				    			  }
			    			  }
			    			  else {
			    				  //Do nothing
			    			  }
			    		  }
			    	  }
			    	  else {
			    		  //Do nothing
			    	  }
			    	  
			    	  //Stores up to 5 entries into the string relevantWords to be displayed
			    	  int rWordI = 0;
			    	  for (int key : relevance.keySet()) {
				    	  relevantWords += key +": " +relevance.get(key) +"\n";
				    	  rWordI++;
				    	  
				    	  if(rWordI >= 5){
				    		  break;
				    	  }
				      }
			    	  
			    	  //Prompt user with alternatives to their potentially incorrect input and scan 
			    	  //for their potentially corrected input
			    	  System.out.println("'" +userQuery +"'" +" is not recognized by this database. "
			    	  		+ "Potential alternatives(Ranked by edit distance): \n" +relevantWords 
			    	  		+"Please enter the same word if there was no error: ");
			    	  userQuery = scan.next().toLowerCase();
			      }
			      //Clear the relevance treemap of previous terms for use in determining the closest document
			      relevance.clear();
			      
			      //Checks if sForm is invalid, and if it is, return to the top of the loop
			      //Otherwise, will return a list of 5 entries containing potential candidates
			      if (sForm != "Invalid") {
			    	  String relevantDocs = new String("");
			    
			    	//Iterates over each entry in the frequency keySet, only considering 
			    	//entries whose keys contain the same first letter as the sForm var  
			      for(String key : frequency.keySet()) {
			    	  mDistance = minDistance(userQuery, key);
			    	  
			    	  if (!key.isEmpty() && !sForm.isEmpty() && key.charAt(0) == sForm.charAt(0)) {
			    		  if(!relevance.containsValue(key)) {
			    			  relevance.put(mDistance, loc.get(key));
			    		  }
			    		  else {
			    			  relevance.put(mDistance, relevance.get(mDistance) +", " +loc.get(key));
			    		  }
			    	  }
			      }
			      //Records up to 5 entries from relevance treemap, ranked by edit distance, to the relevantDocs string
			      int rDocI = 0;
			      for (int key : relevance.keySet()) {
			    	  relevantDocs += key +": " +relevance.get(key) +"\n";
			    	  rDocI++;
			    	  if (rDocI >= 5) {
			    		  break;
			    	  }
			      }
			      
			      //Formats and display relevant documents to the user
			      relevantDocs = relevantDocs.replaceAll(".dat", "");
			      System.out.println("Relevant documents(Ranked by edit distance):\n" +relevantDocs);
			      }
			      else {
			    	  System.out.println("Invalid input\n");
			      }
		      }
		      else {
		    	  //Ends the while loop
		    	  SENTINEL = -99;
		      }
	      }
	    }
	    catch(Exception e)
	    {
	      System.out.println("Error:  " + e.toString());
	    }
		
		
	}
	
	//Tokenize will return a formatted word replacing all unincluded chars in 
	//the regular expression: [^abcdefghijklmnopqrstuvwxyz0123456789'-]
	public static String tokenize(String word) {
		  
		  word = word.toLowerCase().trim();
		  word = word.replaceAll("[^abcdefghijklmnopqrstuvwxyz0123456789'-]", "");
		  return word;	  
	}
	
	//soundex will reduce a given string to a 4-digit soundex String
	public static String soundex(String input) {
		input = input.toLowerCase();
		input = input.replaceAll("[^abcdefghijklmnopqrstuvwxyz]", "");
		
		if (input.isEmpty()) {
			return "Invalid";
		}

		
		String reduced = new String("");
		String temp = new String("");
		reduced = input.substring(0, 1);
		input = input.substring(1, input.length());

		
		input = input.replaceAll("[aeiouhwy]", "0");
		input = input.replaceAll("[bfpv]", "1");
		input = input.replaceAll("[cgjkqsxz]", "2");
		input = input.replaceAll("[dt]", "3");
		input = input.replaceAll("[l]", "4");
		input = input.replaceAll("[mn]", "5");
		input = input.replaceAll("[r]", "6");
				
		for(int i = 0; i < input.length(); i++) {		
			if(i != input.length() - 1 && input.charAt(i) == input.charAt(i + 1)) {
				temp += input.charAt(i);
				i++;
			}
			else {
				temp += input.charAt(i);
			}
		}
		
		temp = temp.replaceAll("0", "");

		while (temp.length() < 3) {
			temp += "0";
		}
		temp = temp.substring(0, 3);
		return reduced += temp;
	}
        
	//Calculate edit distance between word1 and word2
	public static int minDistance(String word1, String word2) {
		int len1 = word1.length();
		int len2 = word2.length();
	 
		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] dp = new int[len1 + 1][len2 + 1];
	 
		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}
	 
		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}
	 
		//iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);
	 
				//if last two chars equal
				if (c1 == c2) {
					//update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;
	 
					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
			}
		}
	 
		return dp[len1][len2];
	}
}
