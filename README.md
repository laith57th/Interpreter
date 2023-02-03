# Execution and Development Environment
To complete this project, I used Visual Studio Code on my Macbook M1 pro.
openjdk version "17.0.2" 2022-01-18
OpenJDK Runtime Environment Temurin-17.0.2+8 (build 17.0.2+8)
OpenJDK 64-Bit Server VM Temurin-17.0.2+8 (build 17.0.2+8, mixed mode)
# Compilation Result
Using the terminal, I used the following commands on the project files:
> javac interpreter/bytecode/*.java
> javac interpreter/Interpreter.java
> java interpreter.Interpreter <bytecode file>
The program ran as expected and no error messages were displayed
Assumptions
Assumed a correct bytecode file will be passed to the interpreter class during execution.
Summary of technical work
To complete this project I implemented a strategy design pattern to dynamically choose the
behavior executed for each scanned bytecode from the token stream. Using object oriented
programming, I passed the information of the bytecode classes to the interpreter while keeping
all instance variables private to each respective class. I also used single function methods in
each bytecode class and the corresponding classes, especially the RunTimeStack class.
Implementation
ByteCode subclasses
1. ByteCode
a. This is the abstract superclass that is parent to all the bytecode subclasses. This
class initializes the init method that takes an arraylist of string parameters and an
execute method that takes a VirtualMachine object.
2. ArgsCode
a. This bytecode class prepares for a function call by creating a new frame and passing
the instance variable derived from the tokenstream. The init method simply sets the
instance variable string equal to the bytecode name.
Page 5
3. BopCode
a. This class is the longest class out of all the bytecode files; however, this is only
because there are multiple operators that require different behavior. The way I went
about doing this was a switch statement that takes the variable operator as a
parameter and performs the correct operation then simply pushes the result on top
of the runTime stack.
4. BranchCode
a. This class is simply a helper byteCode instance for the resolveAddresses method
inside the program class. This class extends ByteCode and is a parent class to
CALL, FALSEBRANCH, and GOTO. Essentially all the classes that require a new
target program counter. This class initializes new abstract base classes, getTarget,
setTarget, and getLabel that need to be implemented in the child classes.
5. CallCode
a. This bytecode extends BranchCode. The init method simply sets the functionName
instance variable to the string read from the bytecode file. The execute method sets
a new target pc and transfers control to the function call. This class implements all
the BranchCode abstract methods.
6. DumpCode
a. This class simply sets the dump on or off depending on the passed value from the
bytecode file.
7. FalseBranchCode
a. This bytecode extends BranchCode. The init method simply sets the label instance
variable to the string read from the bytecode file. The execute method sets a new
target pc if the value on top of the runTimeStack is true. This class implements all
the BranchCode abstract methods.
8. GotoCode
a. This bytecode extends BranchCode. The init method simply sets the label instance
variable to the string read from the bytecode file. The execute method sets a new
target pc and transfers control to the new label. This class implements all the
BranchCode abstract methods.
9. HaltCode
a. This class sets isRunning to false in the VirtualMachine and prints “HALT” if dump is
on.
10. LabelCode
a. This class’s init method sets the label instance variable to the value read from the
bytecode file and has a getLabel method that returns the label.
11. LitCode
a. This class’s init method checks if there’s an id connected to the literal value and sets
the id instance variable to that value; otherwise, sets id to null. The execute method
for this class simply prints the dump output if dump is on.
12. LoadCode
a. This class’s init method sets the id and value instance variables to the values read
from the bytecode file. The execute method calls the load helper method from the
runTimeStack class and prints the dump output if dump is on.
13. PopCode
a. This class’s init method sets the level instance variable to the value read from the
bytecode file. The execute method pops all the values up to the specified level in the
runTime stack and prints the output if dump is on.
14. ReadCode
a. This class reads a new value from the user and pushes it on top of the runTime
stack.
15. ReturnCode
a. This class’s init method checks if there’s functionName present after the return
Page 6
keyword and sets functionName to null otherwise. The execute method for this class
simply prints the dump output if dump is on.
16. StoreCode
a. This class simply stores the value read into the offset from the start of the frame by
calling the store method in the RunTimeStack class. The execute method also prints
the output if dump is on.
17. WriteCode
a. This bytecode simply writes the value on top of the stack to the output if dump is on.
CodeTable class
This class was simple to implement. I started by creating a HashMap to store the bytecodes, then I
created a getClass method to retrieve the bytecode instance from the hashmap using the passed
string parameter.
ByteCodeLoader class
This class was similar to the lexer class I implemented in an earlier assignment. To implement the
loadCodes method, I created a new program object and iterated through the bytecode file adding
each bytecode to the program object. One thing I struggled with was using the forName method to
create a new instance of each bytecode. My IDE kept suggesting that the newInstance method had
been deprecated. After some research, I found that I need to use the getDeclaredConstructor() to
retrieve the respective constructor of each bytecode.
Program class
I started implementing this class by creating a Vector of bytecode objects and HashMap to hold
the symbolic addresses used in the resolveAddresses method. The program class contains three
methods: addCode, getCode, and resolveAddresses. The addCode method takes a byteCode
object as a parameter, checks if the bytecode is a label and adds it to the addresses HashMap,
then adds the bytecode to the program vector. The getCode method simply retrieves the bytecode
from the program vector. Finally resolveAddresses creates a temporary integer jumpAddress,
iterates through the program vector, checks if the bytecode is either a CALL, FALSEBRANCH, or
GOTO, and sets the new target address by using a BranchCode object. Overall this class wasn’t
too difficult to implement, the trickiest part was probably the resolveAddresses function.
RunTimeStack class
This was probably the longest class to implement out of all the classes in the project. However, most
of the functions were straightforward (getters and setters). The most complex method to implement
for me was the dump method. To implement this method, I decided to create a new instance
variable “frameIndex” that simply tracks the index of the vector that a new frame was created. I used
this to find the end of the runTime stack and the beginning of the temporary framePointer stack if
applicable. In order not to modify the runTime stack, I used a temporary arrayList to hold the frame
stack and print the contents after the for loop.
Code Organization
For better organization, all bytecode class files are inside the subpackage bytecode, inside the
package interpreter. Also, all instance variables are private to each class and can be accessed
through getter methods.
