import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.ResumedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.DisconnectEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DisconnectEventMessages extends ListenerAdapter {

    @Override
    public void onDisconnect(DisconnectEvent event) {
        TextChannel log = Main.jda.getTextChannelById("853879746698018838");
        EmbedBuilder emb = new EmbedBuilder();

        emb.setColor(Color.RED)
                .setTitle("SHARD DISCONNECTED")
                .setFooter("Time: " + event.getTimeDisconnected().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
        if (event.isClosedByServer()) {
            emb.setDescription(
                    "```" + event.getJDA().getShardInfo().getShardString() + "DISCONNECT [SERVER]\nCODE: [" + event.getServiceCloseFrame().getCloseCode() + "]\nREASON: " + event.getServiceCloseFrame().getCloseReason() +
                            "\nSHARD STATUS: " + event.getJDA().getStatus()
                            + "```");
        } else {
            emb.setDescription(
                    "```" + event.getJDA().getShardInfo().getShardString() + "DISCONNECT [CLIENT]\nCODE: [" + event.getClientCloseFrame().getCloseCode() + "]\nREASON: " + event.getClientCloseFrame().getCloseReason() +
                            "\nSHARD STATUS: " + event.getJDA().getStatus() +
                            "```");
        }
        log.sendMessage(emb.build()).queue();
    }

    @Override
    public void onResumed(ResumedEvent event) {
        TextChannel log = Main.jda.getTextChannelById("853879746698018838");
        EmbedBuilder emb = new EmbedBuilder();
        emb.setColor(Color.GREEN)
                .setTitle("SHARD RESUMED")
                .setDescription("```" + event.getJDA().getShardInfo().getShardString() + "RESPONSE NUMBER: " + event.getResponseNumber() + "\nSHARD STATUS: " + event.getJDA().getStatus() + "```");
        log.sendMessage(emb.build()).queue();
    }

    @Override
    public void onReconnected(ReconnectedEvent event) {
        TextChannel log = Main.jda.getTextChannelById("853879746698018838");
        EmbedBuilder emb = new EmbedBuilder();
        emb.setColor(Color.GREEN)
                .setTitle("SHARD RECONNECTED")
                .setDescription("```" + event.getJDA().getShardInfo().getShardString() + "RESPONSE NUMBER: " + event.getResponseNumber() + "\nSHARD STATUS: " + event.getJDA().getStatus() + "```");
        log.sendMessage(emb.build()).queue();
    }
}
