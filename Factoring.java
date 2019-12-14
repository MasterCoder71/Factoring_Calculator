// Import required packages
import java.io.*;
import java.util.*;

public class Factoring {
  // Static integer variables to store the factors being used in the program
  static int factor1, factor2;

  // Constructor to call the methods for factoring quadratic polynomial expressions
  Factoring() {
    // Calls the method to print the contents of the program
    printBegin();

    // Creates the scanner object
    Scanner sc = new Scanner(System.in);

    // Asks the user whether they want to read from the text file or manually enter input
    System.out.println("Choose whether you would like to read from the text file or enter your own basic input for the program to execute on: ");
    System.out.println("(Type either 'input' to enter input, or just type anything else to read from the text file)");
    String s = sc.nextLine();

    // If the string that they typed is 'input', then call the askInput() method
    if(s.equalsIgnoreCase("input")) {
      // Asks for the input and then factors out the expression
      askInput(sc, true);
    }
    // If not, then read from the file and print out the factored expressions for each problem in the text file
    else {
      // Sets the scanner object to a new scanner of the file "Factoring.txt" and calls the askInput() method, but this time it passes the boolean value false, telling the method to scan from the file
      try {
        sc = new Scanner(new File("Factoring.txt"));
        askInput(sc, false);
      }
      // Catches the exception that might occur when trying to read a file, and if it does get an error, then redirect the user back to the beginning of the program
      catch(FileNotFoundException fnfe) {
        System.out.println("The file was not able to be found. Redirecting...");
        new Factoring();
      }
    }
  }

  // Main method to call new instance of Factoring (Calls the constructor)
  public static void main(String[] args) {
    new Factoring();
  }

