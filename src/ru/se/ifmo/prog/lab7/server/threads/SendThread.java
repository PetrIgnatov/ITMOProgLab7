package ru.se.ifmo.prog.lab7.server.threads;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.nio.channels.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.RecursiveTask;
import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.commands.*;

public class SendThread extends RecursiveTask<Boolean> {
        private Response response;
	private InetAddress address;
	private int port;
	private DatagramSocket datagramSocket;

        public SendThread(Response response, DatagramSocket datagramSocket, InetAddress address, int port) {
                this.datagramSocket = datagramSocket;
		this.address = address;
		this.port = port;
		this.response = response;
        }
        @Override
        public Boolean compute() {
                try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(response);
			System.out.println("oos written");
                        byte[] arr = baos.toByteArray();
			System.out.println("arr created");
                        DatagramPacket datagramPacket = new DatagramPacket(arr, arr.length, address, port);
			System.out.println("packet created");
                        datagramSocket.send(datagramPacket);
			System.out.println("packet sent");
                        return true;
                }
                catch (Exception e) {
			System.out.println(e.getMessage());
                        return false;
                }
        }
}

