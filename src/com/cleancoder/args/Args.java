package com.cleancoder.args;

import java.util.*;

import static com.cleancoder.args.ArgsException.ErrorCode.*;

public class Args 
{	
	
	  private Map<Character, ArgumentMarshaler> marshalers;
	  private Set<Character> argsFound;
	  private ListIterator<String> currentArgument;
	  private String schema;
	  private List<String> argsList;
	  
	  Args(){}
	  
	 public Args(String schema, String[] args) throws ArgsException 
	 {
		 marshalers = new HashMap<Character, ArgumentMarshaler>();
		 argsFound = new HashSet<Character>();
		 this.schema =schema;
		 argsList = Arrays.asList(args);
		 parseSchemaString();
	  }
	 
	 private void parseSchemaString()throws ArgsException
	 {
		 parseSchema();
	     parseArgumentStrings();
	 }
 
 private void parseSchema()throws ArgsException
 {
		 if(schema.length()==0)
			 throw new ArgsException(Missing_Schema);
		 else
		 {	
			 for (String element : schema.split(","))
				 if(element.length()<1)
				 {
					 throw new ArgsException (MISSING_SCHEMA_ARG);
				 }
				 else
				 {
					 parseSchemaElement(element.trim());
				 }
				 
		 }
 }
 
	 private void parseSchemaElement(String element) throws ArgsException 
	 	{
		    char elementId = element.charAt(0);
		    String elementTail = element.substring(1);
		    validateSchemaElementId(elementId);
		    putInMarshalers(elementId,elementTail);
	 	}
	
	 private void putInMarshalers(char elementId,String elementTail)throws ArgsException
	  	{
		  	if (elementTail.length() == 0)
		      marshalers.put(elementId, new BooleanArgumentMarshaler());
		    else if (elementTail.equals("*"))
		      marshalers.put(elementId, new StringArgumentMarshaler());
		    else if (elementTail.equals("#"))
		      marshalers.put(elementId, new IntegerArgumentMarshaler());
		    else if (elementTail.equals("##"))
		      marshalers.put(elementId, new DoubleArgumentMarshaler());
		    else if (elementTail.equals("[*]"))
		      marshalers.put(elementId, new StringArrayArgumentMarshaler());
		    else if (elementTail.equals("&"))
		      marshalers.put(elementId, new MapArgumentMarshaler());
		    else
		      throw new ArgsException(INVALID_ARGUMENT_FORMAT, elementId, elementTail);
	  	}
 
	  private void validateSchemaElementId(char elementId) throws ArgsException
	  {
	    if (!Character.isLetter(elementId))
	      throw new ArgsException(INVALID_ARGUMENT_NAME, elementId, null);
	  }
	  
	 
	  private void parseArgumentStrings() throws ArgsException 
	  {
		  if(argsList.isEmpty())
		  {
			  throw new ArgsException(MISSING_STRING_ARGUMENT);
		  }
		  else
		  {
			  for (currentArgument = argsList.listIterator(); currentArgument.hasNext();)
			  {
			      String argString = currentArgument.next();
			      if(argStringValidate(argString))
			      {
			    	  String argSubString = argString.substring(1);
			    	  if(argSubString.length()>0)
			    	  {
			    		  parseArgumentCharacters(argSubString);
			    	  }
			    	  else
			    	  {
			    		  throw new ArgsException(INCOMPLETE_PREFIX);
			    	  }
			      }
			      else
			      {
			    	  currentArgument.previous();
				      break;
			      }
			  }
	
		  }
	  }
	  
	  

		private boolean argStringValidate(String argString)
		{
				if(argString.startsWith("-"))
					return true;
				else
					return false;
		}
  
	  private void parseArgumentCharacters(String argChars) throws ArgsException 
	  {
		    for (int i = 0; i < argChars.length(); i++)
		      parseArgumentCharacter(argChars.charAt(i));
	  }

	  private void parseArgumentCharacter(char argChar) throws ArgsException 
	  {
		 ArgumentMarshaler m = marshalers.get(argChar);  //boolean marsh implemnt kiya h arg mashlr
		 if (m == null) 
		 {
		    	throw new ArgsException(UNEXPECTED_ARGUMENT, argChar, null);
		 } 
		  else
		   {
			  argsFound.add(argChar);
			  setCurrentArgument(currentArgument,argChar,m);
		    }
	  }
  
  public void setCurrentArgument(ListIterator<String> currentArgument,
								char argChar,
								ArgumentMarshaler m)throws ArgsException
  {
	try 
		{  
			m.set(currentArgument);
		} 
	catch (ArgsException e) 
		{
			e.setErrorArgumentId(argChar);
			throw e;
		}
  }
  
  public boolean has(char arg) 
  {
    return argsFound.contains(arg);
  }

  public int nextArgument() 
  {
    return currentArgument.nextIndex();
  }

  public boolean getBoolean(char arg)
  {
    return BooleanArgumentMarshaler.getValue(marshalers.get(arg));
  }

  public String getString(char arg) 
  {
    return StringArgumentMarshaler.getValue(marshalers.get(arg));
  }

  public int getInt(char arg) 
  {
    return IntegerArgumentMarshaler.getValue(marshalers.get(arg));
  }

  public double getDouble(char arg) 
  {
    return DoubleArgumentMarshaler.getValue(marshalers.get(arg));
  }

  public String[] getStringArray(char arg) 
  {
    return StringArrayArgumentMarshaler.getValue(marshalers.get(arg));
  }

  public Map<String, String> getMap(char arg)
  {
    return MapArgumentMarshaler.getValue(marshalers.get(arg));
  }
}
