package ru.se.ifmo.prog.lab7.commands;

import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.classes.*;

public class Register extends Command {
	public Register() {
		super("register login password", "зарегистрироваться в системе", 3, new String[0]);
	}
	@Override
	public Response execute(String[] args, Integer stacksize, Dragon dragon, CommandManager commandmanager, CollectionData collectiondata, DatabaseConnector connector, String login, String password) {
		super.check(args.length);
		if (stacksize > 10000) {
			return new Response(new String[0]);
		}
		String[] response = new String[] {connector.register(args[1], args[2])};
		return new Response(response);
	}
}
