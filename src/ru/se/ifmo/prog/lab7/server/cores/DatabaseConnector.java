package ru.se.ifmo.prog.lab7.server.cores;

import java.sql.*;
import java.util.*;
import java.io.*;

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

	public void getDragons() throws SQLException {
		Statement state = connection.createStatement();
		ResultSet rs = state.executeQuery("SELECT * FROM DRAGONS;");
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
			System.out.println(id + " " + name + " " + x + " " + y + " " + creationDate + " " + age + " " + color + " " + type + " " + character + " " + depth + " " + numberOfTreasures);
		}
	}
}
