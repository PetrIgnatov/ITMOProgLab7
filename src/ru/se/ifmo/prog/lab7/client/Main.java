package ru.se.ifmo.prog.lab7.client;

import ru.se.ifmo.prog.lab7.classes.*;
import ru.se.ifmo.prog.lab7.commands.*;
import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.client.cores.*;
import java.util.*;
import ru.se.ifmo.prog.lab7.exceptions.*;

public class Main {
	public static void main(String[] args) throws InputArgumentException {
		if (args.length != 0) {
			throw new InputArgumentException("Error! Got " + Integer.valueOf(args.length) + " arguments when 0 required");
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("\nВыключаем клиент");
			}));
		CommandManager commandmanager = new CommandManager();
		UDPConnector connector = new UDPConnector();
		UDPSender sender = new UDPSender(); 
		UDPReader reader = new UDPReader();
		Console console = new Console(commandmanager, sender, reader);
		String[] comnames = {"help", "info", "show", "add", "update", "remove_by_id", "clear", "save", "execute_script", "exit", "remove_at", "sort", "history", "sum_of_age", "print_field_ascending_character", "print_field_descending_character"};
		Command[] coms = {new Help(), new Info(), new Show(), new Add(), new UpdateID(), new RemoveID(), new Clear(), new Save(), new ExecuteScript(), new Exit(), new RemoveIndex(), new Sort(), new History(), new SumOfAge(), new Ascending(), new Descending()};
		for (int i = 0; i < coms.length; ++i)
		{
			try {
				commandmanager.createCommand(comnames[i], coms[i]);
			}
			catch (CommandIOException e) {
				System.out.println(e.getMessage());
			}
		}	
		console.start(connector);
	}
}
