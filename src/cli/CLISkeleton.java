package cli;

import java.io .*;

public class CLISkeleton { //unused
    private static final String PRINT = "print";
    private static final String EXIT = "exit";
    private static final String CONNECT = "connect";
    private static final String OPEN = "open";
    private static final String SET = "set";
    private final PrintStream outStream;
    private final BufferedReader inBufferedReader;
    private final String playerName;

    public static void main(String[] args) throws IOException {
            System.out.println("Welcome to [YourApp] version 0.1");

            CLISkeleton userCmd = new CLISkeleton("TestUser", System.out, System.in);

            userCmd.printUsage();
            userCmd.runCommandLoop();
        }

        public CLISkeleton(String playerName, PrintStream os, InputStream is) throws IOException {
            this.playerName = playerName;
            this.outStream = os;
            this.inBufferedReader = new BufferedReader(new InputStreamReader(is));
        }

        public void printUsage() {
            StringBuilder b = new StringBuilder();

            b.append("\n");
            b.append("\n");
            b.append("valid commands:");
            b.append("\n");
            b.append(CONNECT);
            b.append(".. connect as tcp client");
            b.append("\n");
            b.append(OPEN);
            b.append(".. open port become tcp server");
            b.append("\n");
            b.append(PRINT);
            b.append(".. print board");
            b.append("\n");
            b.append(SET);
            b.append(".. set a piece");
            b.append("\n");
            b.append(EXIT);
            b.append(".. exit");

            this.outStream.println(b.toString());
        }

        public void runCommandLoop() {
            boolean again = true;

            while (again) {
                boolean rememberCommand = true;
                String cmdLineString = null;

                try {
                    // read user input
                    cmdLineString = inBufferedReader.readLine();

                    // finish that loop if less than nothing came in
                    if (cmdLineString == null) break;

                    // trim whitespaces on both sides
                    cmdLineString = cmdLineString.trim();

                    // extract command
                    int spaceIndex = cmdLineString.indexOf(' ');
                    spaceIndex = spaceIndex != -1 ? spaceIndex : cmdLineString.length();

                    // got command string
                    String commandString = cmdLineString.substring(0, spaceIndex);

                    // extract parameters string - can be empty
                    String parameterString = cmdLineString.substring(spaceIndex);
                    parameterString = parameterString.trim();

                    // start command loop
                    switch (commandString) {
                        case PRINT:
                            this.doSkeleton(PRINT);
                            break;
                        case CONNECT:
                            this.doSkeleton(CONNECT, parameterString);
                            break;
                        case OPEN:
                            this.doSkeleton(OPEN);
                            break;
                        case SET:
                            this.doSkeleton(SET, parameterString);
                            // redraw
                            break;
                        case "q": // convenience
                        case EXIT:
                            again = false;
                            System.exit(1);
                            break; // end loop

                        default:
                            this.outStream.println("unknown command:" + cmdLineString);
                            this.printUsage();
                            rememberCommand = false;
                            break;
                    }
                } catch (IOException ex) {
                    this.outStream.println("cannot read from input stream - fatal, give up");
                    again = false;
                }
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                           ui method implementations                                        //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void doSkeleton(String cmd) {
        this.doSkeleton(cmd, null);
    }

    private void doSkeleton(String cmd, String parameterString) {
        System.out.print("command: " + cmd);
        if(parameterString == null) {
            System.out.print("\n");
        } else {
            System.out.println("(" +  parameterString + ")");
        }
    }
}