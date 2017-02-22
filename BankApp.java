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


  /**-------------------------------------------------------------------*/

  /**
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

  /**
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
    for(int i = 0; i < fileCont.size() - 1; i++){
      System.out.println(fileCont.get(i));
    }
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
           

        System.out.println("Found deposit command");
      }
      //Withdraw command found
      else if(tmp.equals("withdraw")){
          
        System.out.println("Please enter an amount to withdraw");
        tmp = scan.next();

        //Test user input, until valid
        while(checkIfNumber(tmp) == -1)
          tmp = scan.next();
           
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
