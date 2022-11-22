package interpreter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;

import interpreter.bytecode.*;

public class ByteCodeLoader {
  private StringTokenizer tokenizer;
  private BufferedReader sourceFile;

  public ByteCodeLoader(String byteCodeFile) throws IOException {
    this.sourceFile = new BufferedReader(new FileReader(byteCodeFile));
  }

  public Program loadCodes() throws IOException {
    Program program = new Program();
    String source = sourceFile.readLine();
    ArrayList<String> args = new ArrayList<String>();

    while (source != null) {
      args.clear();
      tokenizer = new StringTokenizer(source);
      String byteType = tokenize(source, args);
      
      try {
        if (byteType != null) {
          ByteCode byteCode = (ByteCode) (Class.forName("interpreter.bytecode." + byteType).getDeclaredConstructor()
              .newInstance());

          byteCode.init(args);
          program.addCode(byteCode);
        }
        source = sourceFile.readLine();
      } catch (Exception e) {
        System.out.println("Exception occurred " + e);
        source = null;
      }
    }

    program.resolveAddresses(program);
    return program;
  }

  private String tokenize(String code, ArrayList<String> args) throws IOException {
    tokenizer = new StringTokenizer(code);
    String nextTok = tokenizer.nextToken();
    String type;
    if (nextTok != null) {
      type = CodeTable.getClass(nextTok);

      while (tokenizer.hasMoreTokens()) {
        args.add(tokenizer.nextToken());
      }
      return type;
    }
    return null;
  }

}