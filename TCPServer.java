import java.io.*;
import java.net.*;
import java.sql.ClientInfoStatus;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

class TCPServer {

	private static ServerSocket serverSocket = null;

	private static BufferedReader reader = null;

	private static BufferedWriter writer = null;

	private static Socket socket = null;

	private static FileInputStream fis = null;
	private static BufferedInputStream bis = null;
	private static OutputStream os = null;
	private static String serverdir = System.getProperty("user.dir") + "/Server/";
	private static String user = "";
	private static String account = "";
	private static String password = "";
	private static boolean accountvalid = false;
	private static boolean loggedin = false;
	private static boolean done = false;

	public static void main(String argv[]) throws Exception {
		String clientSentence;
		String clientSentenceSplit[];
		// String capitalizedSentence;

		serverSocket = new ServerSocket(7000);

		System.out.println("(listening for connection)");

		socket = serverSocket.accept(); // blocks until
										// connection
										// made
		if (socket.isConnected()) {
			sendLine("+MIT-XX " + InetAddress.getLocalHost().getHostName() + " Service");
		} else {
			sendLine("-MIT-XX Out to Lunch");
		}

		while (true) {

			clientSentence = readLine();
			// System.out.println("READ LINE == " + clientSentence);

			clientSentenceSplit = clientSentence.split("\\s");

			// System.out.println("User input is" + UserInput[0]);
			String cmd = clientSentenceSplit[0];

			switch (cmd) {
			case "USER":
				user(clientSentenceSplit[1]);

				break;
			case "ACCT":
				account(clientSentenceSplit[1]);

				break;
			case "PASS":
				password(clientSentenceSplit[1]);
				break;
			case "TYPE":
				
				if(clientSentenceSplit[1].equals("A")){
					
					sendLine("+Using Ascii mode");
					
					
				}else if(clientSentenceSplit[1].equals("B")){
					sendLine("+Using Binary mode");
					
					
				}else if(clientSentenceSplit[1].equals("C")){
					sendLine("+Using Continuos mode");
					
					
					
				}else{
					sendLine("-Type not valid");
					break;
					
				}
				
				
				
				
				

				break;
			case "LIST":
				// System.out.println(clientSentenceSplit[2]);
				boolean verbose = false;
				String dir = serverdir;

				if (clientSentenceSplit.length == 2) { // IF only LIST {V F} is
														// typed

					if (clientSentenceSplit[1].equals("F")) {
						verbose = false;
					} else if (clientSentenceSplit[1].equals("V")) {
						verbose = true;
					} else {
						sendLine("- Invalid second parameter");
						break;
					}

				} else { // IF LIST {V F} {dir} is typed

					if (clientSentenceSplit[1].equals("F")) {
						verbose = false;
					} else if (clientSentenceSplit[1].equals("V")) {
						verbose = true;
					} else {
						sendLine("- Invalid second parameter");
						break;
					}
					dir = clientSentenceSplit[2];
				}

				File folder = new File(dir);

				File[] listOfFiles = folder.listFiles();
				String totalfile = "";

				if (folder.isDirectory()) {

					if (!verbose) { //prints only the files names if verbose is not true
						for (int i = 0; i < listOfFiles.length; i++) {
							if (listOfFiles[i].isFile()) {
								totalfile = totalfile + listOfFiles[i].getName() + " ";
							} else if (listOfFiles[i].isDirectory()) {
								totalfile = totalfile + listOfFiles[i].getName() + " ";
							}
						}
						sendLine("+" + dir + ":" + " " + totalfile); //sends one long string to client to parse.

					} else {

						for (int i = 0; i < listOfFiles.length; i++) {
							if (listOfFiles[i].isFile()) {
								totalfile = totalfile + listOfFiles[i].getName() + " " + listOfFiles[i].length() + " "
										+ convertDate(listOfFiles[i].lastModified()) + " " + "	";
							} else if (listOfFiles[i].isDirectory()) {
								totalfile = totalfile + listOfFiles[i].getName() + " " + listOfFiles[i].length() + " "
										+ convertDate(listOfFiles[i].lastModified()) + " " + "	";

							}
						}
						sendLine("+" + dir + ":" + "	" + totalfile);

					}

				}

				else {

					sendLine("-This directory does not exist");

				}

				break;
			case "CDIR":

				File myFile1 = new File(clientSentenceSplit[1]);
				String temp7 = clientSentenceSplit[1];
				boolean cdirAccount = false;
				boolean cdirPass = false;

				if (myFile1.isDirectory()) {

					sendLine("+directory ok, send account/password");

					clientSentence = readLine(); // waiting for response from
													// client
					clientSentenceSplit = clientSentence.split("\\s");

					/// check if command is PASS or ACCT
					if (clientSentenceSplit[0].equals("ACCT")) {

						if (clientSentenceSplit[1].equals(account) && cdirPass) {

							sendLine("!Changed working dir to " + temp7);
							serverdir = temp7;
							if (!serverdir.substring(serverdir.length() - 1).equals("/")) {
								serverdir = temp7 + "/";
							}
							break;
						} else if (clientSentenceSplit[1].equals(account)) {
							sendLine("+account ok, send password");
							cdirAccount = true;

						} else {
							sendLine("-invalid account");
							break;

						}

					} else if (clientSentenceSplit[0].equals("PASS")) {

						if (clientSentenceSplit[1].equals(password) && cdirAccount) {

							sendLine("!Changed working dir to " + temp7);
							serverdir = temp7;
							if (!serverdir.substring(serverdir.length() - 1).equals("/")) {
								serverdir = temp7 + "/";
							}
							break;
						}

						else if (clientSentenceSplit[1].equals(password)) {
							sendLine("+password ok, send account");
							cdirPass = true;
						} else {
							sendLine("-invalid password");
							break;
						}

					} else {

						sendLine("-invalid command");
						break;
					}

					clientSentence = readLine(); // waiting for response from
													// client
					clientSentenceSplit = clientSentence.split("\\s");
					// checks for the second command to be acct or pass
					if (clientSentenceSplit[0].equals("ACCT")) {

						if (clientSentenceSplit[1].equals(account) && cdirPass) {

							sendLine("!Changed working dir to " + temp7);
							serverdir = temp7;
							if (!serverdir.substring(serverdir.length() - 1).equals("/")) {
								serverdir = temp7 + "/";
							}
							break;
						} else if (clientSentenceSplit[1].equals(account)) {
							sendLine("+account ok, send password");
							cdirAccount = true;

						} else {
							sendLine("-invalid account");
							break;

						}

					} else if (clientSentenceSplit[0].equals("PASS")) {

						if (clientSentenceSplit[1].equals(password) && cdirAccount) {

							sendLine("!Changed working dir to " + temp7);
							serverdir = temp7;
							if (!serverdir.substring(serverdir.length() - 1).equals("/")) {
								serverdir = temp7 + "/";
							}
							break;
						}

						else if (clientSentenceSplit[1].equals(password)) {
							sendLine("+password ok, send account");
							cdirPass = true;
						} else {
							sendLine("-invalid password");
							break;

						}

					} else {

						sendLine("-invalid command");

					}

				} else {

					sendLine("-Can't connect to directory because: this is not a valid directory");

				}

				break;
			case "KILL":

				File folder2 = new File(serverdir);
				int arrayNum = 0;
				boolean found = false;

				File[] listOfFiles2 = folder2.listFiles();

				//goes through the directory to find if the file exists
				for (int i = 0; i < listOfFiles2.length; i++) {
					if (listOfFiles2[i].isFile()) {
						if (listOfFiles2[i].getName().equals(clientSentenceSplit[1])) {
							found = true; //checks if the required file is found 
							arrayNum = i;
							break;
						}

					} else if (listOfFiles2[i].isDirectory()) {
						if (listOfFiles2[i].getName().equals(clientSentenceSplit[1])) {
							found = true;
							arrayNum = i;
							break;
						}
					}
				}
				//delets the file if found
				if (found) {
					File filetodel = new File(serverdir + listOfFiles2[arrayNum].getName());

					if (filetodel.delete()) {
						sendLine("+" + listOfFiles2[arrayNum].getName() + " deleted");
					} else {
						sendLine(serverdir + listOfFiles2[arrayNum].getName()
								+ "  -Not deleted because of security reasons");
					}

				} else {
					sendLine("-Not deleted because not found");
				}

				break;
			case "NAME":
				// as as above, looks for file in directory
				File folder3 = new File(serverdir);
				File[] listOfFiles3 = folder3.listFiles();
				int arrayNum2 = 0;
				boolean found2 = false;

				for (int i = 0; i < listOfFiles3.length; i++) {
					if (listOfFiles3[i].isFile()) {
						if (listOfFiles3[i].getName().equals(clientSentenceSplit[1])) {
							found2 = true;
							arrayNum2 = i;
							break;
						}

					} else if (listOfFiles3[i].isDirectory()) {
						if (listOfFiles3[i].getName().equals(clientSentenceSplit[1])) {
							found2 = true;
							arrayNum2 = i;
							break;
						}
					}
				}
				// renames if file is found
				if (found2) {
					sendLine("+File exists");
					clientSentence = readLine(); // waiting for response from
													// client
					clientSentenceSplit = clientSentence.split("\\s");
					if (clientSentenceSplit[0].equals("TOBE")) {

						File filetoname = new File(serverdir + listOfFiles3[arrayNum2].getName());
						File fileNametowrite = new File(serverdir + clientSentenceSplit[1]);

						if (filetoname.renameTo(fileNametowrite)) {
							sendLine(filetoname.getName() + " renamed to " + fileNametowrite.getName());
						} else {
							sendLine(" -File wasn't renamed because name is invalid");
						}

					}

				} else {

					sendLine("-Can't find " + clientSentenceSplit[1]);
				}

				break;
			case "DONE":
				//closes connection
				sendLine("+(the message may be charge/accounting info)");
				socket.close();
				done = true;

				break;
			case "RETR":

				File myFile = new File(serverdir + clientSentenceSplit[1]);
				String temp17 = serverdir + clientSentenceSplit[1];

				if (myFile.exists()) {

					byte[] mybytearray = new byte[(int) myFile.length()];
					sendLine(Long.toString(myFile.length()));
					// bytes to
					// be sent.

					clientSentence = readLine();

					clientSentenceSplit = clientSentence.split("\\s");

					String cmd2 = clientSentenceSplit[0];

					if (cmd2.equals("SEND")) {

						sendLine("File Sent");
						sendFile(temp17);

					} else if (cmd2.equals("STOP")) {

						sendLine("+ok, RETR aborted");

					} else {

						sendLine("Invalid command");

					}

				} else {
					sendLine("-File doesn't exist");
				}

				break;
			case "STOR":
				// only stor old works.
				if (clientSentenceSplit[1].equals("NEW")) {

				} else if (clientSentenceSplit[1].equals("OLD")) {

					File folder4 = new File(serverdir);
					File[] listOfFiles4 = folder4.listFiles();
					int arrayNum3 = 0;
					boolean found3 = false;
					String temp10 = clientSentenceSplit[2];

					//goes through files to check existence
					for (int i = 0; i < listOfFiles4.length; i++) {
						if (listOfFiles4[i].isFile()) {
							if (listOfFiles4[i].getName().equals(clientSentenceSplit[2])) {
								found3 = true;
								arrayNum3 = i;
								break;
							}

						} else if (listOfFiles4[i].isDirectory()) {
							if (listOfFiles4[i].getName().equals(clientSentenceSplit[2])) {
								found3 = true;
								arrayNum3 = i;
								break;
							}
						}
					}

					if (found3) {
						sendLine("+Will write over old file");

					} else {
						sendLine("+Will create new file"); //creates new file if file is not found.
						File file10 = new File(serverdir + clientSentenceSplit[2]);
						if (file10.createNewFile()) {
							// System.out.println("File is created!");
						} else { //file is created regardless of whether size is sent, as per protocol
							// System.out.println("File already exists.");
						}

					}

					clientSentence = readLine();
					clientSentenceSplit = clientSentence.split("\\s"); // receives
																		// size
																		// here.
					if (clientSentenceSplit[0].equals("ERROR")) { //client sends error if invalid command is specified.
						break;
					}

					if (Integer.parseInt(clientSentenceSplit[1]) < folder4.getUsableSpace()) {
						sendLine("+ok, waiting for file");

						int bytes = Integer.parseInt(clientSentenceSplit[1]); //receives size of file from user.

						if (bytes >= 4096) { //limits buffer array size to 4096.
							bytes = 4096;
						}

						DataInputStream dis = new DataInputStream(socket.getInputStream());
						FileOutputStream fos = new FileOutputStream(serverdir + temp10);
						byte[] buffer = new byte[bytes];

						int count = 0;

						while ((count = dis.read(buffer)) > 0) {
							fos.write(buffer, 0, count);
							fos.flush();
							if (count < 4096) { //writes to the file first and leaves loop if there is still stuff in buffer
								break;
							}
						}

						while (dis.available() > 0) { // cleaned the buffer
							dis.read();
						}

						fos.flush();
						fos.close();

						sendLine("+Saved " + temp10);

					} else {
						sendLine("-Not enough room. Dont send");
					}

				} else if (clientSentenceSplit[1].equals("APP")) {

				} else {

					break;

				}

				break;
			default:
				sendLine("invalid command");
			}

			if (done == true) {
				break;
			}


		}

	}

