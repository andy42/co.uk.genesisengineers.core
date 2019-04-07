package network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import util.Logger;

import java.util.ArrayList;
import java.util.List;

public class GameServer extends Listener {
    private Server server;
    private List<ClientConnection> connectionList = new ArrayList<>();
    private NetworkEventInterface listener;

    public GameServer (NetworkEventInterface listener, int tcpPort, int udpPort) {
        this.listener = listener;
        this.server = new Server() {
            @Override
            protected Connection newConnection () {
                return new ClientConnection();
            }
        };

        Network.register(server);
        server.start();
        try {
            server.bind(tcpPort, udpPort);
        } catch (Exception e) {
            Logger.error("GameServer cound not bind ports : " + e.getMessage());
        }

        server.addListener(this);
    }

    @Override
    public void received (Connection c, Object object) {
        ClientConnection clientConnection = (ClientConnection) c;

        if (object instanceof Network.Register) {
            Network.Register register = (Network.Register) object;
            clientConnection.clientUUID = register.clientUUID;
            connectionList.add(clientConnection);
        }
        if (object instanceof Network.NetworkMessage) {
            listener.onNetworkEventRecived((Network.NetworkMessage) object);
        }
    }

    @Override
    public void disconnected (Connection c) {
        ClientConnection clientConnection = (ClientConnection) c;
        connectionList.remove(clientConnection);
    }

    public void sendEventToClients (Network.NetworkMessage message) {
        server.sendToAllTCP(message);
    }

    static class ClientConnection extends Connection {
        ClientConnection () {
            super();
        }

        String clientUUID;
    }

    public void close () {
        this.server.close();
    }
}
