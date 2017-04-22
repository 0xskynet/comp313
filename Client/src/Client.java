import java.io.IOException;
import java.net.UnknownHostException;

public class Client {
public static void main (String args[]) throws UnknownHostException, IOException, InterruptedException, Exception     {
    ClientA cl = new ClientA();
    cl.go();
}
}
