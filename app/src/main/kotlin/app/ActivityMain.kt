package app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import service.IntentServiceCustom
import service.ServiceCustom
import java.net.HttpURLConnection
import java.net.URL

class ActivityMain : Activity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        top.text = "AsyncServiceREST"

        //Sleep
        SystemClock.sleep(2000)
        Log.d("DebugSleep","Timer: 2")
        content.append("Timer: 2\n\n")
        SystemClock.sleep(2000)
        Log.d("DebugSleep","Timer: 4")
        content.append("Timer: 4\n\n")
        SystemClock.sleep(2000)
        Log.d("DebugSleep","Timer: 6")
        content.append("Timer: 6\n\n")

        //Async

        content.append("Before Async" + "\n\n")

        doAsync {
            SystemClock.sleep(5000)
            uiThread {
                content.append("Timer: 5\n\n")
            }
            SystemClock.sleep(5000)
            uiThread {
                content.append("Timer: 10\n\n")
            }
            SystemClock.sleep(5000)
            uiThread {
                content.append("Timer: 15\n\n")
            }
        }

        content.append("After Async" + "\n\n")

        doAsync {
            //Timer
            var Timer : Int = 0
            while(Timer < 20)
            {
                SystemClock.sleep(1000)
                Timer += 1
                uiThread {
                    content.text = Timer.toString()
                }
            }
        }

        doAsync {
            //HTTP
            val connection = URL("http://www.android.com/").openConnection() as HttpURLConnection
            val data = connection.inputStream.bufferedReader().readText()

            uiThread {
                content.text = ""
                content.append("DATA : " + data + "\n\n")
                content.append("HTTP Done\n\n")
            }
        }

        //REST

        //JSON GET
        doAsync {
            "https://jsonplaceholder.typicode.com/users".httpGet().responseJson { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        uiThread {
                            content.append("FAIL")
                        }
                    }
                    is Result.Success -> {
                        uiThread {
                            val json: Result<Json, FuelError> = result

                            val jsonArray = json.get().array()
                            //content.append("JSONarray: " + jsonArray[1] + "\n\n")

                            val jsonObject1: JSONObject = jsonArray.getJSONObject(1)
                            //content.append("JSONobject: " + jsonObject1.get("email") + "\n\n")

                            val jsonObject2: JSONObject = jsonArray.getJSONObject(1)
                            //content.append("JSONobject: " + jsonObject2.getJSONObject("address") + "\n\n")

                            var Counter = 0

                            content.append("Length: " + jsonArray.length() + "\n")
                            while( Counter < jsonArray.length())
                            {
                                content.append("CompanyName: " + jsonArray.getJSONObject(Counter++).getJSONObject("company").get("name") + "\n")
                            }
                        }
                    }
                }

                uiThread {
                    content.append("JSON Done\n\n")
                }
            }
        }

        //JSON POST
        doAsync {
            val JSONOBJECT : String = "{\"title\": \"foo\",\"body\": \"bar\",\"userId\": 1 }"
            Log.d("JSON","JSON----" + JSONOBJECT)
            "http://jsonplaceholder.typicode.com/posts".httpPost().body(JSONOBJECT).responseJson { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        uiThread {
                            content.append("FAIL")
                        }
                    }
                    is Result.Success -> {
                        uiThread {
                            content.append("Response:\n\n" + response.toString())
                            content.append("Result:\n\n" + result.get().content)
                        }
                    }
                }
            }
        }

        //Service

        val intent1 = Intent(App.instance, IntentServiceCustom::class.java)
        //App.instance.startService(intent1)

        val intent2 = Intent(App.instance, ServiceCustom::class.java)
        //App.instance.startService(intent2)
        //App.instance.stopService(intent2)
    }
}
