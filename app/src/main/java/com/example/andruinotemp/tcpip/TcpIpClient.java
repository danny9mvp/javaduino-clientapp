package com.example.andruinotemp.tcpip;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TcpIpClient {
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    public DataInputStream getIn() {
        return in;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public void startTcpIpClient(String ip, int port) throws IOException {
        this.clientSocket = new Socket(ip, port);
        this.out = new DataOutputStream(this.clientSocket.getOutputStream());
        this.in = new DataInputStream(this.clientSocket.getInputStream());
    }

}
