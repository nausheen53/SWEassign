package com.cleancoder.args;

import java.util.Map;
import java.util.Iterator;
import java.util.List;

public class ArgsMain {
  public static void main(String[] args)
  {
    try 
    {
	      Args arg = new Args("l,p#,d*,m&,a##,s[*]", args);
	      boolean logging = arg.getBoolean('l');
	      int port = arg.getInt('p');
	      String directory = arg.getString('d');
	      Map<String, String> map =arg.getMap('m');
	      double percent = arg.getDouble('a');
	      String[] strings = arg.getStringArray('s');
	      executeApplication(logging, port, directory);
	      printKeyValue(map);
	      printPercentage(percent);
	      printStringArray(strings);
	      
    } 
    catch (ArgsException e) 
    {
    	System.out.println("Argument error: "+e.errorMessage());
    }
  }

  private static void executeApplication(boolean logging,
		  								int port, 
		  								String directory) 
  {
	  System.out.printf("logging is %s, port:%d, directory:%s\n",logging, port, directory);	
  }
  
  private static void printKeyValue(Map<String,String>map)
  {
	  for(String s:map.keySet())
	  {
		  System.out.println("key is "+s+" value is "+map.get(s));
	  }
  }
  
  private static void printPercentage(double percent)
  {
	  System.out.println("Percentage is "+percent+"%");
  }
  
  private static void printStringArray(String[] strings)
  {	
	  int i=0;
	  for(String str : strings)
		  	System.out.println("string "+(++i)+" is "+str);
  }
}
