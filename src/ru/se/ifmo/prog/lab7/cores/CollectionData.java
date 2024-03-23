package ru.se.ifmo.prog.lab7.cores;

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

	public CollectionData(String filename) {
		maxId = 0;
		initDate = new java.util.Date();
		this.filename = filename;
		dragons = new LinkedList<Dragon>();
		FileInputStream inputStream = null;
		InputStreamReader reader = null;
		try {
			inputStream = new FileInputStream(filename);
			reader = new InputStreamReader(inputStream);
			int temp;
			String inputDragon = "";
			while ((temp = reader.read()) != -1) {
				if (temp != 10) {
					inputDragon += (char)temp;
				}
				else {
					try {
						String[] splitted = new String[11];
						for (int i = 0; i < 11; ++i) {
							splitted[i] = "";
						}
						int pos = 0;
						for (int i = 0; i < inputDragon.length(); ++i) {
							if (inputDragon.charAt(i) == ';') {
								++pos;
								if ((i == inputDragon.length()-1 && pos != 11) || (i < inputDragon.length()-1 && pos >= 11)) {
									throw new FileInputException("Error! The given file's data doesn't match the required type");
								}
							}
							else
							{
								if ((i == inputDragon.length()-1 && pos != 10) || pos >= 11) {
									throw new FileInputException("Error! The given file's data doesn't match the required type");
								}
								splitted[pos] += inputDragon.charAt(i);
							}
						}
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
								throw new ConvertationException("Error! Unknown color \"" + splitted[6] + "\"");
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
								throw new ConvertationException("Error! Unknown type \"" + splitted[7] + "\"");
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
								throw new ConvertationException("Error! Unknown character \"" + splitted[8] + "\"");
						}
						String format = "EEE MMM dd HH:mm:ss z yyyy";
						SimpleDateFormat formater = new SimpleDateFormat(format, Locale.ENGLISH);
						java.util.Date date = null;
						date = formater.parse(splitted[4]);
						maxId = Math.max(maxId, Integer.parseInt(splitted[0]));
						for (int i = 0; i < dragons.size(); ++i) {
							if (Integer.parseInt(splitted[0]) == dragons.get(i).getId()) {
								throw new ConvertationException("Error! Two dragons from file have the same IDs");
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
					finally {
						inputDragon = "";
					}
				}
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("Error! File \"" + filename + "\" not found or not accessible");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			}
			catch (IOException e) {
				System.out.println("Error! Input/Output exception");
			}
		}
	}
	
	public void save() {
		try {
			FileOutputStream outputStream = new FileOutputStream(filename);
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);
			writer.flush();
			if (dragons.size() > 0) {
				for (int i = 0; i < dragons.size()-1; ++i) {
					writer.write(dragons.get(i).toString()+"\n");
				}
				writer.write(dragons.getLast().toString()+"\n");
			}
			if (writer != null) {
				writer.close();
			}
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("Error! File \"" + filename + "\" not found or not accessible");
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
			String format = "EEE MMM dd HH:mm:ss z yyyy";
			SimpleDateFormat formater = new SimpleDateFormat(format, Locale.FRENCH);
			java.util.Date date = new java.util.Date();
			if (splitted[0] == "") {
				throw new IOException("Error! Name can't be null");
			}
			if (splitted[3] == "") {
				throw new IOException("Error! Age can't be null");
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
			dragon.setDate(new java.util.Date());
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
				dragon.setDate(new java.util.Date());
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

