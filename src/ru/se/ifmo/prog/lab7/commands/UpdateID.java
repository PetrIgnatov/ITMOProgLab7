package ru.se.ifmo.prog.lab7.commands;

import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.classes.*;

public class UpdateID extends Command {
	public UpdateID() {
		super("update id {element}", "обновить значение элемента коллекции, id которого равен заданному", 2, new String[]{"Имя дракона: ", "Координата X: ", "Координата Y: ", "Возраст: ", "Цвет (доступные варианты - GREEN, YELLOW, ORANGE, WHITE): ", "Тип дракона (доступные варианты - WATER, UNDERGROUND, AIR): ", "Характер дракона (доступные варианты - EVIL, GOOD, CHAOTIC, CHAOTIC_EVIL, FICKLE): ", "Глубина пещеры: ", "Количество сокровищ в пещере: "}); 
	}
	@Override
	public Response execute(String[] args, Integer stacksize, Dragon dragon, CommandManager commandmanager, CollectionData collectiondata, DatabaseConnector connector, String login, String password) {
		super.check(args.length);
		if (stacksize > 10000) {
      return new Response(new String[0]);
    }
		try {
			Integer.parseInt(args[1]);
		}
		catch (Exception e) {
			System.out.println("Error! Argument is not a number");
			return new Response(new String[] {"Error! Argument is not a number"});
		}
		int index = collectiondata.findById(Integer.parseInt(args[1]));
		if (index == -1) {
			return new Response(new String[] {"Error! Dragon with ID " + args[1] + " not found"});
		}
                collectiondata.update(dragon, index, Integer.parseInt(args[1]));
		return new Response(new String[0]);
        }
}

