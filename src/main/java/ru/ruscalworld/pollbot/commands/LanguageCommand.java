package ru.ruscalworld.pollbot.commands;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import ru.ruscalworld.pollbot.PollBot;
import ru.ruscalworld.pollbot.core.commands.DefaultCommand;
import ru.ruscalworld.pollbot.core.commands.Response;
import ru.ruscalworld.pollbot.core.settings.GuildSettings;
import ru.ruscalworld.pollbot.util.Ensure;

import java.util.Collections;

public class LanguageCommand extends DefaultCommand {
    public LanguageCommand() {
        super("language", "Changes language of bot responses for your guild");
    }

    @Override
    public Response onExecute(SlashCommandEvent event, GuildSettings settings) throws Exception {
        assert event.getMember() != null;
        Ensure.ifMemberIsAdministrator(settings, event.getMember());
        SelectionMenu.Builder menu = SelectionMenu.create("language");
        menu.setMaxValues(1);
        menu.setMinValues(1);
        PollBot.getInstance().getTranslations().forEach((code, translation) -> {
            String name = translation.getLocalizedName();
            SelectOption option = SelectOption.of(name == null ? "Unknown" : name, code)
                    .withEmoji(Emoji.fromUnicode(translation.getEmoji()))
                    .withDefault(settings.getLanguage().equals(code));
            menu.addOptions(Collections.singletonList(option));
        });

        return Response.selection("Please select your language", menu.build());
    }
}
