import android.util.Log
import com.example.fireapp.mqtt.SSLSocketFactoryHelper
import org.eclipse.paho.mqttv5.client.*
import org.eclipse.paho.mqttv5.common.MqttMessage
import org.eclipse.paho.mqttv5.common.MqttException
import org.eclipse.paho.mqttv5.common.packet.MqttProperties

class MqttClientHelper {

    private val serverUri = "your_serverUri"
    private val clientId = "android_client_" + System.currentTimeMillis()
    private val topic = "your_topic"

    private val mqttClient = MqttAsyncClient(serverUri, clientId, null)

    init {
        connect()
    }

    private fun connect() {
        val options = MqttConnectionOptions().apply {
            isCleanStart = true
            keepAliveInterval = 30
            userName = "your_username"
            password = "your_password".toByteArray()
            socketFactory = SSLSocketFactoryHelper.getSocketFactory()
        }

        mqttClient.setCallback(object : MqttCallback {
            override fun disconnected(disconnectResponse: MqttDisconnectResponse) {
                Log.d("MQTT", "Disconnected: ${disconnectResponse.reasonString}")
            }

            override fun mqttErrorOccurred(exception: MqttException) {
                Log.e("MQTT", "MQTT Error: ${exception.message}")
            }

            override fun messageArrived(topic: String, message: MqttMessage) {
                Log.d("MQTT", "Message arrived [$topic]: ${message.toString()}")
            }

            override fun deliveryComplete(token: IMqttToken) {
                Log.d("MQTT", "Delivery complete")
            }

            override fun connectComplete(reconnect: Boolean, serverURI: String) {
                Log.d("MQTT", "Connected to $serverURI, reconnect = $reconnect")
                subscribe()
            }

            override fun authPacketArrived(reasonCode: Int, properties: MqttProperties) {
                Log.d("MQTT", "Auth packet arrived")
            }
        })

        mqttClient.connect(options)
    }

    private fun subscribe() {
        mqttClient.subscribe(topic, 1)
        Log.d("MQTT", "Subscribed to $topic")
    }
}
