import java.io.*;
import java.net.*;

class TCPClient {

	private static Socket socket = null;

	private static BufferedReader reader = null;
	private static BufferedWriter writer = null;

	private static File file = null;

	private static FileOutputStream fos = null;
	private static BufferedOutputStream bos = null;
	private static String clientdir = System.getProperty("user.dir") + "/Client/";
	private static boolean done = false;
	private static boolean loggedin = false; //TODO make sure to change to false for submission. 

	private static String user;

	public static void main(String argv[]) throws Exception {
		String sentence;
		String response = "";
		String UserInput[];

		// NOTE often times there is a 
		//System.out.print("R: ");
		//Client responds by checking errors on client side, rather than server side. 
		//
		//

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		socket = new Socket("localhost", 7000);

		System.out.print("R: ");
		System.out.println(readLine());

		while (true) {

			System.out.print("S: ");
			sentence = inFromUser.readLine();
			UserInput = sentence.split("\\s");

			// System.out.println("User input is" + UserInput[0]);
			String cmd = UserInput[0];
			cmd = cmd.toUpperCase();

			switch (cmd) {
			case "USER":
				if (!loggedin) {
					if (UserInput.length != 2) {

						break;

					} else {
						user(UserInput[1]);
					}
				} else {
					System.out.print("R: ");
					System.out.println("!" + user + " logged in"); //if user is already logged in
				}

				break;
			case "ACCT":
				if (!loggedin) {
					if (UserInput.length != 2) {

						break;

					} else {
						account(UserInput[1]);
					}
				} else {
					System.out.print("R: ");
					System.out.println("! Account valid, logged-in");
				}

				break;
			case "PASS":
				if (!loggedin) {
					if (UserInput.length != 2) {

						break;

					} else {
						password(UserInput[1]);
					}
				} else {
					System.out.print("R: ");
					System.out.println("! Logged in");
				}

				break;
			case "TYPE":
				
				if (loggedin) {
					if (UserInput.length == 2) {
						
						if(UserInput[1].toUpperCase().equals("A")){
							
							sendLine("TYPE A");
							System.out.print("R: ");
							System.out.println(readLine());
							
							
						}else if(UserInput[1].toUpperCase().equals("B")){
							sendLine("TYPE B");
							System.out.print("R: ");
							System.out.println(readLine());
							
							
						}else if(UserInput[1].toUpperCase().equals("C")){
							sendLine("TYPE C");
							System.out.print("R: ");
							System.out.println(readLine());
							
							
							
						}else{
							sendLine("TYPE error");
							System.out.print("R: ");
							System.out.println(readLine());
							
						}
						
						

						
						
					} else {
						break;
					}
				} else {

					System.out.print("R: ");
					System.out.println("Please Login");

				}
				


				break;
			case "LIST":

				if (loggedin) {
					if (UserInput.length == 2) {
						list(UserInput[1]); //if user only passes in one argument.
					} else if (UserInput.length == 3) {
						list(UserInput[1], UserInput[2]); //if user passes in two argument.
					} else {
						break;
					}
				} else {

					System.out.print("R: ");
					System.out.println("Please Login");

				}

				break;
			case "CDIR":
				if (loggedin) {
					if (UserInput.length != 2) {

						break;

					} else {
						String temp = "";
						sendLine("CDIR" + " " + UserInput[1]);
						System.out.print("R: ");
						temp = readLine();
						System.out.println(temp);

						if (temp.equals("+directory ok, send account/password")) {

							System.out.print("S: ");
							sentence = inFromUser.readLine(); // waiting for
																// user's
																// next command
							UserInput = sentence.split("\\s");

							if (UserInput.length != 2) {
								sendLine("garbage"); //send garbage if command is not valid. Server can decipher and respond.
								System.out.print("R: ");
								System.out.println(readLine());
								break;

							} else {
								//checks that the user inputs the acct or pass first time
								if (UserInput[0].toUpperCase().equals("ACCT")) {

									sendLine("ACCT" + " " + UserInput[1]);
									System.out.print("R: ");
									System.out.println(readLine());

								} else if (UserInput[0].toUpperCase().equals("PASS")) {

									sendLine("PASS" + " " + UserInput[1]);
									System.out.print("R: ");
									System.out.println(readLine());

								} else {

									System.out.print("R: ");
									System.out.println("- Invalid command. Re-enter command with valid parameters");
									break;
								}

							}

							System.out.print("S: ");
							sentence = inFromUser.readLine();
							UserInput = sentence.split("\\s");

							if (UserInput.length != 2) {
								sendLine("garbage"); //if server receives this, it'll respond appropriately 
								System.out.print("R: ");
								System.out.println(readLine());
								break;

							} else {
								//checks that the user inputs the acct or pass the second time
								if (UserInput[0].toUpperCase().equals("ACCT")) {

									sendLine("ACCT" + " " + UserInput[1]);
									System.out.print("R: ");
									System.out.println(readLine());

								} else if (UserInput[0].toUpperCase().equals("PASS")) {

									sendLine("PASS" + " " + UserInput[1]);
									System.out.print("R: ");
									System.out.println(readLine());

								} else {

									System.out.print("R: ");
									System.out.println("- Invalid command. Re-enter command with valid parameters");
									break;
								}

							}

						}

					}
				} else {
					System.out.print("R: ");
					System.out.println("Please Login");
				}

				break;
			case "KILL":
				if (loggedin) {
					if (UserInput.length != 2) {

						break;

					} else {
						kill(UserInput[1]);
					}
				} else {
					System.out.print("R: ");
					System.out.println("Please Login");
				}

				break;
			case "NAME":
				if (loggedin) {
					if (UserInput.length != 2) {

						break;

					} else {
						sendLine("NAME" + " " + UserInput[1]);
						System.out.print("R: ");
						String temp = "";
						temp = readLine();
						System.out.println(temp);
						//all the following is error checking for commands
						if (temp.equals("+File exists")) {

							System.out.print("S: ");
							sentence = inFromUser.readLine(); 
							UserInput = sentence.split("\\s");

							if (UserInput.length != 2) {
								System.out.print("R: "); 
								System.out.println("Invalid command");
								break;
							} else if(!UserInput[0].toUpperCase().equals("TOBE")){
								System.out.print("R: ");
								System.out.println("Invalid command");
								break;
								
							}
								else {
							

								sendLine("TOBE" + " " + UserInput[1]);
								System.out.print("R: ");
								System.out.println(readLine());

							}

						}

					}
				} else {
					System.out.print("R: ");
					System.out.println("Please Login");
				}

				break;
			case "DONE":
				if (loggedin) {
					if (UserInput.length != 1) {
						break;
					} else {
						sendLine("DONE");
						System.out.print("R: ");
						String temp4 = readLine();
						System.out.println(temp4);
						if (temp4.equals("+(the message may be charge/accounting info)")) {
							done = true;
							socket.close();
						}

					}
				} else {
					System.out.print("R: ");
					System.out.println("Please Login");
				}

				break;
			case "RETR":
				if (loggedin) {
					String readline = "";
					int bytes;

					retr(UserInput[1]);
					System.out.print("R: ");
					readline = readLine();
					System.out.println(readline);

					if (readline.equals("-File doesn't exist")) {
						break;
					} else {
						bytes = Integer.parseInt(readline); // total number of
															// bytes
															// from server

					}

					if (bytes >= 4096) {
						bytes = 4096;
					}

					System.out.print("S: ");
					sentence = inFromUser.readLine();

					if (sentence.toUpperCase().equals("SEND")) {

						sendLine("SEND ");
						System.out.print("R: ");
						System.out.println(readLine());

						file = new File(clientdir + UserInput[1]); //creates a new file if required
						if (file.createNewFile()) {
							// System.out.println("File is created!");
						} else {
							// System.out.println("File already exists.");
						}

						DataInputStream dis = new DataInputStream(socket.getInputStream());
						FileOutputStream fos = new FileOutputStream(clientdir + UserInput[1]); //create streams to receive file
						byte[] buffer = new byte[bytes];

						int count = 0;

						while ((count = dis.read(buffer)) > 0) {
							fos.write(buffer, 0, count); //reads from the input stream buffer and writes to the 
							fos.flush();// specified file
							if (count < 4096) {
								break;
							}
						}

						while (dis.available() > 0) { // cleans the buffer
							dis.read();
						}

						fos.flush();
						fos.close();

					} else if (sentence.toUpperCase().equals("STOP")) {

						sendLine("STOP ");
						System.out.print("R: ");
						System.out.println(readLine());

					} else {

						System.out.print("R: ");
						System.out.println("Invalid command");

					}
				} else {
					System.out.print("R: ");
					System.out.println("Please Login");
				}

				break;
			case "STOR":
			// STOR OLD <file> works. NEW and APPEND, no logic, because they are similar. Always use STOR OLD <file>.
				if (loggedin) {
					if (UserInput.length != 3) {

						break;

					} else {

						if (UserInput[1].equals("NEW")) {

						} else if (UserInput[1].toUpperCase().equals("OLD")) {

							sendLine("STOR" + " " + UserInput[1].toUpperCase() + " " + UserInput[2]);
							System.out.print("R: ");
							String readline = readLine();
							System.out.println(readline);
							String temp17 = UserInput[2];

							File myFile = new File(clientdir + UserInput[2]);

							System.out.print("S: ");
							sentence = inFromUser.readLine(); // waiting for
																// user to
																// pass in the
																// size
							UserInput = sentence.split("\\s");
							// verfies that the user enters a valid SIZE <size> otherwise user re-enters command.
							if (UserInput.length != 2) {
								System.out.print("R: ");
								System.out.println("Invalid Command");
								sendLine("ERROR ");
								break;
							}

							if (!UserInput[0].toUpperCase().equals("SIZE")) {
								System.out.print("R: ");
								System.out.println("Invalid Command");
								sendLine("ERROR ");
								break;
							}

							sendLine("SIZE" + " " + UserInput[1]);

							System.out.print("R: "); // response from server
														// after
														// size is entered
							readline = readLine();
							System.out.println(readline);

							if (readline.equals("+ok, waiting for file")) {

								sendFile(clientdir + temp17); //sends file after server responds with waiting for file.
								System.out.print("R: ");
								readline = readLine();
								System.out.println(readline);

							} else {
								break;
							}

						} else if (UserInput[1].equals("APP")) {

						} else {

							System.out.print("R: ");
							System.out.println("Invalid command");

						}

					}
				} else {
					System.out.print("R: ");
					System.out.println("Please Login");
				}

				break;
			default:
				System.out.print("R: ");
				System.out.println("Invalid command");
			}

			if (done == true) {
				break;
			}


		}

	}

