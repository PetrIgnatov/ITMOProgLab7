package ru.se.ifmo.prog.lab7.server.cores;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.nio.channels.*;
import java.awt.event.*;
import java.util.Iterator;
import ru.se.ifmo.prog.lab7.commands.*;
import ru.se.ifmo.prog.lab7.cores.*;
import java.util.logging.*;

public class UDPSender {
	private DatagramSocket datagramSocket;
	
	public UDPSender(DatagramSocket datagramSocket) {
		this.datagramSocket = datagramSocket;
	}

	public void send(Response response, InetAddress address, int port, Logger logger) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(response);
			byte[] arr = baos.toByteArray();
			DatagramPacket datagramPacket = new DatagramPacket(arr, arr.length, address, port);
			datagramSocket.send(datagramPacket);
			logger.fine("Sent " + arr.length + " bytes");
		}
		catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}
}
