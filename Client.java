package com;

import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class Client {
	public static int size;
	public static boolean ack[];
	public static byte[][] datas;
	public static boolean[] sended;
	public static long[] time;
	public static Pack[] pacotes;
	public static int janela;
	public static int porcentagem;
	public static int filesize;
	public static void main(String[] args) throws IOException {

		Scanner in = new Scanner(System.in);
		janela = in.nextInt();
		porcentagem = in.nextInt();
		byte[] array = Files.readAllBytes(new File("D:\\Users\\vss2\\Downloads\\prot2.zip").toPath());
		filesize=array.length;
		new Thread(t1).start();
		new Thread(t2).start();
		new Thread(t3).start();

	}

	private static Runnable t1 = new Runnable() {
		public void run() {
			try {
				DatagramSocket clientSocket = new DatagramSocket();
				InetAddress IPServer = InetAddress.getByName("localhost");
			
				byte[] array = Files.readAllBytes(new File("D:\\Users\\vss2\\Downloads\\prot2.zip").toPath());
				filesize = array.length;
				
				size = (array.length / 512) + 1;
				ack = new boolean[size];
				sended = new boolean[size];
				time = new long[size];
				datas = thedivision(array);
				int x = 0;

				pacotes = new Pack[size];

				x = 0;

				while (true) {
					new Thread().sleep(5);
					boolean a = olheajanela(x);
					if (x < janela || a) {
						pacotes[x] = new Pack(x, datas[x], "local", "local", array.length);
						time[x]=System.currentTimeMillis();
						byte[] aux = new byte[512];
						byte[] temp = new byte[537];
						int y=0;
						while(y<512) {
						aux[y]= datas[x][y];
						y++;}
						temp = toByteArray(x,aux);
						
						
						DatagramPacket sendPacket = new DatagramPacket(temp, temp.length, IPServer, 5000);
						clientSocket.send(sendPacket);
						sended[x]=true;
						//system.out.println("enviado: " + x);
						x++;
					}

				}
			} catch (Exception e) {
			}

		}
	};
	
	private static boolean olheajanela(int z) {
		boolean a = true;
		int o=0;
		while(o<=janela-z) {
			a = a & ack[o];	
			o++;
		}
		
		
		
		return a;
	}
	private static Runnable t2 = new Runnable() {
		public void run() {
			try {
				//////system.out.println(filesize);
				//////system.out.println("t2");
				int z = 0;
				while (z<(filesize/512)+1) {
					
					new Thread().sleep(75);
					
					if (!ack[z]&sended[z]) {
						
						pacotes[z] = new Pack(z, datas[z], "local", "local", filesize);
						time[z]=System.currentTimeMillis();
						byte[] aux = new byte[512];
						byte[] temp = new byte[537];
						int y=0;
						while(y<512) {
						aux[y]= datas[z][y];
						////system.out.println(z*512+y +": "+datas[z][y]);
						y++;}
						temp = toByteArray(z,aux);
						DatagramSocket clientSocket = new DatagramSocket();
						InetAddress IPServer = InetAddress.getByName("localhost");
					
						
						DatagramPacket sendPacket = new DatagramPacket(temp, temp.length, IPServer, 5000);
						clientSocket.send(sendPacket);
						//system.out.println("reenvio: "+ z);
					} else if (ack[z]) {
						z++;
					}
				}
			} catch (Exception e) {
			}

		}
	};

	private static Runnable t3 = new Runnable() {
		public void run() {
			try {

				DatagramSocket serverSocket = new DatagramSocket(5001);
				byte[] receiveData = new byte[1000];

				while (true) {
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

					serverSocket.receive(receivePacket);
					String received = new String(receivePacket.getData(), 0, receivePacket.getLength());

					ack(received);

				}

			} catch (Exception e) {
			}

		}
	};

	public static void ack(String recebidoServer) {
		Random rndNumber = new Random();
		int a = rndNumber.nextInt(100);
		if (a < porcentagem) {
			ack[Integer.parseInt(recebidoServer)] = true;
			////system.out.println("ack"+ Integer.parseInt(recebidoServer));
			////system.out.println(ack[1]);
		}

	}

	public static byte[] toByteArray(int x, byte aux[]) throws IOException {
		String header = new String(x +","+ filesize+", ");
		byte [] bytes = new byte[537];
		int y=0;
		while(header.length()<25) {
			header=header+"a";
			
		}
        byte[] headerData = header.getBytes();
        while(y<25) {
            bytes[y] = headerData[y];
            y++;
        }
        y=0;
       while(y<512) {
            bytes[25 + y] = aux[y];
            y++;
        }
		return bytes;
	}

	public static byte[][] thedivision(byte[] a){
		int y=0;
		byte[][] datas = new byte[(a.length/512)+1][512];
		////system.out.println("division");
		
		while(y<(a.length/512)+1) {
		int x=0;
		while(y*512+x<filesize && x<512) {
			
			datas[y][x]=a[y*512+x];
			//////system.out.println(y*512+x +": "+datas[y][x]);
			x++;
		}
		y++;
	}
	return datas;	
	}}
