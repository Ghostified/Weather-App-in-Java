package ProgramFiles;

import java.io.Console;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//Backend
//Retreiving weather data from the API. This logic will fetch the latest weather
// data from external API and return it .The GUI will dispaly the data to the user

public class WeatherApp {
    // featch weather data from  agiven location
    public static JSONObject getWeatherData(String locationName) {
        // get location cordinates using the geo location API
        JSONArray locationData = getLocationData(locationName);

        // extract longitude and latitude data
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        // build API request URL request with location cordinates
        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude +
                "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=Africa%2FCairo";

        System.out.println(urlString);

        try {
            // call api and get response
            HttpURLConnection conn = fetchApiResponse(urlString);

            // Check for response status
            // 200 - menas that aconnection was a success
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Connection Failed to API");
                return null;
            }

            // store resulting json data
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                // read and store scanner
                resultJson.append(scanner.nextLine());
            }

            // close scanner
            scanner.close();

            // close URL connecttion
            conn.disconnect();

            // parse through our data
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            System.out.println(resultJson);

            // retrive hourly data
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

            System.out.println(hourly);

            // we want to get the current hours data
            // getting the index of our current hour
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            System.out.println(index);

            // get temprature
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            // get weather code
            JSONArray weathercode = (JSONArray) hourly.get("weathercode");
            String weatherCondition = convertWeatherCode((long) weathercode.get(index));

            // get humidity
            JSONArray relativeHumidity = (JSONArray) hourly.get("relativeHumidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            // get windspeed
            JSONArray windspeedData = (JSONArray) hourly.get("windspeed_10m");
            double windspeed = (double) windspeedData.get(index);

            // //build the weather json data object that we are going to access on the front
            // end
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windSpeed", windspeed);

            return weatherData;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // retrives geographic cordinates for given locatio name
    public static JSONArray getLocationData(String locationName) {
        // replace any whitespain in location name to + to adhere to API request formart
        locationName = locationName.replaceAll("", "+");

        System.out.println(locationName);

        // build API url with locattion parameter
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName + "&count=10&language=en&format=json";

        System.out.println(urlString);

        try {
            // call api and get a response
            HttpURLConnection conn = fetchApiResponse(urlString);

            // check response status
            // A response of 200 means it went well,successfull connection
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            } else {
                // store the API results
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());

                // read and store the resulting data into the spring bulder
                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }

                // Close scanner
                scanner.close();

                // close URL COnnection
                conn.disconnect();

                // parse the JSON String into a JSON object
                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                // get the list of Location data the Api generated from the location name
                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
                return locationData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Couldn't find the location
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            // atttempt to create a conection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // set request method to get
            conn.setRequestMethod("GET");

            // connect our api
            conn.connect();
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // could not make connection
        return null;
    }

    private static int findIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();

        // iterarate through the time list to and see which matches our current time
        for (int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) {
                // return the index
                return i;
            }
        }

        return 0;
    }

    public static String getCurrentTime() {
        // get current data and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // formart date to be 2023-09-0200:00 (how its read in the Api)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd't'HH':00'");

        // formart and print the curentvdate and time
        String formattedDateTime = currentDateTime.format(formatter);

        return formattedDateTime;

    }

    // convert the weather code into a readable formart
    private static String convertWeatherCode(long weathercode) {
        String weatherCondition = "";
        if (weathercode == 0L) {
            // Clear
            weatherCondition = "Clear";
        } else if (weathercode <= 3 && weathercode > 0L) {
            weatherCondition = "Cloudy";
        } else if ((weathercode >= 51L && weathercode <= 67L)
                || (weathercode >= 80L && weathercode <= 99L)) {

            // rain
            weatherCondition = "Rain";
        } else if (weathercode >= 71L && weathercode <= 77L) {
            // snow
            weatherCondition = "Snow";
        }

        return weatherCondition;

    }
}
