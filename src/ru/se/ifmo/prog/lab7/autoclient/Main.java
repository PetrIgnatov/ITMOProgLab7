package ru.se.ifmo.prog.lab7.autoclient;

import ru.se.ifmo.prog.lab7.classes.*;
import ru.se.ifmo.prog.lab7.commands.*;
import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.autoclient.cores.*;
import java.util.*;
import ru.se.ifmo.prog.lab7.exceptions.*;

public class Main {
	public static void main(String[] args) throws InputArgumentException {
		if (!(args.length == 3)) {
			throw new InputArgumentException("Error! Got " + Integer.valueOf(args.length) + " arguments when 0 required");
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("\nВыключаем клиент");
			}));
			for (int j = 0; j <= 2; ++j) {
				System.out.println("Creating thread " + j);
				Runnable r = new ClientThread(j%3+1, args[1], Integer.parseInt(args[2]));
				Thread thread = new Thread(r, "new thread");
				thread.start();
			}
	}
}
