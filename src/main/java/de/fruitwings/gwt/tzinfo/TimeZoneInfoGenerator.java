/**
 * 
 */
package de.fruitwings.gwt.tzinfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author <a href="mailto:andrei.cojocaru@fruitwings.de">Andrei Cojocaru</a>
 * 
 */
public class TimeZoneInfoGenerator
{

	private static class Generator
	{
		public void generate() throws JSONException
		{
			InputStream stream = getClass()
					.getClassLoader()
					.getResourceAsStream(
							"com/google/gwt/i18n/client/constants/TimeZoneConstants.properties");

			Reader reader = new InputStreamReader(stream);
			Properties properties = new Properties();
			try
			{
				properties.load(reader);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Map<String, String> map = new HashMap<String, String>();
			for (Object key : properties.keySet())
			{
				String methodName = key.toString();
				String stringValue = properties.getProperty(methodName);
				JSONObject jsonValue = new JSONObject(stringValue);
				for (int i = 0; i < jsonValue.length(); i++)
				{
					String tzId = jsonValue.getString("id");
					map.put(tzId, key.toString());
				}
			}
			JSONObject timezoneMappings = new JSONObject();
			for (String tzId : map.keySet())
			{
				timezoneMappings.accumulate(tzId, map.get(tzId));
			}
			System.out.println("timezoneMappings=" + timezoneMappings.toString());
			
			JSONObject timezones = new JSONObject();
			for (String tzId : map.keySet())
			{
				System.out.println(tzId);
				if (tzId.contains("/"))
				{
					String[] areaAndLocation = tzId.split("/", 2);
					String area = areaAndLocation[0]
						, location = areaAndLocation[1];
					timezones.accumulate(area, location);					
				}
			}
			String names[]= JSONObject.getNames(timezones);
			for(int i=0; i<names.length;i++){
				String name = names[i];
				Object value = timezones.get(name);
				if(value == null)
					timezones.remove(name);
			}
			System.out.println("timezones=" + timezones.toString());
			StringBuilder sb = new StringBuilder("Areas:");
			for (Iterator it = timezones.keys(); it.hasNext();)
			{
				sb.append(" ").append(it.next());
			}
			System.out.println(sb.toString());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			new Generator().generate();
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
