package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class HackCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        String[] gender = {"Male", "Female", "Trans", "Other", "Retard"};
        int age = Utility.randomNum(1, 30);
        String[] height = {"4\"10\"", "4\"11\"", "5\"0\"", "5\"1\"", "5\"2\"", "5\"3\"", "5\"4\"", "5\"5\"", "5\"6\"", "5\"7\"", "5\"8\"", "5\"9\"", "5\"10\"", "5\"11\"", "6\"0\"", "6\"1\"", "6\"2\"", "6\"3\"", "6\"4\"", "6\"5\"", "6\"6\"", "6\"7\"", "6\"8\"", "6\"9\"", "6\"10\"", "6\"11\""};
        int weight = Utility.randomNum(60, 300);
        String[] hair_color = {"Black", "Brown", "Blonde", "White", "Gray", "Red"};
        String[] skin_color = {"White", "Pale", "Brown", "Black", "Light-Skin"};
        String[] religion = {"Christian", "Muslim", "Atheist", "Hindu", "Buddhist", "Jewish"};
        String[] sexuality = {"Straight", "Gay", "Homo", "Bi", "Bi-Sexual", "Lesbian", "Pansexual"};
        String[] education = {"High School", "College", "Middle School", "Elementary School", "Pre-School", "Retard never went to school LOL"};
        String[] ethnicity = {"White", "African American", "Asian", "Latino", "Latina", "American", "Mexican", "Korean", "Chinese", "Arab", "Italian", "Puerto Rican", "Non-Hispanic", "Russian", "Canadian", "European", "Indian"};
        String[] occupation = {"Retard has no job LOL", "Certified discord retard", "Janitor", "Police Officer", "Teacher", "Cashier", "Clerk", "Waiter", "Waitress", "Grocery Bagger", "Retailer", "Sales-Person", "Artist", "Singer", "Rapper", "Trapper", "Discord Thug", "Gangster", "Discord Packer", "Mechanic", "Carpenter", "Electrician", "Lawyer", "Doctor", "Programmer", "Software Engineer", "Scientist"};
        String[] salary = {"Retard makes no money LOL", "$" + Utility.randomNum(0, 1000), "<$50,000", "<$75,000", "$100,000", "$125,000", "$150,000", "$175,000", "$200,000+"};
        String[] location = {"Retard lives in his mom's basement LOL", "America", "United States", "Europe", "Poland", "Mexico", "Russia", "Pakistan", "India", "Some random third world country", "Canada", "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
        String[] email = {"@gmail.com", "@yahoo.com", "@hotmail.com", "@outlook.com", "@protonmail.com", "@disposablemail.com", "@aol.com", "@edu.com", "@icloud.com", "@gmx.net", "@yandex.com"};
        String dob = Utility.randomNum(1, 12) + "/" + Utility.randomNum(1, 31) + "/" + Utility.randomNum(1950, 2020);
        String[] name = {"James Smith", "Michael Smith", "Robert Smith", "Maria Garcia", "David Smith", "Maria Rodriguez", "Mary Smith", "Maria Hernandez", "Maria Martinez", "James Johnson", "Catherine Smoaks", "Cindi Emerick", "Trudie Peasley", "Josie Dowler", "Jefferey Amon", "Kyung Kernan", "Lola Barreiro", "Barabara Nuss", "Lien Barmore", "Donnell Kuhlmann", "Geoffrey Torre", "Allan Craft", "Elvira Lucien", "Jeanelle Orem", "Shantelle Lige", "Chassidy Reinhardt", "Adam Delange", "Anabel Rini", "Delbert Kruse", "Celeste Baumeister", "Jon Flanary", "Danette Uhler", "Xochitl Parton", "Derek Hetrick", "Chasity Hedge", "Antonia Gonsoulin", "Tod Kinkead", "Chastity Lazar", "Jazmin Aumick", "Janet Slusser", "Junita Cagle", "Stepanie Blandford", "Lang Schaff", "Kaila Bier", "Ezra Battey", "Bart Maddux", "Shiloh Raulston", "Carrie Kimber", "Zack Polite", "Marni Larson", "Justa Spear"};
        String phone = Utility.randomNum(0, 9) + Utility.randomNum(0, 9) + Utility.randomNum(0, 9) + Utility.randomNum(0, 9) + "-" + Utility.randomNum(0, 9) + Utility.randomNum(0, 9) + Utility.randomNum(0, 9) + "-" + Utility.randomNum(0, 9) + Utility.randomNum(0, 9) + Utility.randomNum(0, 9) + Utility.randomNum(0, 9);
        if (mentionedMembers.isEmpty()) {
            String[] password = {"password", "123", "mypasswordispassword", event.getAuthor().getName() + "iscool123", event.getAuthor().getName() + "isdaddy", "daddy" + event.getAuthor().getName(), "ilovediscord", "i<3discord", "furryporn" + event.getAuthor().getDiscriminator(), "secret", "123456789", "apple49", "redskins32", "princess", "dragon", "password1", "1q2w3e4r", "ilovefurries"};
            String base = String.format("Disclaimer: this is just a joke and none of this information is relevant or accurate and by using this command you acknowledge that this bot is just joking\n\n`Hacking %s... will take %s seconds` \n", event.getAuthor().getName(), new Random().nextInt(60));
            event.getChannel().sendMessage(base).queue(response -> response.editMessage(base + "`Hacking into the mainframe...\n`").queue((response1) -> response.editMessage(base + "`Hacking into the mainframe...\nCaching data...\n`").queueAfter(1, TimeUnit.SECONDS, (response2) -> response.editMessage(base + "`Hacking into the mainframe...\nCaching data...\nCracking SSN information...\n`").queueAfter(1, TimeUnit.SECONDS, (response3) -> response.editMessage(base + "`Hacking into the mainframe...\nCaching data...\nCracking SSN information...\nBruteforcing love life details...\n`").queueAfter(1, TimeUnit.SECONDS, (response4) -> response.editMessage(base + "`Hacking into the mainframe...\nCaching data...\nCracking SSN information...\nBruteforcing love life details...\nFinalizing life-span dox details\n`").queueAfter(1, TimeUnit.SECONDS, (response5) -> response.editMessage(MarkdownUtil.codeblock("\"Disclaimer: this is just a joke and none of this information is relevant or accurate and by using this command you acknowledge that this bot is just joking and we cannot be prosecuted for it.\"\n\nSuccessfully hacked " + event.getAuthor().getName() + "\nName: " + randomChoice(name) + "\nGender: " + randomChoice(gender) + "\nAge: " + age + "\nHeight: " + randomChoice(height) + "\nWeight: " + weight + "\nHair Color: " + randomChoice(hair_color) + "\nSkin Color: " + randomChoice(skin_color) + "\nDOB: " + dob + "\nLocation: " + randomChoice(location) + "\nPhone: " + phone + "\nE-Mail: " + event.getAuthor().getName() + randomChoice(email) + "\nPasswords: " + Arrays.deepToString(randomChoices(password, 3)) + "\nOccupation: " + randomChoice(occupation) + "\nAnnual Salary: " + randomChoice(salary) + "\nEthnicity: " + randomChoice(ethnicity) + "\nReligion: " + randomChoice(religion) + "\nSexuality: " + randomChoice(sexuality) + "\nEducation: " + randomChoice(education))).queueAfter(1, TimeUnit.SECONDS)))))));
        } else {
            User member = mentionedMembers.get(0).getUser();
            String[] password = {"password", "123", "mypasswordispassword", member.getName() + "iscool123", member.getName() + "isdaddy", "daddy" + member.getName(), "ilovediscord", "i<3discord", "furryporn" + member.getDiscriminator(), "secret", "123456789", "apple49", "redskins32", "princess", "dragon", "password1", "1q2w3e4r", "ilovefurries"};
            String base = String.format("Disclaimer: this is just a joke and none of this information is relevent or accurate and by using this command you acknoledge that this bot is just joking\n\n`Hacking %s... will take %s seconds` \n", member.getName(), new Random().nextInt(60));
            event.getChannel().sendMessage(base)
                    .queue(response -> response.editMessage(base + "`Hacking into the mainframe...\n`")
                            .queue((response1) -> response.editMessage(base + "`Hacking into the mainframe...\nCaching data...\n`")
                                    .queueAfter(1, TimeUnit.SECONDS, (response2) -> response.editMessage(base + "`Hacking into the mainframe...\nCaching data...\nCracking SSN information...\n`")
                                            .queueAfter(1, TimeUnit.SECONDS, (response3) -> response.editMessage(base + "`Hacking into the mainframe...\nCaching data...\nCracking SSN information...\nBruteforcing love life details...\n`")
                                                    .queueAfter(1, TimeUnit.SECONDS, (response4) -> response.editMessage(base + "`Hacking into the mainframe...\nCaching data...\nCracking SSN information...\nBruteforcing love life details...\nFinalizing life-span dox details\n`")
                                                            .queueAfter(1, TimeUnit.SECONDS, (response5) -> response.editMessage(MarkdownUtil.codeblock("\"Disclaimer: this is just a joke and none of this information is relevant or accurate and by using this command you acknowledge that this bot is just joking and we cannot be prosecuted for it.\"\n\nSuccessfully hacked " + member.getName() + "\nName: " + randomChoice(name) + "\nGender: " + randomChoice(gender) + "\nAge: " + age + "\nHeight: " + randomChoice(height) + "\nWeight: " + weight + "\nHair Color: " + randomChoice(hair_color) + "\nSkin Color: " + randomChoice(skin_color) + "\nDOB: " + dob + "\nLocation: " + randomChoice(location) + "\nPhone: " + phone + "\nE-Mail: " + member.getName() + randomChoice(email) + "\nPasswords: " + Arrays.deepToString(randomChoices(password, 3)) + "\nOccupation: " + randomChoice(occupation) + "\nAnnual Salary: " + randomChoice(salary) + "\nEthnicity: " + randomChoice(ethnicity) + "\nReligion: " + randomChoice(religion) + "\nSexuality: " + randomChoice(sexuality) + "\nEducation: " + randomChoice(education))).queueAfter(1, TimeUnit.SECONDS)))))));
        }

    }

    private String randomChoice(String[] list) {
        if (list != null && list.length != 0) {
            return list[new Random().nextInt(list.length)];
        } else {
            return null;
        }
    }

    private String[] randomChoices(String[] list, int k) {
        if (list != null && list.length != 0) {
            String[] newstring = new String[k];
            for (int i = 0; i < k; i++) {
                newstring[i] = randomChoice(list);
            }
            return newstring;
        } else {
            return null;
        }
    }


    @Override
    public String getHelp() {
        return "Hacks the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";

    }

    @Override
    public String getInvoke() {
        return "hack";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"lifeinfo", "dox", "haxor", "hax"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
