package ru.se.ifmo.prog.lab7.server.cores;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.*;
import java.nio.channels.*;
import java.awt.event.*;
import java.util.Iterator;
import ru.se.ifmo.prog.lab7.commands.*;
import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.exceptions.*;
import ru.se.ifmo.prog.lab7.server.threads.*;
import java.sql.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class UDPReader {
	private DatagramSocket datagramSocket;
	private boolean active;
	private ByteBuffer buffer;
	private byte arr[];
	private DatagramPacket datagramPacket;
	private CollectionData collection;
	private CommandManager commandmanager;
	private UDPSender sender;
	private HashMap<InetAddress, LinkedList<Command>> histories;
	private LinkedList<Command> localHistory;
	private DatabaseConnector connector;
	private boolean authorized;
	private String login;
	private String password;
	private ReentrantLock requestLock;
	private ExecutorService executor;

	public UDPReader(DatagramSocket datagramSocket, CollectionData collection, CommandManager commandmanager, UDPSender sender, DatabaseConnector connector) {
		this.active = true;
		this.connector = connector;
		this.datagramSocket = datagramSocket;
		this.buffer = ByteBuffer.allocate(10000);
		this.arr = new byte[10000];
		this.collection = collection;
		this.commandmanager = commandmanager;
		this.sender = sender;
		this.histories = new HashMap<InetAddress, LinkedList<Command>>();
		this.localHistory = new LinkedList<Command>();
		this.requestLock = new ReentrantLock();
		executor = Executors.newFixedThreadPool(1000);
		try {
			datagramSocket.setSoTimeout(100);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void start(Logger logger) {
		int parametersptr = -1;
		CommandShallow shallow = new CommandShallow();
		String[] parameters = new String[0];
		while (this.active) {
			try {
				if (System.in.available() > 0) {
					byte[] inputBuffer = new byte[System.in.available()];
					System.in.read(inputBuffer);
					String input = (new String(inputBuffer)).replaceAll("\n", "");
					if (parametersptr == -1) {
						String[] com = input.split(" ");
						if (com.length > 0) {
							try {
								Command command = commandmanager.getCommand(com[0]);
								if (command != null && !authorized && !command.getName().equals("exit") && !command.getName().equals("sign_in login password") && !command.getName().equals("register login password") && !command.getName().equals("help") && !command.getName().equals("save")) {
									System.out.println("Команда " + command.getName() + " недоступна неавторизованным пользователям");
									continue;	
								}
								shallow = new CommandShallow(command, com, login, password);
								if (command.getParameterAdvices() != null && command.getParameterAdvices().length > 0) {
									parametersptr = 0;
									parameters = new String[command.getParameterAdvices().length+1];
									System.out.print(command.getParameterAdvices()[parametersptr]);
								}
								else {
									this.localExecute(shallow, logger);
								}
							}
							catch (Exception e) {
								System.out.println(e.getMessage());
							}
						}
					}
					else {
						parameters[parametersptr] = input;
						parametersptr++;
						if (parametersptr == parameters.length - 1) {
							parametersptr = -1;
							parameters[parameters.length-1] = login;
							try {
								shallow.setDragon(parameters, login);
								this.localExecute(shallow, logger);								
							}
							catch (ConvertationException e) {
								System.out.println(e.getMessage());
							}
						}
						else {
							System.out.print(shallow.getCommand().getParameterAdvices()[parametersptr]);
						}
					}
				}
			}
			catch (IOException e) {
				System.out.println(e.getMessage());
			}
			readCommand(logger);
		}
	}
	
	private void localExecute(CommandShallow shallow, Logger logger) {
		Integer stacksize = 0;
		localHistory.addLast(shallow.getCommand());
		if (localHistory.size() > 5) {
			localHistory.removeFirst();
		}
		if (shallow.getCommand().getName().equals("history")) {
			for (Command com : localHistory) {
				System.out.println(com.getName());
			}
		}
		Response response = shallow.getCommand().execute(shallow.getArguments(), stacksize, shallow.getDragon(), commandmanager, collection, connector, "admin", "admin");
		for (String s: response.getMessage()) {
			if (s.equals("exit")) {
				this.stop();
				break;
			}
			else if (s.equals("save")) {
				try {
					connector.save(collection.getDragons());
				}
				catch (SQLException e) {
					System.out.println(e.getMessage());
					logger.severe("Ошибка сохранения!");
				}
				break;
			}
			else if (s.equals("Вы успешно зашли в систему")) {
                                                this.authorized = true;
                                                this.login = shallow.getArguments()[1];
                                                this.password = shallow.getArguments()[2];
                        }
			System.out.println(s);
		}
	}

	private void readCommand(Logger logger) {
		try {
			/*datagramPacket = new DatagramPacket(arr, arr.length);
			datagramSocket.receive(datagramPacket);
			ByteArrayInputStream bis = new ByteArrayInputStream(datagramPacket.getData());
			ObjectInput in = new ObjectInputStream(bis);
			CommandShallow shallow = (CommandShallow)in.readObject(); */
			datagramPacket = executor.submit(new ReadThread(datagramSocket)).get();
			if (datagramPacket != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(datagramPacket.getData());
        	                ObjectInput in = new ObjectInputStream(bis);
                	        CommandShallow shallow = (CommandShallow)in.readObject();
				Response response = new Response();
				FutureTask<Response> future = new FutureTask<>(new RequestThread(requestLock, collection, shallow, histories, datagramPacket.getAddress(), commandmanager, connector));
				Thread requestThread = new Thread(future);
				requestThread.start();
				sender.send(future.get(), datagramPacket.getAddress(), datagramPacket.getPort(), logger);
			}
			//showMessage(datagramPacket);
		}
		catch (InterruptedException e) {
        		e.printStackTrace();
        	}
		catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}


	public void stop() {
		this.active = false;
	}
}
