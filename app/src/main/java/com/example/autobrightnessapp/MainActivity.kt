package com.example.autobrightnessapp
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var lightSensor: Sensor? = null
    private var sensorManager: SensorManager? = null
    private var sensorListener: SensorEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AmbientLightSensorApp()
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager?
        lightSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)

        sensorListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    if (it.sensor.type == Sensor.TYPE_LIGHT) {
                        // Update the ambient light sensor value here
                        updateAmbientLightValue(it.values[0])
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(
            sensorListener,
            lightSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(sensorListener)
    }

    @Composable
    fun AmbientLightSensorApp() {
        var ambientLightValue by remember { mutableStateOf(0f) }

        LaunchedEffect(key1 = ambientLightValue) {
            while (isActive) {
                delay(1000) // Update sensor value every second
                // Get the latest sensor value and update the state
                ambientLightValue = getLatestSensorValue()
            }
        }

        Surface() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Ambient Light Sensor Value: $ambientLightValue")
            }
        }
    }

    private fun updateAmbientLightValue(value: Float) {
        // Update the ambient light sensor value in your Composable
    }

    private fun getLatestSensorValue(): Float {
        // Simulate getting the latest sensor value (replace with actual sensor data)
        return 1000f
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        AmbientLightSensorApp()
    }
}
