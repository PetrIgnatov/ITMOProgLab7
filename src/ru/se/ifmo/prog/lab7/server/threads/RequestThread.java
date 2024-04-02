package ru.se.ifmo.prog.lab7.server.threads;

import java.util.concurrent.locks.ReentrantLock;
import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.commands.*;
import java.net.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class RequestThread implements Callable<Response> {
	private ReentrantLock locker;
	private CollectionData data;
	private CommandShallow shallow;
        private HashMap<InetAddress, LinkedList<Command>> histories;
	private InetAddress address;
	private CommandManager commandmanager;
	private DatabaseConnector connector;

	public RequestThread(ReentrantLock locker, CollectionData data, CommandShallow shallow, HashMap<InetAddress, LinkedList<Command>> histories, InetAddress address, CommandManager commandmanager, DatabaseConnector connector) {
		this.data = data;
		this.locker = locker;
		this.shallow = shallow;
		this.histories = histories;
		this.address = address;
		this.commandmanager = commandmanager;
		this.connector = connector;
	}
	@Override
	public Response call() {
		System.out.println("Running thread");
		locker.lock();
		Response response = new Response();
		try {
			if (!histories.containsKey(address)) {
                                histories.put(address, new LinkedList<Command>());
                        }
                        histories.get(address).addLast(shallow.getCommand());
                        if (histories.get(address).size() > 5) {
                                histories.get(address).removeFirst();
                        }
                        if (!shallow.getCommand().getName().equals("history")) {
                                Integer stacksize = 0;
                                response = shallow.getCommand().execute(shallow.getArguments(), stacksize, shallow.getDragon(), commandmanager, data, connector, shallow.getLogin(), shallow.getPassword());
                        }
                        else {
                                String[] history = new String[histories.get(address).size()];
                                for (int i = 0; i < history.length; ++i) {
                                        history[i] = histories.get(address).get(i).getName();
                                }
                                response = new Response(history);
                        }
			for (String s : response.getMessage()) {
                                System.out.println(s);
                        }
			System.out.println("Thread over");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			locker.unlock();
			return response;
		}
	}
}
