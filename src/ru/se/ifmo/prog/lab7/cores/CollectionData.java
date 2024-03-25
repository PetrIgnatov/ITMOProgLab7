package ru.se.ifmo.prog.lab7.cores;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import ru.se.ifmo.prog.lab7.classes.*;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Collections;
import ru.se.ifmo.prog.lab7.exceptions.*;

public class CollectionData {
	private LinkedList<Dragon> dragons;
	private String filename;
	private java.util.Date initDate;
	private int maxId;
	
	public CollectionData() {
		maxId = 0;
		initDate = new java.util.Date();
		dragons = new LinkedList<Dragon>();
	}

	public CollectionData(LinkedList<String> data) {
		maxId = 0;
		initDate = new java.util.Date();
		dragons = new LinkedList<Dragon>();
		try {
			for (String dragon : data) {
				try {
					String[] splitted = dragon.split(";");
					Color col = null;
					switch(splitted[6]) {
						case "GREEN":
							col = Color.GREEN;
							break;
						case "YELLOW":
							col = Color.YELLOW;
							break;
						case "ORANGE":
							col = Color.ORANGE;
							break;
						case "WHITE":
							col = Color.WHITE;
							break;	
						case "":
							col = null;
							break;
						default:
							throw new ConvertationException("Ошибка! Неизвестный цвет \"" + splitted[6] + "\"");
					}
					DragonType type = null; 
					switch(splitted[7]) {
						case "WATER":
							type = DragonType.WATER;
							break;
						case "UNDERGROUND":
							type = DragonType.UNDERGROUND;	
							break;
						case "AIR":
							type = DragonType.AIR;
							break;
						case "":
							type = null;
							break;
						default:
							throw new ConvertationException("Ошибка! Неизвестный тип \"" + splitted[7] + "\"");
					}
					DragonCharacter character = null;
					switch(splitted[8]) {
						case "EVIL":
							character = DragonCharacter.EVIL;
							break;
						case "GOOD":
							character = DragonCharacter.GOOD;	
							break;
						case "CHAOTIC":
							character = DragonCharacter.CHAOTIC;
							break;
						case "FICKLE":
							character = DragonCharacter.FICKLE;	
							break;
						case "CHAOTIC_EVIL":
							character = DragonCharacter.CHAOTIC_EVIL;
							break;
						case "":
							character = null;
							break;
						default:
							throw new ConvertationException("Ошибка! Неизвестный характер \"" + splitted[8] + "\"");
					}
					String format = "yyyy-MM-dd HH:mm:ss";
					DateTimeFormatter formater = DateTimeFormatter.ofPattern(format);
					LocalDateTime date = LocalDateTime.parse(splitted[4], formater);
					maxId = Math.max(maxId, Integer.parseInt(splitted[0]));
					for (int i = 0; i < dragons.size(); ++i) {
						if (Integer.parseInt(splitted[0]) == dragons.get(i).getId()) {
							throw new ConvertationException("Ошибка! У двух драконов одинаковый ID");
						}
					}
					dragons.add(new Dragon(
								Integer.parseInt(splitted[0]),
								splitted[1] == "" ? null : splitted[1],
								splitted[2] == "" ? null : Integer.parseInt(splitted[2]),
								splitted[3] == "" ? null : Float.parseFloat(splitted[3]),
								date,
								Integer.parseInt(splitted[5]),
								col,type,character,
								splitted[9] == "" ? null : Double.parseDouble(splitted[9]),
								splitted[10] == "" ? null : Float.parseFloat(splitted[10])));
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void addDragons(LinkedList<String> data) {
		try {
			for (String dragon : data) {
				try {
					String[] splitted = dragon.split(";");
					Color col = null;
					switch(splitted[6]) {
						case "GREEN":
							col = Color.GREEN;
							break;
						case "YELLOW":
							col = Color.YELLOW;
							break;
						case "ORANGE":
							col = Color.ORANGE;
							break;
						case "WHITE":
							col = Color.WHITE;
							break;	
						case "":
							col = null;
							break;
						default:
							throw new ConvertationException("Ошибка! Неизвестный цвет \"" + splitted[6] + "\"");
					}
					DragonType type = null; 
					switch(splitted[7]) {
						case "WATER":
							type = DragonType.WATER;
							break;
						case "UNDERGROUND":
							type = DragonType.UNDERGROUND;	
							break;
						case "AIR":
							type = DragonType.AIR;
							break;
						case "":
							type = null;
							break;
						default:
							throw new ConvertationException("Ошибка! Неизвестный тип \"" + splitted[7] + "\"");
					}
					DragonCharacter character = null;
					switch(splitted[8]) {
						case "EVIL":
							character = DragonCharacter.EVIL;
							break;
						case "GOOD":
							character = DragonCharacter.GOOD;	
							break;
						case "CHAOTIC":
							character = DragonCharacter.CHAOTIC;
							break;
						case "FICKLE":
							character = DragonCharacter.FICKLE;	
							break;
						case "CHAOTIC_EVIL":
							character = DragonCharacter.CHAOTIC_EVIL;
							break;
						case "":
							character = null;
							break;
						default:
							throw new ConvertationException("Ошибка! Неизвестный характер \"" + splitted[8] + "\"");
					}
					String format = "yyyy-MM-dd HH:mm:ss";
					DateTimeFormatter formater = DateTimeFormatter.ofPattern(format);
					LocalDateTime date = LocalDateTime.parse(splitted[4], formater);
					maxId = Math.max(maxId, Integer.parseInt(splitted[0]));
					for (int i = 0; i < dragons.size(); ++i) {
						if (Integer.parseInt(splitted[0]) == dragons.get(i).getId()) {
							throw new ConvertationException("Ошибка! У двух драконов одинаковый ID");
						}
					}
					dragons.add(new Dragon(
								Integer.parseInt(splitted[0]),
								splitted[1] == "" ? null : splitted[1],
								splitted[2] == "" ? null : Integer.parseInt(splitted[2]),
								splitted[3] == "" ? null : Float.parseFloat(splitted[3]),
								date,
								Integer.parseInt(splitted[5]),
								col,type,character,
								splitted[9] == "" ? null : Double.parseDouble(splitted[9]),
								splitted[10] == "" ? null : Float.parseFloat(splitted[10])));
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public LinkedList<Dragon> getDragons() {
		return dragons;
	}

	public void clear() {
		dragons.clear();
	}

	public void sort() {
		Collections.sort(dragons);
	}
	
	public Dragon createDragon(String[] splitted, int id) {
		try {
			Color col = null;
			switch(splitted[4]) {
				case "GREEN":
					col = Color.GREEN;
					break;
				case "YELLOW":
					col = Color.YELLOW;
					break;
				case "ORANGE":
					col = Color.ORANGE;
					break;
				case "WHITE":
					col = Color.WHITE;
					break;	
				case "":
					col = null;
					break;
				default:
					throw new ConvertationException("Error! Unknown color \"" + splitted[4] + "\"");
			}
			DragonType type = null; 
			switch(splitted[5]) {
				case "WATER":
					type = DragonType.WATER;
					break;
				case "UNDERGROUND":
					type = DragonType.UNDERGROUND;	
					break;
				case "AIR":
					type = DragonType.AIR;
					break;
				case "":
					type = null;
					break;
				default:
					throw new ConvertationException("Error! Unknown type \"" + splitted[5] + "\"");
			}
			DragonCharacter character = null;
			switch(splitted[6]) {
				case "EVIL":
					character = DragonCharacter.EVIL;
					break;
				case "GOOD":
					character = DragonCharacter.GOOD;	
					break;
				case "CHAOTIC":
					character = DragonCharacter.CHAOTIC;
					break;
				case "FICKLE":
					character = DragonCharacter.FICKLE;	
					break;
				case "CHAOTIC_EVIL":
					character = DragonCharacter.CHAOTIC_EVIL;
					break;
				case "":
					character = null;
					break;
				default:
					throw new ConvertationException("Error! Unknown character \"" + splitted[6] + "\"");
			}
			String format = "yyyy-MM-dd HH:mm:ss";
			DateTimeFormatter formater = DateTimeFormatter.ofPattern(format);
			LocalDateTime date = LocalDateTime.parse(splitted[4], formater);
			if (splitted[0] == "") {
				throw new IOException("Ошибка! Имя не может быть равен null");
			}
			if (splitted[3] == "") {
				throw new IOException("Ошибка! Возраст не может быть равен null");
			}
			return new Dragon(
					id,
					splitted[0] == "" ? null : splitted[0],
					splitted[1] == "" ? null : Integer.parseInt(splitted[1]),
					splitted[2] == "" ? null : Float.parseFloat(splitted[2]),
					date,
					splitted[3] == "" ? null : Integer.parseInt(splitted[3]),
					col,type,character,
					splitted[7] == "" ? null : Double.parseDouble(splitted[7]),
					splitted[8] == "" ? null : Float.parseFloat(splitted[8]));
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public void add(String[] splitted) {
		Dragon newDragon = createDragon(splitted, maxId+1);
		if (newDragon != null) {
			dragons.add(newDragon);
			++maxId;
		}
	}
	
	public void add(Dragon dragon) {
		if (dragon != null) {
			dragon.setId(++maxId);
			dragon.setDate(LocalDateTime.now());
			dragons.add(dragon);
		}
	}

	public int findById(int id) {
		for (int i = 0; i < dragons.size(); ++i) {
			if (dragons.get(i).getId() == id) {
				return i;
			}
		}
		return -1;
	}

	public void update(String[] parameters, int ind, int id) {
		Dragon newDragon = createDragon(parameters, id);
		if (newDragon != null) {
			dragons.set(ind, newDragon);
		}
	}

	public void update(Dragon dragon, int ind, int id) {
		try {
			if (!dragon.equals(null)) {
				dragon.setId(id);
				dragon.setDate(LocalDateTime.now());
				System.out.println(dragon.toString());
				dragons.set(ind, dragon);
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void remove(int id) {
		try {
			boolean found = false;
			for (int i = 0; i < dragons.size(); ++i) {
				if (dragons.get(i).getId() == id) {
					dragons.remove(i);
					found = true;
					break;
				}
			}
			if (!found) {
				throw new IOException("Error! Dragon with ID " + id + " not found");
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void removeIndex(int index) {
		try {
			if (index < 0 || index >= dragons.size()) {
				throw new IOException("Error! Invalid index");
			}
			dragons.remove(index);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public int sumAge() {
		int sum = 0;
		for (Dragon dragon : dragons) {
			sum += dragon.getAge();
		}
		return sum;
	}

	public String dragonsString() {
		String s = "";
		for (Dragon dragon : dragons) {
			s += dragon.toString() + "\n";
		}
		return s;
	}
	@Override
	public String toString() {
		return "LinkedList<Dragon>;" + initDate.toString() + ";" + Integer.toString(dragons.size()) + " elements;";
	}
}