	static private void sendLine(String line) throws IOException {

		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		if (socket == null) {
			throw new IOException("SimpleFTP is not connected.");
		}
		try {
			writer.write(line + "\r\n\0");
			writer.flush();

		} catch (IOException e) {
			socket = null;
			throw e;
		}
	}

	static private String readLine() throws IOException {

		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		String line = reader.readLine();

		return line;
	}

	public static void user(String username) throws IOException {
		File myFile = new File(System.getProperty("user.dir") + "/Server/" + "USERS.txt");

		BufferedReader filereader = new BufferedReader(new FileReader(myFile));
		String line = "";
		String reqLine = "";
		String reqLineSplit[];
		boolean valid = false;

		line = filereader.readLine();

		while (line != null) { //keeps reading the file  until the required name is found

			if (line.contains(username)) { //checks if the username specified is valid in the text file
				reqLine = line;
				valid = true;
				break;
			}
			line = filereader.readLine();
		}

		if (valid) {
			reqLineSplit = reqLine.split("\t");
			username = reqLineSplit[0]; //assigns username, account and password straight from text file.
			account = reqLineSplit[1];
			password = reqLineSplit[2];

			sendLine("+User-id valid, send account and password");

		} else {

			sendLine("-Invalid user-id, try again");

		}

	}

	public static void account(String accountname) throws IOException {

		if (accountname.equals(account)) {
			sendLine("+Account valid, send password");
			accountvalid = true;

		} else {
			sendLine("-Invalid account, try again");
		}

	}

	public static void password(String upassword) throws IOException {

		if (upassword.equals(password)) {
			if (accountvalid) {
				sendLine("! Logged in");
				loggedin = true;
			} else {
				sendLine("+Send account");
			}

		} else {
			sendLine("-Wrong password, try again");
		}

	}

	public static String convertDate(long time) {
		Date date = new Date(time);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT+12:00"));
		String formatted = format.format(date);

		return formatted;
	}

	public static void sendFile(String file) throws IOException {
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[4096];

		while (fis.read(buffer) > 0) { //keeps writing from the file to the data output stream
			dos.write(buffer);
		}
		dos.write(-1); //must send -1 to signify the end of file transfer.

		fis.close();
		dos.flush();
		// dos.close();
	}

}
