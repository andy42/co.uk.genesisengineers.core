package network;

import java.util.ArrayList;

public class NetworkHandler implements NetworkEventInterface {
    private ArrayList<Network.NetworkMessage> networkMessages = new ArrayList<>();

    public void add (Network.NetworkMessage message) {
        networkMessages.add(message);
    }

    public ArrayList<Network.NetworkMessage> getMessages () {
        return networkMessages;
    }

    public void clearMessages () {
        networkMessages.clear();
    }

    @Override
    public void onNetworkEventRecived (Network.NetworkMessage message) {
        this.add(message);
    }
}
