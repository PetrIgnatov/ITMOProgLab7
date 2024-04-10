package ru.se.ifmo.prog.lab7.commands;

import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.classes.*;
import java.util.Collections;
import java.util.LinkedList;

public class ExecuteScript extends Command {
	public ExecuteScript() {
		super("execute_script file_name", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.", 2);
	}
	@Override
	public Response execute(String[] args, Integer stacksize, Dragon dragon, CommandManager commandmanager, CollectionData collectiondata, DatabaseConnector connector, String[] params, String login, String password) {
		super.check(args.length);
		if (stacksize > 1000) {
			return new Response(new String[0]);
		}
		LinkedList<CommandShallow> commandsList = ScriptReader.readCommands(args[1], commandmanager, login, password);
		Response response = new Response(new String[0]);
		for (CommandShallow command : commandsList) {
			response.addLines(command.execute(++stacksize, commandmanager, collectiondata, connector).getMessage());
		}
		return response;
	}
}

