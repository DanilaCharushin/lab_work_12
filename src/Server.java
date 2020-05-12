import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

class Server {
    public static LinkedList<NewClient> hostList = new LinkedList<NewClient>();
    public static void main(String[] args) {
        try {
            System.out.println("Server is running");
            int port = 3000;
            ServerSocket ss = new ServerSocket(port);

            Thread acceptor = new Thread(() -> {
                while (true) {
                    try {
                        Socket s = ss.accept();
                        NewClient p = new NewClient(s);
                        p.start();
                        System.out.println("\n-- CONNECTED --");
                        System.out.print("server@birds > ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            acceptor.setDaemon(false);
            acceptor.setName("Server accept");
            acceptor.start();

            Thread console = new Thread(() -> {
                while (true) {
                    System.out.print("server@birds > ");
                    String cmd = new Scanner(System.in).nextLine();
                    if (cmd.equals("hosts")) {
                        System.out.println("---------------- CLIENTS ----------------");
                        for (NewClient host : hostList) {
                            System.out.print("name: " + host.getname());
                            System.out.println(", id: " + host.getid());
                        }
                        System.out.println("-----------------------------------------");
                    } else {
                        System.out.println("Unknown command. Available: hosts");
                    }
                }
            });
            console.setDaemon(true);
            console.setName("Server console");
            console.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

class NewClient extends Thread {
    private static int COUNT = 0;
    private int id;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    private Socket sock;
    private String name;

    public NewClient(Socket s) {
        sock = s;
        id = COUNT++;
        try {
            inStream = new DataInputStream(sock.getInputStream());
            outStream = new DataOutputStream(sock.getOutputStream());
            name = inStream.readUTF();
            Server.hostList.addLast(this);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("NewClient constructor error");
        }

    }

    public int getid() {
        return id;
    }


    public String getname() {
        return name;
    }

    public void sendData(int data) {
        try {
            outStream.writeInt(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendData(String data) {
        try {
            outStream.writeUTF(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            for (NewClient host : Server.hostList) {
                host.sendData(0);
                host.sendData(Server.hostList.size());
                for (NewClient h : Server.hostList) {
                    host.sendData(h.getname());
                }
            }
            boolean exit = false;
            while (!exit) {
                int action = inStream.readInt();
                switch (action) {
                    case 1:
                        String target = inStream.readUTF();
                        int data = inStream.readInt();
                        for (NewClient host : Server.hostList) {
                            if (host.getname().equals(target)) {
                                host.sendData(1);
                                host.sendData(data);
                                break;
                            }
                        }
                        break;
                    case 2:
                        exit = true;
                        break;
                }
            }

            Server.hostList.remove(this);
            inStream.close();
            outStream.close();
            sock.close();
            System.out.println("\n-- DISCONNECTED --");
            System.out.print("server@birds > ");

            for (NewClient host : Server.hostList) {
                host.sendData(0);
                host.sendData(Server.hostList.size());
                for (NewClient h : Server.hostList) {
                    host.sendData(h.getname());
                }
            }

        } catch (Exception e) {
            System.out.println("\n-- FORCE DISCONNECTED --");
            System.out.print("server@birds > ");
            Server.hostList.remove(this);
        }

    }
}
