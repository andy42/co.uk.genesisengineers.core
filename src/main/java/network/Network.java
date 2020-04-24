package network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import util.Vector2Df;

public class Network {
    static public final int port = 54555;

    static public void register (EndPoint endpoint) {
        Kryo kryo = endpoint.getKryo();

        kryo.register(StringMessage.class);
        kryo.register(LoginMessage.class);
        kryo.register(Register.class);
        kryo.register(NewEntity.class);
        kryo.register(Vector2Df.class);
    }

    static public class StringMessage implements NetworkMessage {
        public String text;

        public StringMessage () {

        }

        public StringMessage (String text) {
            this.text = text;
        }
    }

    static public class LoginMessage implements NetworkMessage {
        public int entityId;
    }

    static public class Register implements NetworkMessage {
        public String clientUUID;
    }

    static public class NewEntity implements NetworkMessage {
        public int entityId;
        public int protoTypeId;
    }

    public interface NetworkMessage {

    }
}
