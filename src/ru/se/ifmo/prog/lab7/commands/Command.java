package ru.se.ifmo.prog.lab7.commands;

import ru.se.ifmo.prog.lab7.cores.*;
import java.io.Serializable;

public abstract class Command implements Executable, Serializable {
	String name;
	String description;
	CommandManager commandmanager;
	CollectionData collectiondata;
	int argsnumber;
	String[] parametersAdvices;
	public int calls;

	public Command(String name, String description, int argsnumber, String[] parametersAdvices) {
		this.name = name;
		this.description = description;
		this.argsnumber = argsnumber;
		this.parametersAdvices = parametersAdvices;
		this.calls = 1;
	}

	public Command(String name, String description, int argsnumber) {
		this.name = name;
		this.description = description;
		this.argsnumber = argsnumber;
	}

	public void check(int argsnumber) {
		if (this.argsnumber != argsnumber) {
			throw new IllegalArgumentException("Error! Got " + Integer.valueOf(argsnumber-1) + " arguments when " + Integer.valueOf(this.argsnumber-1) + " needed");
		}
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String getDescription() {
		return description;
	}

	public String[] getParameterAdvices() {
		return parametersAdvices;
	}
}
