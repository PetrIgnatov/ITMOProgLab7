package ru.se.ifmo.prog.lab7.commands;

import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.classes.*;

public class Exit extends Command {
	public Exit() {
		super("exit", "завершить программу (без сохранения в файл)", 1);
	}
	@Override
	public Response execute(String[] args, Integer stacksize, Dragon dragon, CommandManager commandmanager, CollectionData collectiondata, DatabaseConnector connector, String login, String password) {
		super.check(args.length);
		if (stacksize > 10000) {
      return new Response(new String[0]);
    }
		return new Response(new String[] {"exit"});
	}
}
