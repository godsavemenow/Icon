package com;
import java.io.File;
import java.io.FileOutputStream;
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
public class Server {

public static byte [] header;
public static int seqn;
public static boolean []check= new boolean[1000000]; 
public static int y=0;
public static void main(String[] args) throws IOException  {
	
	byte [] arquivo = new byte[215349947];
	DatagramSocket serverSocket = new DatagramSocket(5000);
	byte[] receiveData = new byte[537];
	byte[] sendData;
	InetAddress clientIP;
	int port;
	long contatempo = System.currentTimeMillis();
	
	while(true) {
		DatagramPacket receivePacket= new DatagramPacket(receiveData, receiveData.length);
	
	
	serverSocket.receive(receivePacket);
	
	String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
	//System.out.println(received);
	header(receivePacket.getData());
	String [] cabecalho=received.split(",");
	
	if(Integer.parseInt(cabecalho[0])%100==0) {System.out.println(cabecalho[0]);}
	sendData = (cabecalho[0]).getBytes();
	clientIP = receivePacket.getAddress();
	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientIP, 5001);
	serverSocket.send(sendPacket);
	if(!check[ Integer.parseInt(cabecalho[0])]) {
	arquivo=data(receivePacket.getData(), Integer.parseInt(cabecalho[0]), Integer.parseInt(cabecalho[1]), arquivo);
	
	seqn=(Integer.parseInt(cabecalho[1])/512) + 1;
	
	 
	
	//arquivo = load(Integer.parseInt(cabecalho[0]), data, arquivo);
	port = receivePacket.getPort();
	
	
	byte []file = new byte[Integer.parseInt(cabecalho[1])];
	file = transfer(Integer.parseInt(cabecalho[1]), arquivo);
	check[Integer.parseInt(cabecalho[0])]=true;
	fileconvert(seqn, file);
	//print(file);	
	}
	}

		
}

static void print(byte[] file) {
	int re=0;
	while(re<file.length) {
		
		
		//System.out.println(re +": "+file[re]);
		re++;
	}
	
	
}
static byte[] transfer(int filesize, byte[] arq) {
	byte []file = new byte[filesize];  
	int t=0;
	while(t<filesize) {
		file[t]=arq[t];
		t++;
	}
	
	
	return file;
}
static void fileconvert(int seqn, byte [] file) throws IOException {
	int x=0;
	boolean a=true;
	while(x<seqn) {
	a = a & check[x];	
	//System.out.println(check[x]);
	x++;
	}
	
	if(a) {
		print(file);
		try (FileOutputStream stream = new FileOutputStream("D:\\Users\\vss2\\Documents\\arquivoGerado.zip")) {
		    stream.write(file);
		}
	}
	
}

public static void header(byte[]areceived) {
	header=new byte[25];
	int x = 0;

	while(x<25) {
	header[x]=areceived[x];
	x++;
	}

}


public static byte[] data(byte[]aareceived, int seq, int filesize, byte[]arq) {
	byte[] temps=new byte[filesize];
	
	if(seq==4) {
		//System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
	}
	
	
	int x = 0;
	
	while(x<512&y<filesize) {
	arq[seq*512+x]=aareceived[25+x];
	y++;
	//System.out.println(y +": "+temps[x]);
	x++;
	
	}
return arq;
}




	public static byte[] load(int seq, byte[] dat, byte[]arq) {
		
		int x = seq*512;
		int y = 0;
		while(x< header[1] & y<512) {
		arq[x]=dat[y];
		x++;
		y++;
		}
return arq;
	}

}
