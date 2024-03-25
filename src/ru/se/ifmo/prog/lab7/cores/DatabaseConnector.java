package ru.se.ifmo.prog.lab7.cores;

import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.Collections.*;
import java.util.LinkedList;
import ru.se.ifmo.prog.lab7.classes.*;

public class DatabaseConnector {
	private Connection connection;

	public DatabaseConnector() {
		connection = null;
	}

	public DatabaseConnector(String name, String password) throws SQLException, SQLTimeoutException {
		connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/proglab7", name, password);
	}

	public DatabaseConnector(Properties logininfo) throws SQLException, SQLTimeoutException {
		connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/proglab7", logininfo);
	}	

	public LinkedList<String> getDragons() throws SQLException {
		Statement state = connection.createStatement();
		ResultSet rs = state.executeQuery("SELECT * FROM DRAGONS;");
		LinkedList<String> dragons = new LinkedList<String>();
		while (rs.next()) {
			int id = rs.getInt(1);
			String name = rs.getString(2);
			int x = rs.getInt(3);
			float y = rs.getFloat(4);
			String creationDate = rs.getString(5);
			int age = rs.getInt(6);
			String color = rs.getString(7);
			String type = rs.getString(8);
			String character = rs.getString(9);
			double depth = rs.getDouble(10);
			float numberOfTreasures = rs.getFloat(11);
			String login = rs.getString(12);
			dragons.addLast(id + ";" + name + ";" + x + ";" + y + ";" + creationDate + ";" + age + ";" + color + ";" + type + ";" + character + ";" + depth + ";" + numberOfTreasures + ";" + login);
		}
		return dragons;
	}

	public void save(LinkedList<Dragon> dragons) throws SQLException {
		Statement state = connection.createStatement();
		String req = "INSERT INTO DRAGONS(id, name, x, y, creationDate, age, color, type, character, depth, numberOfTreasures, login) VALUES ";
		for (int i = 0; i < dragons.size(); ++i) {
			Dragon dragon = dragons.get(i);
			String add = "(" + dragon.getId() + ", '" + (dragon.getName() != null ? dragon.getName() : "NULL") + "', " + (dragon.getCoordinates().getXPtr() != null ? dragon.getCoordinates().getX() : "NULL") + ", " + (dragon.getCoordinates().getYPtr() != null ? dragon.getCoordinates().getY() : "NULL") + ", '" + dragon.getDate().toString().replace('T', ' ') + "', " + dragon.getAge() + ", '" + dragon.getColorStr() + "', '" + dragon.getTypeStr() + "', '" + dragon.getCharacterStr() + "', " + (dragon.getCave().getDepthPtr() != null ? dragon.getCave().getDepth() : "NULL") + ", " + (dragon.getCave().getNumberOfTreasuresPtr() != null ? dragon.getCave().getNumberOfTreasures() : "NULL") + ", '" + dragon.getOwner() + "')";
			req += add + (i == dragons.size()-1 ? " " : ", ");
		}
		req += "ON CONFLICT (id) DO UPDATE SET name = DRAGONS.name, x = DRAGONS.x, y = DRAGONS.y, age = DRAGONS.age, color = DRAGONS.color, type = DRAGONS.type, character = DRAGONS.character, depth = DRAGONS.depth, numberOfTreasures = DRAGONS.numberOfTreasures;";
		int result = state.executeUpdate(req);
	}

	public String signIn(String login, String password) {
		try {
			Statement state = connection.createStatement();
			ResultSet rs = state.executeQuery("SELECT * FROM USERS WHERE name = '" + login + "';");
			int cnt = 0;
			boolean ok = false;
			while (rs.next()) {
				++cnt;
				String pword = rs.getString(2);
				if (pword.equals(password)) {
					ok = true;
				}
			}
			if (cnt < 1) {
				return "Пользователь с таким логином не найден! Попробуйте снова";
			}
			else if (!ok) {
				return "Неправильный пароль! Попробуйте снова";
			}
			return "Вы успешно зашли в систему";
		}
		catch (Exception e) {
			return "Ошибка! Попробуйте связаться с администратором или зайти в систему позже";
		}
	}

	public String register(String login, String password) {
		try {
			Statement state = connection.createStatement();
			ResultSet rs = state.executeQuery("SELECT * FROM USERS WHERE name = '" + login + "';");
			int cnt = 0;
			while (rs.next()) {
				++cnt;
			}
			if (cnt > 0) {
				return "Пользователь с таким логином уже существует! Придумайте новый";
			}
			String req = "INSERT INTO USERS(name, password) VALUES ('" + login + "', '" + password + "');";
			int res = state.executeUpdate(req);
			return "Вы успешно зашли в систему";
		}
		catch (Exception e) {
			return "Ошибка! Попробуйте связаться с администратором или зайти в систему позже";
		}
	}
}
