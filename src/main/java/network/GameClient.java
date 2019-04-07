package network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import util.Logger;

import java.io.IOException;

public class GameClient extends Listener {
    private Client client;

    public GameClient (String serverIp, int tcpPort, int udpPort) {
        this.client = new Client();
        this.client.start();

        Network.register(this.client);

        client.addListener(new ThreadedListener(new Listener() {
            public void connected (Connection connection) {
                Network.StringMessage message = new Network.StringMessage("hello world");
                client.sendTCP(message);
            }

            public void received (Connection connection, Object object) {
                if (object instanceof Network.StringMessage) {
                    Network.StringMessage response = (Network.StringMessage) object;
                    Logger.info(response.text);
                }
            }
        }));


        try {
            this.client.connect(5000, serverIp, tcpPort, udpPort);
        } catch (IOException e) {
            Logger.error("GameClient : " + e.getMessage());
        }
    }

    public void close () {
        this.client.close();
    }
}
