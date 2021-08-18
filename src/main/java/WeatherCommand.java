import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.text.WordUtils;
import org.openweathermap.api.model.currentweather.CurrentWeather;
import org.openweathermap.api.query.*;
import org.openweathermap.api.query.currentweather.CurrentWeatherOneLocationQuery;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;


public class WeatherCommand extends ListenerAdapter {


    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            return;
        }

        if (Main.mapDisable.containsKey(event.getGuild().getId()) && !event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s+");
        String prefix = "?";


        if(Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }

         if (args[0].equalsIgnoreCase(prefix + "weather") || args[0].equalsIgnoreCase(prefix + "forcast")) {

            try {
                if (args.length > 1) {
                    String location = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));

                    CurrentWeatherOneLocationQuery currentWeatherOneLocationQuery = QueryBuilderPicker.pick()
                            .currentWeather()                   // get current weather
                            .oneLocation()                      // for one location
                            .byCityName(location)              // for city
                            //.countryCode("UA")                  // not needed?
                            .type(Type.ACCURATE)                // with Accurate search
                            .language(Language.ENGLISH)         // in English language
                            .responseFormat(ResponseFormat.JSON)// with JSON response format
                            .unitFormat(UnitFormat.IMPERIAL)
                            .build();
                    CurrentWeather weather = Main.weatherHelper.getCurrentWeather(currentWeatherOneLocationQuery);

                    EmbedBuilder emb = new EmbedBuilder();

                    emb.setTitle("-Current " + weather.getCityName() + ", " + weather.getSystemParameters().getCountry() + " Weather-")
                    .setDescription("Current Forecast: **" + WordUtils.capitalize(weather.getWeather().get(0).getDescription()) + "**")
                    .addField("\uD83C\uDF21 Temperature:",
                            "Current: **" + weather.getMainParameters().getTemperature() + "°**" +
                                    "\nMin: **" + weather.getMainParameters().getMinimumTemperature() + "°**" +
                                    "\nMax: **" + weather.getMainParameters().getMaximumTemperature() + "°**"
                                    , false)

                    .addField("\uD83C\uDF26 Weather:",
                            "Cloud Cover: **" + weather.getClouds().toString().substring(11, weather.getClouds().toString().length() - 1) + "%**" +
                                   // "\nVisibility: **" + weather.getVisibility() + " meters**" +
                                    "\nHumidity: **" + weather.getMainParameters().getHumidity() + "%**" +
                                    "\nPressure: **" + weather.getMainParameters().getPressure() + "**", false)

                    .addField("\uD83D\uDCA8 Wind:",
                            "Wind Speed: **" + weather.getWind().getSpeed() + "mph**" +
                                    "\nWind Bearing: **" + weather.getWind().getDirection().toString().substring(21, weather.getWind().getDirection().toString().length() - 1) + "**"

                            , false)

                    .addField("\uD83C\uDF07 Sunrise/Sunset:",
                            "*(Military Time)*\nSunrise: **" + weather.getSystemParameters().getSunrise().getHours() + ":" + weather.getSystemParameters().getSunrise().getMinutes() + "**" +
                                    "\nSunset **" + weather.getSystemParameters().getSunset().getHours() + ":" + weather.getSystemParameters().getSunset().getMinutes() + "**"
                            , false)

                    .addField("Source:",
                            "[`OpenWeatherMap.org`](https://openweathermap.org/)"
                            , false)


                    .setFooter("Weather for " + weather.getCityName() + " requested by " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), "https://pbs.twimg.com/profile_images/1173919481082580992/f95OeyEW.jpg")
                    .setColor(Color.yellow);
                    event.getMessage().reply(emb.build()).queue();

                } else {
                    event.getMessage().reply("Please use this format ``?weather <location>``.\nExample: ``?weather Seattle``").queue();
                }

            } catch (Exception e) {
                event.getMessage().reply("Hmm something went wrong... make sure you entered the city correctly or try again later.").queue();
                e.printStackTrace();
            }
        }
    }
}