  // Method to ask for the input from the user (quadratic polynomial constants)
  private static void askInput(Scanner sc, boolean bool) {
    // If the user wanted to manually enter input...
    if(bool) {
      // Asks for the quadratic polynomial values:
      System.out.println("\nPlease enter the quadratic polynomial constant values:\n(ax^2 + bx + c)\n");
      // Asks for the A Value in AX^2 + BX + C
      System.out.print("Enter a: ");
      int a = sc.nextInt();
      // Asks for the B Value in AX^2 + BX + C
      System.out.print("\nEnter b: ");
      int b = sc.nextInt();
      // Asks for the C Value in AX^2 + BX + C
      System.out.print("\nEnter c: ");
      int c = sc.nextInt();
      // Creates a new line and calls the factor method
      System.out.println();
      factor(a, b, c);
    }
    // If the user entered anything else on the screen when asked...
    else {
      // Gets the first line from the text file and prints out the title for this section
      String s = sc.nextLine();
      System.out.println("\nProblems from file:\n");
      
      // Iterates through a while loop to read until the end of the file has been reached
      while(s != null) {
        // Splits the string that is read into its multiple parts and stores into string array
        String[] split = s.split(" ");
        
        // Factors using the values from the string array, but each string is being parsed into an integer before being sent off to the factor() method
        factor(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        System.out.println();
        
        // Trys to read the next line from the file, and if the IO read operation fails for any reason, then this block of code makes sure that the program exits out of the loop
        try {s = sc.nextLine();}
        catch(Exception e) {break;}
      }
    }
  }

  // Method to factor the quadratic polynomial based on the a, b, and c values
  private static void factor(int a, int b, int c) {
    // Calculates the first and second val, making sure it is always positive (absolute), and creates boolean variable to be used later
    int firstVal = (a*c<0 ? -(a*c) : a*c);
    int secondVal = (b<0 ? -b : b);
    int gcf = findGCF(a, secondVal);
    boolean isFactorable = false;

    // If the c value in the quadratic polynomial is 0, then find a common factor between a and b to factor out the x and the common factor
    if(firstVal == 0) {
      boolean simplifiable = (a%b == 0);
      int divisor = (a>b ? b : a);
      if(simplifiable) System.out.println(divisor + "x(" + a/divisor + "x" + (b/divisor<0 ? b/divisor : "+" + b/divisor) + ") <-- (Line 1)");
      else System.out.println("x(" + a + "x" + (b<0 ? b : "+" + b) + ") <-- (Line 1)");
      return;
    }
    // If the gcf of the two numbers is not 1, then it factors out the gcf and solves the rest of the polynomial normally
    else if(gcf!=1) {
      a /= gcf;
      b /= gcf;
      secondVal = b;
      c /= gcf;
      firstVal = a*c;
    }

    // For loop to iterate through the numbers in the first value (a*c) and find the factors of a*c that can add up to b
    for(int i = 1; i < firstVal+1; i++) {
      // Checks if the remainder of the first value (a*c) divided by i is equal to 0
      if(firstVal%i == 0) {
        // Makes a variable called divisibleBy that stores the other factor that goes with the variable i
        int divisibleBy = firstVal/i;
        // Checks if the two factors have already been iterated through
        if(factor1 != divisibleBy && factor2 != i) {
          // Checks if the boolean isFactorable variable is false
          if(isFactorable == false)
            // Calls the firstStep method and stores the result in the isFactorable method
            isFactorable = firstStep(a, b, c, i, divisibleBy, gcf);
        }
      }
    }
    // After the for loop, the isFactorable variable is checked to see if the polynomial was factorable or not, and prints out the error message accordingly
    if(!isFactorable) System.out.println("This polynomial is not factorable.");
  }

  // Variable to display the steps of the factoring by grouping using the factors and the a, b, and c variables
  private static boolean firstStep(int a, int b, int c, int i, int divisibleBy, int gcf) {
    // Checks if the two factors as positive integers add up to the b value
    if(i + divisibleBy == b) {
      // Stores the factor variables in the static variable stored as a class instance, and also accounts for the order in which the factors will be displayed in the steps
      factor1 = (i%a==0 && (i!=1 || (i==1 && a==1)) ? i : i==1 ? (divisibleBy%a==0 ? divisibleBy : (a%divisibleBy==0 ? divisibleBy : i)) : (a%i == 0 ? i : divisibleBy));
      factor2 = (divisibleBy%c==0 && (divisibleBy!=1 || (divisibleBy==1 && c==1)) && factor1!=divisibleBy ? divisibleBy : divisibleBy==1 ? (i%c==0 ? i : (c%i==0 ? i : divisibleBy)) : (c%divisibleBy == 0 && factor1!=divisibleBy ? divisibleBy : i));

      // Calls the checkFactor() method and stores the result of the method into a String variable called "factor"
      String factor = checkFactors(a, c, factor1, factor2);

      // If the factor variable is not empty, then...
      if(!factor.equals("")) {
        // Creates an integer variable of the factor string
        int integerFactor = Integer.parseInt(factor);

        // Prints out the steps to factor the polynomial by grouping and returns a value of true
        System.out.println((gcf!=1 ? gcf + "(" : "") + (a==1 ? "" : a) + "x^2 + " + factor1 + "x + " + factor2 + "x " + (c<0 ? "- " + -c : "+ " + c) + (gcf!=1 ? ")" : "") + " <-- (Line 1)");
        System.out.println((gcf!=1 ? gcf + "(" : "") + (a!=1 ? a : "") + "x(x" + (integerFactor<0 ? factor : ("+" + factor)) + ") + " + factor2 + "(x" + (integerFactor<0 ? factor : ("+" + factor)) + (gcf!=1 ? ")" : "") + ") <-- (Line 2)");
        System.out.println((gcf!=1 ? gcf : "") + "(" + (a!=1 ? a : "") + "x" + (factor2<0 ? factor2 : "+" + factor2) + ")(" + "x" + (integerFactor<0 ? factor : ("+" + factor)) + ") <-- (Line 3)");
        return true;
      }
      // If the factor is empty, then return false for the method
      else return false;
    }
    // Checks if the first factor as positive and the second factor as negative added together would equal b
    else if(i - divisibleBy == b) {
      // Stores the factor variables in the static variable stored as a class instance, and also accounts for the order in which the factors will be displayed in the steps
      factor1 = (i%a==0 && (i!=1 || (i==1 && a==1)) ? i : i==1 ? (-divisibleBy%a==0 ? -divisibleBy : (a%-divisibleBy==0 ? -divisibleBy : i)) : (a%i == 0 ? i : -divisibleBy));
      factor2 = (-divisibleBy%c==0 && (-divisibleBy!=1 || (-divisibleBy==1 && c==1)) && factor1!=-divisibleBy ? -divisibleBy : -divisibleBy==1 ? (i%c==0 ? i : (c%i==0 ? i : -divisibleBy)) : (c%-divisibleBy == 0 && factor1!=-divisibleBy ? -divisibleBy : i));

      // Calls the checkFactor() method and stores the result of the method into a String variable called "factor"
      String factor = checkFactors(a, c, factor1, factor2);

      // If the factor variable is not empty, then...
      if(!factor.equals("")) {
        int integerFactor = Integer.parseInt(factor);
        System.out.println((gcf!=1 ? gcf + "(" : "") + (a==1 ? "" : a) + "x^2 + " + factor1 + "x + " + factor2 + "x " + (c<0 ? "- " + -c : "+ " + c) + (gcf!=1 ? ")" : "") + " <-- (Line 1)");
        System.out.println((gcf!=1 ? gcf + "(" : "") + (a!=1 ? a : "") + "x(x" + (integerFactor<0 ? factor : ("+" + factor)) + ")" + (factor2<0 ? " - " + -factor2 : " + " + factor2) + "(x" + (integerFactor<0 ? factor : ("+" + factor)) + (gcf!=1 ? ")" : "") + ") <-- (Line 2)");
        System.out.println((gcf!=1 ? gcf : "") + "(" + (a!=1 ? a : "") + "x" + (factor2<0 ? factor2 : "+" + factor2) + ")(" + "x" + (integerFactor<0 ? factor : ("+" + factor)) + ") <-- (Line 3)");
        return true;
      }
      // If the factor is empty, then return false for the method
      else return false;
    }
    // Checks if the first factor as negative and the second factor as postiive added together would equal b
    else if(-i + divisibleBy == b) {
      // Stores the factor variables in the static variable stored as a class instance, and also accounts for the order in which the factors will be displayed in the steps
      factor1 = (-i%a==0 && (-i!=1 || (-i==1 && a==1)) ? -i : -i==1 ? (divisibleBy%a==0 ? divisibleBy : (a%divisibleBy==0 ? divisibleBy : -i)) : (a%-i == 0 ? -i : divisibleBy));
      factor2 = (divisibleBy%c==0 && (divisibleBy!=1 || (divisibleBy==1 && c==1)) && factor1!=divisibleBy ? divisibleBy : divisibleBy==1 ? (-i%c==0 ? -i : (c%-i==0 ? -i : divisibleBy)) : (c%divisibleBy == 0 && factor1!=divisibleBy ? divisibleBy : -i));

      // Calls the checkFactor() method and stores the result of the method into a String variable called "factor"
      String factor = checkFactors(a, c, factor1, factor2);

      // If the factor variable is not empty, then...
      if(!factor.equals("")) {
        int integerFactor = Integer.parseInt(factor);
        System.out.println((a==1 ? "" : a) + "x^2 + " + factor1 + "x + " + factor2 + "x " + (c<0 ? "- " + -c : "+ " + c) + " <-- (Line 1)");
        System.out.println((a!=1 ? a : "") + "x(x" + (i<0 ? i : ("+" + i)) + ") + " + divisibleBy + "(x" + (integerFactor<0 ? factor : ("+" + factor)) + ") <-- (Line 2)");
        System.out.println("(" + (a!=1 ? a : "") + "x" + (divisibleBy<0 ? divisibleBy : "+" + divisibleBy) + ")(" + "x" + (integerFactor<0 ? factor : ("+" + factor)) + ") <-- (Line 3)");
        return true;
      }
      // If the factor is empty, then return false for the method
      else return false;
    }
    // Checks if the two factors as negative integers add up to the b value
    else if(-i - divisibleBy == b) {
      // Stores the factor variables in the static variable stored as a class instance, and also accounts for the order in which the factors will be displayed in the steps
      factor1 = (-i%a==0 && (-i!=1 || (-i==1 && a==1)) ? -i : -i==1 ? (-divisibleBy%a==0 ? -divisibleBy : (a%-divisibleBy==0 ? -divisibleBy : -i)) : (a%-i == 0 ? -i : -divisibleBy));
      factor2 = (-divisibleBy%c==0 && (-divisibleBy!=1 || (-divisibleBy==1 && c==1)) && factor1!=-divisibleBy ? -divisibleBy : -divisibleBy==1 ? (-i%c==0 ? i : (c%-i==0 ? -i : -divisibleBy)) : (c%-divisibleBy == 0 && factor1!=-divisibleBy ? -divisibleBy : -i));

      // Calls the checkFactor() method and stores the result of the method into a String variable called "factor"
      String factor = checkFactors(a, c, factor1, factor2);

      // If the factor variable is not empty, then...
      if(!factor.equals("")) {
        int integerFactor = Integer.parseInt(factor);
        System.out.println((a==1 ? "" : a) + "x^2 + " + factor1 + "x + " + factor2 + "x " + (c<0 ? "- " + -c : "+ " + c) + " <-- (Line 1)");
        System.out.println((a!=1 ? a : "") + "x(x" + (integerFactor<0 ? factor : ("+" + factor)) + ") - " + factor2 + "(x" + (integerFactor<0 ? factor : ("+" + factor)) + ") <-- (Line 2)");
        System.out.println("(" + (a!=1 ? a : "") + "x" + (factor2<0 ? factor2 : "+" + factor2) + ")(" + "x" + (integerFactor<0 ? factor : ("+" + factor)) + ") <-- (Line 3)");
        return true;
      }
      // If the factor is empty, then return false for the method
      else return false;
    }
    // If any of the combinations do not end up with the value b, then return false for the method
    else {
      return false;
    }
  }

  // Method to check the factors provided from the parameters a, c, val, and divisibleBy
  private static String checkFactors(int a, int c, int val, int divisibleBy) {
    // Calculates the two factors from the values provided and checks if the two factors, returning true or false based on the result of comparing
    int factor1 = val%a==0 ? val/a : (a%val==0 ? a/val : (c%val==0 ? c/val : (val%c==0 ? val/c : 0)));
    int factor2 = divisibleBy%a==0 && (factor1 != val/a || val/a == divisibleBy/a) ? divisibleBy/a : (a%divisibleBy==0 && factor1 != a/val && factor1 != val/a ? a/divisibleBy : (c%divisibleBy==0 && factor1 != c/val ? c/divisibleBy : divisibleBy/c));
    //System.out.println("factor1: " + factor1 + ", factor2: " + factor2);
    return factor1==factor2 ? factor1 + "" : "";
  }

  // Method to find the gcf of the two numbers passed as parameters
  private static int findGCF(int a, int b) {
    int maxVal = 1;
    // For loop to iterate through and find if any values between the two numbers are the greatest factors
    for(int i = 1; i < (a>b ? a+1 : b+1); i++) {
      if(a%i==0 && b%i==0 && i>maxVal)
        maxVal = i;
    }
    return maxVal;
  }

  // Method to print the beginning part of the program
  private static void printBegin() {
    System.out.println("-----------------------------------------------------------------");
    System.out.println("| Algebra 2 | Mukund Raman | Factoring By Grouping | 6th Period |");
    System.out.println("-----------------------------------------------------------------\n");
  }
}
