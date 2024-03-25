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
								shallow = new CommandShallow(command, com);
								if (command.getParameterAdvices() != null) {
									parametersptr = 0;
									parameters = new String[command.getParameterAdvices().length];
									System.out.print(command.getParameterAdvices()[parametersptr]);
								}
								else {
									this.localExecute(shallow);
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
						if (parametersptr == parameters.length) {
							parametersptr = -1;
							try {
								shallow.setDragon(parameters);
								this.localExecute(shallow);								
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
	
	private void localExecute(CommandShallow shallow) {
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
		Response response = shallow.getCommand().execute(shallow.getArguments(), stacksize, shallow.getDragon(), commandmanager, collection);
		for (String s: response.getMessage()) {
			if (s.equals("exit")) {
				this.stop();
				break;
			}
			System.out.println(s);
		}
	}

	private void readCommand(Logger logger) {
		try {
			datagramPacket = new DatagramPacket(arr, arr.length);
			datagramSocket.receive(datagramPacket);
			ByteArrayInputStream bis = new ByteArrayInputStream(datagramPacket.getData());
			ObjectInput in = new ObjectInputStream(bis);
			CommandShallow shallow = (CommandShallow)in.readObject();
			if (!histories.containsKey(datagramPacket.getAddress())) {
				histories.put(datagramPacket.getAddress(), new LinkedList<Command>());
			}
			histories.get(datagramPacket.getAddress()).addLast(shallow.getCommand());
			if (histories.get(datagramPacket.getAddress()).size() > 5) {
				histories.get(datagramPacket.getAddress()).removeFirst();
			}
			Response response;
			logger.info("Got packet with command " + shallow.getCommand().getName());
			if (!shallow.getCommand().getName().equals("history")) {
				Integer stacksize = 0;
				response = shallow.getCommand().execute(shallow.getArguments(), stacksize, shallow.getDragon(), commandmanager, collection);
			}
			else {
				String[] history = new String[histories.get(datagramPacket.getAddress()).size()];
				for (int i = 0; i < history.length; ++i) {
					history[i] = histories.get(datagramPacket.getAddress()).get(i).getName();
				}
				response = new Response(history);
			}
			//System.out.println(datagramPacket.getAddress().toString());
			sender.send(response, datagramPacket.getAddress(), datagramPacket.getPort(), logger);
			//showMessage(datagramPacket);
		}
		catch (IOException e) {
	//		System.out.println(e.getMessage());
		}
		catch (ClassNotFoundException e) {
			logger.severe(e.getMessage());
		}
	}


	public void stop() {
		this.active = false;
	}
}
