package com.cleancoder.args;

import java.util.Map;

public class ArgsMain {
  public static void main(String[] args)
  {
    try 
    {
	      Args arg = new Args("l,p#,d*,m@,a##,p[*]", args);
	      boolean logging = arg.getBoolean('l');
	      int port = arg.getInt('p');
	      String directory = arg.getString('d');
	      Map<String, String> map =arg.getMap('m');
	      executeApplication(logging, port, directory);
    } 
    catch (ArgsException e) 
    {
    	System.out.println("Argument error: "+e.errorMessage());
    }
  }

  private static void executeApplication(boolean logging, int port, String directory) 
  {
	  System.out.printf("logging is %s, port:%d, directory:%s",logging, port, directory);
  }
}