package interpreter.bytecode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import interpreter.VirtualMachine;

public class ReadCode extends ByteCode {
  public ReadCode() {
  }

  @Override
  public void execute(VirtualMachine vm) {
    System.out.print("Please input an integer: ");

    try {
      BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
      String readLine = read.readLine();
      vm.getRunStack().push(Integer.parseInt(readLine));
    } catch (Exception e) {
      System.out.println(e);
    }

    if (vm.dumpIsOn()) {
      System.out.println("READ");
      
    }
  }

  @Override
  public void init(ArrayList<String> parameters) {

  }
}