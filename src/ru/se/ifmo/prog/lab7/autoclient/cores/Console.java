package ru.se.ifmo.prog.lab7.autoclient.cores;

import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.commands.*;
import ru.se.ifmo.prog.lab7.classes.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.se.ifmo.prog.lab7.exceptions.*;
import java.security.*;

public class Console implements Serializable {
	private Scanner scanner;
	private boolean active;
	private CommandManager commandmanager;
	private LinkedList<Command> history;
	private LinkedList<String> commandsStack;
	private int stacksize;
	private UDPSender sender;
	private UDPReader reader;
	private boolean authorized; 
	private String login;
	private String password;
	private String serverip;
	private int serverport;

	public Console(CommandManager commandmanager, UDPSender sender, UDPReader reader) throws NoSuchAlgorithmException {
		this.scanner = new Scanner(System.in);
		this.active = true;
		this.history = new LinkedList<Command>();
		this.commandmanager = commandmanager;
		this.commandsStack = new LinkedList<String>();
		this.stacksize = 0;
		this.sender = sender;
		this.reader = reader;
		this.authorized = false;
	}
	
	public void setServer(String ip, int port) {
		this.serverip = ip;
		this.serverport = port;
	}

	public void start(UDPConnector connector, String scriptName) {
		String host = "";
		int port = 0;
		/*Pattern pattern = Pattern.compile("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");	
		Patter ipv6pattern = Patter.compile("(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))");
		*/
				try {
					InetAddress.getByName(serverip);
				}
				catch (UnknownHostException e) {
					System.out.println("Неизвестный IP! Попробуйте ввести снова");
					return;
				}
				port = serverport;
		connector.connect(host, port);
		sender = new UDPSender(connector.getDatagramChannel(), connector.getAddress());
		reader = new UDPReader(connector.getDatagramChannel());
		int[] parametersptr = {-1};
		CommandShallow shallow = new CommandShallow();
		String[] parameters = new String[0];
		autoRunScript(scriptName);
	}

	public void print(String line)
	{
		if (line.equals(null))
		{
			return;
		}
		System.out.print(line);
	}

	public void println(String line)
	{
		if (line.equals(null))
		{
			System.out.println();
			return;
		}
		System.out.println(line);
	}

	public void autoRunScript(String scriptname) {
		try {
		CommandShallow signinshallow = new CommandShallow(commandmanager.getCommand("sign_in"), new String[] {"sign_in", "Petrign", "Graph"}, login, password);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(signinshallow);
		byte[] arr = baos.toByteArray();
		sender.send(arr);
		Response response = reader.getResponse("sign in");
		for (String s: response.getMessage()) {
			if (s.equals("exit")) {
				this.stop();
				break;
			}
			else if (s.equals("Вы успешно зашли в систему")) {
				this.authorized = true;
				this.login = signinshallow.getArguments()[1];
				this.password = signinshallow.getArguments()[2];
			}
			System.out.println(s);
		}
		CommandShallow autoshallow = new CommandShallow(commandmanager.getCommand("execute_script"), new String[] {"execute_script", scriptname}, login, password);
		baos = new ByteArrayOutputStream();
		oos = new ObjectOutputStream(baos);
		oos.writeObject(autoshallow);
		arr = baos.toByteArray();
		sender.send(arr);
		response = reader.getResponse("execute");
		for (String s: response.getMessage()) {
			if (s.equals("exit")) {
				this.stop();
				break;
			}
			else if (s.equals("Вы успешно зашли в систему")) {
				this.authorized = true;
				this.login = autoshallow.getArguments()[1];
				this.password = autoshallow.getArguments()[2];
			}
			System.out.println(s);
		}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String readln() {
		if (commandsStack.size() == 0) {
			stacksize = 0;
			return scanner.nextLine();
		}
		else {
			this.println(commandsStack.peek());
			return commandsStack.removeFirst();
		}
	}
	
	public void stop() {
		active = false;
	}
	
	public void printHistory() {
		for (int i = history.size()-1; i >= 0; i--) {
			println(history.get(i).getName());
		}	
	}
}

