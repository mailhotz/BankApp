/**
 * BankApp class is used to simulate a bank account. 
 * Does the following:
 * Allows user to Deposit, Withdraw or Check balance
 * Deposit: Add money to account
 * Withdraw: Remove money from account
 * Check balance: Return money available in account
 * Progam is written to interact with command line with above commands plus 'exit'
 * @author Zachary Mailhot
 * 2/21/17 class created
 */

import java.util.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
public class BankApp{

  /**----------------------Instance Variables----------------------------*/

  /** Scanner used to handle standard input*/
  Scanner scan;
   
  /**File contents*/
  List<String> fileCont;

  /**Current directory info*/
  String curDir = System.getProperty("user.dir");

  /**Indexes for start and end of html table in fileCont*/
  int startIndex = -1, endIndex;


  /**-------------------------------------------------------------------*/

  /**Writes the value to the html table
   * @param val Value to push into html table
   * @param isDeposit True if deposit, false if withdraw
  */
  public void writeValue(Float val, boolean isDeposit){
   
    //Used to create new html file
    List<String> tmp = new ArrayList<String>();
    boolean added = false;
    String[] pre = fileCont.get(endIndex - 1).split("-|[0-9]");

    //Add new deposit row (i.e. positive number) to table
    if(isDeposit){
      for(int i = 0; i < fileCont.size(); i++){
        if(i == endIndex && !added){
          tmp.add(pre[0] + val + "</td></tr>");
          added  = true; 
          i = endIndex - 1;
        }
        else
          tmp.add(fileCont.get(i));
      }
      fileCont = tmp;
    
    }
    //Add new withdraw row (i.e. negative number) to table
    else{
      for(int i = 0; i < fileCont.size(); i++){
        if(i == endIndex && !added){
          tmp.add(pre[0] + "-" + val + "</td></tr>");
          added  = true; 
          i = endIndex - 1;
        }
        else
          tmp.add(fileCont.get(i));
      }
      fileCont = tmp;
    }
  }

  /**Returns balance from html table
   * @return Value representing the balance of bank account
  */
  public Float getBalance(){
    return null;
  }

  /** Reads file given a file name
   * @param fName Name of file to read to
   * @return File contents as list of string 
  */
  public List<String> readFile(String fName){
    Path path = Paths.get(fName);
    try{
      return Files.readAllLines(path, StandardCharsets.UTF_8);
    }
    catch(IOException ex){
      System.out.println("Unable to read file " + ex.toString());
      return null;
    }
  }

  /** Writes to a file given a file name
   * @param fName Name of file to write to
  */
  public void writeToFile(String fName){
    Path path = Paths.get(fName);
    try{
      Files.write(path, fileCont, StandardCharsets.UTF_8);
    }
    catch(IOException ex){
      System.out.println("Unable to write to file " + ex.toString());
    }
  }


  /** Checks if a string is in proper notation for Bank (2 decimal places max)
   * @param input String to be converted to float and checked
   * @return The value is returned after conversion if valid, otherwise returns -1
   */
  public float checkIfNumber(String input){

    //Matching currency with regex, found base for this regex here: 
    //http://stackoverflow.com/questions/13848570/currency-regular-expression
    if(input.matches("(\\$?\\d*(\\.\\d\\d?)?)$")){
        if(input.charAt(0) == '$')
          input = input.substring(1);
          return Float.parseFloat(input);
    }

    //Not a currency value, returning an error state
    else{
        System.out.println("Not a correct currency amount, try again");
        return -1;
    } 
  }

  /**Run the loop necessary to run the application; i.e. looking for input*/
  public void runApp(){
    fileCont = readFile(curDir + "/log.html");

    //Get transaction table location
    for(int i = 0; i < fileCont.size(); i++){
      if(fileCont.get(i).matches(".*id=\"transactions\".*"))
        startIndex = i;
      if(fileCont.get(i).matches(".*</tbody>.*") && startIndex != -1){  
        endIndex = i;
        break;
      }
    }
    System.out.println("Start:" + startIndex + " End:" + endIndex);

    scan = new Scanner(System.in);
    String tmp; //String used to hold user input

    System.out.println("Please enter a command(Deposit, Withdraw, Balance, Exit)");
    while(scan.hasNext()){

      //Lower case version of user input
      tmp = scan.next().toLowerCase();

      //Deposit command found
      if(tmp.equals("deposit")){

        System.out.println("Please enter an amount to deposit"); 
        tmp = scan.next();

        //Test user input, until valid
        while(checkIfNumber(tmp) == -1)
          tmp = scan.next();
           
        writeValue(checkIfNumber(tmp), true);
        writeToFile(curDir + "/log.html");
        System.out.println("Found deposit command");
      }
      //Withdraw command found
      else if(tmp.equals("withdraw")){
          
        System.out.println("Please enter an amount to withdraw");
        tmp = scan.next();

        //Test user input, until valid
        while(checkIfNumber(tmp) == -1)
          tmp = scan.next();
          
        writeValue(checkIfNumber(tmp), false); 
        writeToFile(curDir + "/log.html");
        System.out.println("Found withdraw command");
      }
      //Balance command found
      else if(tmp.equals("balance")){
        System.out.println("Found balance command");
      }
      //Exit command found
      else if(tmp.equals("exit")){
        System.exit(0);
      }
      //Invalid user input
      else{
        System.out.println("Invalid command");
      }
      
      System.out.println("Please enter a command(Deposit, Withdraw, Balance, Exit)");
    }
  }

  public static void main(String[] args){
    BankApp bApp = new BankApp();
    bApp.runApp();
  } 

}
