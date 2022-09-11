package me.comu.exeter.events;

import me.comu.exeter.commands.bot.AutoResponseCommand;
import me.comu.exeter.core.Core;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GuildMessageListenerResponderEvent extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (!event.getMessage().getAuthor().isBot() && event.getMessage().getType() != MessageType.INLINE_REPLY) {
            if (!event.getMessage().getMentionedMembers().isEmpty() && event.getMessage().getMentionedMembers().get(0).getId().equalsIgnoreCase(event.getJDA().getSelfUser().getId()) && args.length == 1) {
                MessageEmbed embed = new EmbedBuilder().addField("Current prefix", Core.PREFIX, false).addField("More Information", Core.PREFIX + "help", false).setColor(Core.getInstance().getColorTheme()).build();
                event.getChannel().sendMessageEmbeds(embed).queue();
            }
            if (AutoResponseCommand.responses.containsKey(event.getMessage().getContentRaw()))
                event.getChannel().sendMessage(AutoResponseCommand.responses.get(event.getMessage().getContentRaw())).queue();

        }
        if (event.getMessage().getContentRaw().contains("tiktok.com")) {
             String cookiesStr = "tt_webid_v2=6968990058163848705; tt_webid=6968990058163848705; MONITOR_WEB_ID=6968990058163848705; _ga=GA1.2.1315239861.1622594624; passport_csrf_token=d08d697c6bf8996de200c21ef8be7c8f; passport_csrf_token_default=d08d697c6bf8996de200c21ef8be7c8f; passport_auth_status=4461f506dcbaaf2dc0ca07dbec6da47c%2C; passport_auth_status_ss=4461f506dcbaaf2dc0ca07dbec6da47c%2C; sid_guard=9eed4fa6763dceed51cd86d0ca27202e%7C1622769381%7C5184000%7CTue%2C+03-Aug-2021+01%3A16%3A21+GMT; uid_tt=1b45d3452d8c51a951b9159bffae68918d8fce847ff46c3b762385d076ad9b0d; uid_tt_ss=1b45d3452d8c51a951b9159bffae68918d8fce847ff46c3b762385d076ad9b0d; sid_tt=9eed4fa6763dceed51cd86d0ca27202e; sessionid=9eed4fa6763dceed51cd86d0ca27202e; sessionid_ss=9eed4fa6763dceed51cd86d0ca27202e; store-idc=alisg; store-country-code=cn; odin_tt=f9dfc6847fc4deb428d6560a0eee1ac6da7621e81e4bc66e48ca11d9b94e944ed465bc13dddaeecffe52066ab1f3c54de75bdb9d835ffab67ce46f6f38ec77f6209383977e1a0909c2043fabd8b45ff4; tt_csrf_token=dgOsuRkyci3vAyVbQrti_Ocf; csrf_session_id=a547a648622c44b9bca4ca0511e7540e; s_v_web_id=verify_kpiysnzz_xjFVty6E_o1pv_44hq_B1sf_4WKo9WU4hKnH; xgplayer_device_id=40509682259; xgplayer_user_id=703950030384; bm_sz=CBFF5398784179C9F43AAB6CBA6C039F~YAAQLHTCF/xyJst5AQAAC5P25AzUNANKT9ugqun9O+rFsGco3TICngDvsZZC2mV1sEGkchzYF38i9vYgaTSlxK41SIR17mjIQkw/fnhyeWJBMube6GkI8fv/BD7SzDOvtEXOerSCk8UwzBSaE2kxFC/K2j+Q/On9kOt1CGlkU2H7k0uzQMToSgvLNWEY0qTd; _abck=FAAE0713AB570A9C7357AC429302A0E8~-1~YAAQLHTCF/1yJst5AQAAC5P25AbsU8gINg7Sz+k5U1NJZK21u2/SfwcAiV1Z3rIbusd/Yn0Hao4t3rwmfr0cy/vQFoTPhb1/SJFjwbV2l5SESSp66mrYxnyseQF88EZyKLo0ZdLX+2a/X96ee0H3WVjTnc5bGUPaaTMIbj13s//1Na4GrbuOhjlDZzR3dGHILVw7uX6ies55m74WERBFLYLSPoEsBBl/1hT9Uq9MKpHCYDWkw54/vQ3N+Us9SglLZu5hXmZUlkl1hKCZ36CpyQrV8+vAnxMgNn3d6cjDqWK3vleTBb5vfx5DL602ByWHxOGAxjSdvLiU2f+Len+/iBILH/LEigNrW3xVPUldIjRG8Q/XqBZ+yMYMYPMBIRT2ssYaFG42YBjnlw==~-1~-1~-1; cmpl_token=AgQQAPPdF-RMpYxYfIog4Jk8-tdKu3OK_4PZYP9bfw; ttwid=1%7C_2DQhAb3FNdJO7TeEViwfBMb6tdOOpeS_jCEDDvWbQo%7C1623053481%7Cf9eca73a998726391f6ea768029a455df15d98514f9e2db9eaf76923815f1147; R6kq3TV7=AJW9huV5AQAAaPD3BP-74Sa5ENc8YjPGPmaoV6zprTPuHXNU0e8XxFEXLH7t|1|0|8b7c8d63a889aa2f48e4084853b1f4c3a58ad403; ak_bmsc=8878D47FB0062375E549B53BDBDC797617C2743D3E3B0000ADD4BD60ACBAC402~plxj3R+QWYAe5cIlRvJ6392wOlUtm3KxMZu769G1krI5pnVrzNig4P9mCPsOFc3hXyoagE5JOhfPSkkkjq/J+EjvZ2cSNwCovppDU4S7Klg4jSh+RwxUGMa7k48ab1HLSLcH2P3bSJpWyGQazN/Pen6+MtIS43hIndhCw9zTEELoJ0gEHoxlswYXfc1wGiyNOL2vR3YvQ+nEHuK2aXkwJzSriVYnH9yh8opa+39xVz7BQ=; bm_sv=16A7BCAC64C5999ECEFB83EAABF366C3~6vMhfeLgJoeI/bgNVuRnUopkAuGnrODmT0AvCYqnyXm1gcM0mckyo4UX1NRqb6yfoVgV0vqXbOs/kd3riX9Vv7zT1/2NxE5M2vZPq+1dKt9rULjESkhYSL558gY0AkstIaiPRAQSBQ+O5yonWRJ1Tbe2MH3fmgcfsp3VCiPAKYM=";
            Document doc = null;
            String jsCode = "";
            try {
                doc = Jsoup.connect(args[0])
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36")
                        .referrer("https://www.tiktok.com")
                        .cookies(buildCookiesMap(cookiesStr))
                        .timeout(100000)
                        .get();

            } catch (IOException e) {
                e.printStackTrace();
            }
            Element vidoeInformation = doc.head().getAllElements().get(8);
            Element scriptElement = doc.selectFirst("script[id='__NEXT_DATA__']");
        }

    }

    private static Map<String, String> buildCookiesMap(String cookieStr) {
        Map<String, String> cookieMap = new HashMap<String, String>();
        String[] cookieArr = cookieStr.split("; ");
        Arrays.stream(cookieArr).forEach(cookie -> {
            String[] split = cookie.split("=");
            cookieMap.put(split[0], split[1]);
        });
        return cookieMap;
    }
}