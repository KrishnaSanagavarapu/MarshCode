
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TemperaturePrediction {

	public static void main(String[] args) throws IOException {
		String input;
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				System.out.println("Temperature Prediction");
				System.out.println("Enter the USA 5 digit Zip code of your City or exit to quit");

				input = scanner.nextLine();
				int zipcode = 19406;
				zipcode = Integer.parseInt(input);
				getDetails(zipcode);

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("System.in was closed; exiting");
		}

	}

	public static void getDetails(int zipcode) {

		try {

			URL url = new URL(
					"https://weather.api.here.com/weather/1.0/report.json?app_id=EHgD6obcDg9Tj0y2f67F&app_code=jCU8zup6jic7R641IGVr2A&product=forecast_hourly&metric=false&zipcode="
							+ zipcode);

// Get the input stream through URL Connection
			URLConnection con = url.openConnection();

			InputStream is = con.getInputStream();

			BufferedReader ar = new BufferedReader(new InputStreamReader(is));

			String line = null;

			line = ar.readLine();

			JSONObject obj = null;
			obj = new JSONObject(line);

			JSONArray arr = null;
			arr = obj.getJSONObject("hourlyForecasts").getJSONObject("forecastLocation").getJSONArray("forecast");
			String cityName = obj.getJSONObject("hourlyForecasts").getJSONObject("forecastLocation").getString("city");

			ArrayList<Double> temp = new ArrayList<Double>();
			ArrayList<String> dateTime = new ArrayList<String>();
			int i = 0;
			String today = arr.getJSONObject(i).getString("weekday");
			for (i = 1; today.equals(arr.getJSONObject(i).getString("weekday")); i++) {
			}
			for (int j = 0; j < 24; i++, j++) {
				temp.add(Double.parseDouble((arr.getJSONObject(i).getString("temperature"))));
				dateTime.add(arr.getJSONObject(i).getString("localTime"));
			}
			int min_index = temp.indexOf(Collections.min(temp));
			String time = dateTime.get(min_index).substring(0, 2);
			String date = dateTime.get(min_index).substring(2, dateTime.get(min_index).length());
			date = date.substring(0, 2) + "-" + date.substring(2, 4) + "-" + date.substring(4, 8);
			String meridiem = "am";
//Display time in meridiem
			if (Integer.parseInt(time) > 11) {
				if (time != "12")
					time = Integer.toString((Integer.parseInt(time) - 12));
				meridiem = "pm";
			} else {
				if (time == "00")
					time = "12";
			}
//Finds the coolest hour of tomorrow based on the input.

			System.out.println(
					"coolest hour in " + cityName + " tomorrow on " + date + " would be at " + time + " " + meridiem);
			System.out.println("and the temperature would be " + temp.get(min_index) + " F\n");

		} catch (IOException e) {

			e.printStackTrace();
			System.out.println("City not found, please enter a valid usa Zip Code\n");
			return;
		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println(" Invalid API format ");
		}

	}
}
