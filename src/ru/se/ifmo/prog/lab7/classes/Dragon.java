package ru.se.ifmo.prog.lab7.classes;

import java.util.Date;
import java.io.Serializable;

public class Dragon implements Comparable<Dragon>, Serializable {
	private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
	private String name; //Поле не может быть null, Строка не может быть пустой
	private Coordinates coordinates; //Поле не может быть null
	private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
	private int age; //Значение поля должно быть больше 0
	private Color color; //Поле может быть null
	private DragonType type; //Поле может быть null
	private DragonCharacter character; //Поле может быть null
	private DragonCave cave; //Поле может быть null
	
	public Dragon(int id, String name, Coordinates coordinates, java.util.Date creationDate, int age, Color color, DragonType type, DragonCharacter character, DragonCave cave) {
		this.setId(id);
		this.setName(name);
		this.setCoordinates(coordinates);
		this.setDate(creationDate);
		this.setAge(age);
		this.setColor(color);
		this.setType(type);
		this.setCharacter(character);
		this.setCave(cave);	
	}

	public Dragon(int id, String name, Integer x, Float y, java.util.Date creationDate, int age, Color color, DragonType type, DragonCharacter character, Double depth, Float numberOfTreasures) {
		this.setId(id);
		this.setName(name);
		this.setCoordinates(new Coordinates(x, y));
		this.setDate(creationDate);
		this.setAge(age);
		this.setColor(color);
		this.setType(type);
		this.setCharacter(character);
		this.setCave(new DragonCave(depth, numberOfTreasures));	
	}
	
	public Dragon(String name, Integer x, Float y, int age, Color color, DragonType type, DragonCharacter character, Double depth, Float numberOfTreasures) {
		this.setName(name);
		this.setCoordinates(new Coordinates(x, y));
		this.setAge(age);
		this.setColor(color);
		this.setType(type);
		this.setCharacter(character);
		this.setCave(new DragonCave(depth, numberOfTreasures));	
	}

	public void setId(int id) {
		this.id = id;
	}
	
	private void setName(String name) {
		if ((name == null) || (name.length() == 0) || (name.matches("^\s*$"))) {
			throw new IllegalArgumentException("Error! Can't use name \"" + name + "\"");
		}
		this.name = name;
	}	
	
	private void setCoordinates(Coordinates coordinates) {
		if (coordinates == null || coordinates.getXPtr() == null || coordinates.getYPtr() == null) {
			throw new IllegalArgumentException("Error! Coordinates can't be null for dragon with name \"" + this.name + "\"");
		}	
		this.coordinates = coordinates;
	}
	
	public void setDate(java.util.Date creationDate) {
		if (creationDate == null) {
			throw new IllegalArgumentException("Error! Creation Date can't be null for dragon with name \"" + this.name + "\"");
		}
		this.creationDate = creationDate;
	}
	
	private void setAge(int age) {
		if (age < 0) {
			throw new IllegalArgumentException("Error! Dragon with name \"" + this.name + "\" wasn't born yet");
		}
		if (age == 0) {
			throw new IllegalArgumentException("Error! Dragon with name \"" + this.name + "\" is too young");
		}
		this.age = age;
	}
	
	private void setColor(Color color) {
		this.color = color;
	}
	
	private void setType(DragonType type) {
		this.type = type;
	}
	
	private void setCharacter(DragonCharacter character) {
		this.character = character;
	}
	
	private void setCave(DragonCave cave) {
		this.cave = cave;
	}

	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}	
	
	public Coordinates getCoordinates() {	
		return this.coordinates;
	}
	
	public java.util.Date getDate() {
		return this.creationDate;
	}
	
	public int getAge() {
		return this.age;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public DragonType getType() {
		return this.type;
	}
	
	public DragonCharacter getCharacter() {
		return this.character;
	}
	
	public DragonCave getCave() {
		return this.cave;
	}
	@Override
	public int compareTo(Dragon otherDragon)
	{
		if (this.id > otherDragon.getId()) {
			return 1;
		}
		if (this.id < otherDragon.getId()) {
			return -1;
		}
		if (!this.name.equals(otherDragon.getName())) {
			return this.name.compareTo(otherDragon.getName()); 
		}
		if (!this.coordinates.equals(otherDragon.getCoordinates())) {
			return this.coordinates.compareTo(otherDragon.getCoordinates()); 
		}
		if (!this.creationDate.equals(otherDragon.getDate())) {
			return this.creationDate.compareTo(otherDragon.getDate()); 
		}
		if (age > otherDragon.getAge()) {
			return 1; 
		}
		if (age < otherDragon.getAge()) {
			return -1;
		}
		if (!this.color.equals(otherDragon.getColor())) {
			return this.color.compareTo(otherDragon.getColor()); 
		}
		if (!this.type.equals(otherDragon.getType())) {
			return this.type.compareTo(otherDragon.getType()); 
		}
		if (!this.character.equals(otherDragon.getCharacter())) {
			return this.character.compareTo(otherDragon.getCharacter()); 
		}
		if (!this.cave.equals(otherDragon.getCave())) {
			return this.cave.compareTo(otherDragon.getCave()); 
		}	
		return 0;
	}
	@Override
	public String toString() {
		return Integer.toString(id) + ";" + name + ";" + coordinates.toString() + ";" + creationDate.toString() + ";" + Integer.toString(age) + ";" + (color == null ? "" : color.toString()) + ";" + (type == null ? "" : type.toString()) + ";" + (character == null ? "" : character.toString()) + ";" + (cave == null ? "" : cave.toString()) + ";";
	}
}