	//sends command to kill
	private static void kill(String filename) throws IOException {
		sendLine("KILL" + " " + filename);
		System.out.print("R: ");
		System.out.println(readLine());

	}

	//sends commands to server. (opposite on server side)
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

	//reads responses from server
	static private String readLine() throws IOException {

		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		String line = reader.readLine();

		return line;
	}


	public static void user(String username) throws IOException {
		sendLine("USER" + " " + username);
		System.out.print("R: ");
		String temp = readLine();
		if (temp.equals("+User-id valid, send account and password")) {
			user = username;
		}

		System.out.println(temp);
	}

	public static void retr(String filename) throws IOException {
		sendLine("RETR" + " " + filename);

	}

	public static void account(String accountname) throws IOException {
		sendLine("ACCT" + " " + accountname);
		System.out.print("R: ");
		System.out.println(readLine());
	}

	public static void password(String password) throws IOException {
		sendLine("PASS" + " " + password);
		System.out.print("R: ");
		String temp = readLine();
		if (temp.equals("! Logged in")) {
			loggedin = true; //if server responds with !, logged in variable is set on client. Check login status on client side
		}

		System.out.println(temp);
	}

	public static void list(String arg, String arg2) throws IOException {

		sendLine("LIST" + " " + arg + " " + arg2);
		System.out.print("R: ");
		String temp = readLine(); //receives the directory as one long string and parses on this side.

		if (temp.equals("-This directory does not exist")) {
			System.out.println(temp);

		} else if (temp.equals("- Invalid second parameter")) {
			System.out.println(temp);
		}

		else {

			if (arg.equals("F")) {
				String splitFiles[] = temp.split("\\s"); //server sends with spaces to seperate files for F

				for (int i = 0; i < splitFiles.length; i++) {
					System.out.println(splitFiles[i]);
				}
			} else {

				String splitFiles[] = temp.split("\t"); //server sends with tabs as delimiter to seperate each file, as space is used for {name size lastmodified}

				for (int i = 0; i < splitFiles.length; i++) {
					System.out.println(splitFiles[i]);
				}

			}

		}

	}
	// same concept as above, except for only one argument
	public static void list(String arg) throws IOException {

		sendLine("LIST" + " " + arg);
		System.out.print("R: ");
		String temp = readLine();

		if (temp.equals("- Invalid second parameter")) {
			System.out.println(temp);
		} else {

			if (arg.equals("F")) {
				String splitFiles[] = temp.split("\\s");

				for (int i = 0; i < splitFiles.length; i++) {
					System.out.println(splitFiles[i]);
				}
			} else {

				String splitFiles[] = temp.split("\t");

				for (int i = 0; i < splitFiles.length; i++) {
					System.out.println(splitFiles[i]);
				}

			}
		}

	}

	public static void sendFile(String file) throws IOException {
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[4096];

		while (fis.read(buffer) > 0) {
			dos.write(buffer);
		}
		dos.write(-1);

		fis.close();
		dos.flush();
		// dos.close();
	}


}
