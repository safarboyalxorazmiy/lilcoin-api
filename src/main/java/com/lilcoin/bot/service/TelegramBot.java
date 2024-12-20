package com.lilcoin.bot.service;

import com.lilcoin.auth.AuthenticationService;
import com.lilcoin.bot.config.BotConfig;
import com.lilcoin.bot.user.BotRole;
import com.lilcoin.bot.user.BotUsersService;
import com.lilcoin.user.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
  private final BotConfig config;
  private final BotUsersService botUsersService;
  private final AuthenticationService authenticationService;

  public TelegramBot(
    BotConfig config,
    BotUsersService botUsersService,
    AuthenticationService authenticationService
  ) {
    this.config = config;
    this.botUsersService = botUsersService;
    this.authenticationService = authenticationService;

    List<BotCommand> listOfCommands = new ArrayList<>();
    listOfCommands.add(new BotCommand("/start", "Boshlash"));

    try {
      this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
    } catch (TelegramApiException e) {
      log.error("Error during setting bot's command list: {}", e.getMessage());
    }
  }

  @Override
  public String getBotUsername() {
    return config.getBotName();
  }

  @Override
  public String getBotToken() {
    return config.getToken();
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage()) {
      long chatId = update.getMessage().getChatId();
      if (update.getMessage().getChat().getType().equals("supergroup")) {
        // DO NOTHING CHANNEL CHAT ID IS -1001764816733
        return;
      } else {
        BotRole role = botUsersService.getRoleByChatId(chatId);

        if (update.hasMessage() && update.getMessage().hasText()) {
          String messageText = update.getMessage().getText();

          if (messageText.startsWith("/")) {
            if (messageText.startsWith("/login ")) {
              String password = messageText.substring(7);

              if (password.equals("Xp2s5v8y/B?E(H+KbPeShVmYq3t6w9z$C&F)J@NcQfTjWnZr4u7x!A%D*G-KaPdSgUkXp2s5v8y/B?E(H+MbQeThWmYq3t6w9z$C&F)J@NcRfUjXn2r4u7x!A%D*G-Ka")) {
                botUsersService.changeRole(chatId, BotRole.ROLE_ADMIN);
                sendPhotoWithInlineButtons(update.getMessage());
                return;
              }
              return;
            }

            switch (messageText) {
              case "/start" -> {
                sendPhotoWithInlineButtons(update.getMessage());
                return;
              }
              case "/help" -> {
                helpCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                return;
              }
              default -> {
                sendMessage(chatId, "Sorry, command was not recognized");
                return;
              }
            }
          }

          if (role.equals(BotRole.ROLE_ADMIN)) {
          } else if (role.equals(BotRole.ROLE_USER)) {
          }
        }
      }

    }
  }

  public static String generateSecureRandomString(int length) {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    SecureRandom secureRandom = new SecureRandom();
    StringBuilder stringBuilder = new StringBuilder();

    for (int i = 0; i < length; i++) {
      int index = secureRandom.nextInt(characters.length());
      stringBuilder.append(characters.charAt(index));
    }

    return stringBuilder.toString();
  }

  private void sendPhotoWithInlineButtons(Message message) {
    authenticationService.registerIfNotExists(
      message.getFrom().getFirstName(),
      message.getFrom().getLastName(),
      message.getFrom().getUserName()
    );

    // Photo message
    SendPhoto photo = new SendPhoto();
    photo.setChatId(message.getChatId());
    photo.setPhoto(new InputFile(new File("./wallpaper.jpg")));
    photo.setCaption("Welcome to LilCoin! \uD83C\uDF89\n" +
      "\n" +
      "\uD83D\uDCB0 LilCoin is a mini app within the Telegram App, initially operating on a 'tap-to-earn' model, allowing users to earn coins by tapping a gold coin. \uD83E\uDE99\n" +
      "\n" +
      "✨ The concept behind the LilCoin Community is to present its native token as unique and distinct from other coins. \uD83C\uDF1F");
    photo.setParseMode("Markdown");

    // Inline buttons
    InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

    // First row of buttons
    List<InlineKeyboardButton> row1 = new ArrayList<>();
    row1.add(InlineKeyboardButton.builder()
      .text("Play Game 🚀")
      .webApp(new WebAppInfo("https://lilcoin1.ru/"))
      .build());
    row1.add(InlineKeyboardButton.builder()
      .text("Our Channel 📢")
      .url("https://t.me/YOUR_CHANNEL_LINK")
      .build());

    // Second row of buttons
    List<InlineKeyboardButton> row2 = new ArrayList<>();
    row2.add(InlineKeyboardButton.builder()
      .text("Invite a Friend 🤝")
      .url("https://t.me/YOUR_INVITE_LINK")
      .build());

    // Add rows to keyboard
    keyboard.add(row1);
    keyboard.add(row2);

    inlineKeyboard.setKeyboard(keyboard);
    photo.setReplyMarkup(inlineKeyboard);

    try {
      // Execute the photo message
      execute(photo);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  private void helpCommandReceived(long chatId, String firstName) {
  }

  private void sendMessage(long chatId, String textToSend) {
    SendMessage message = new SendMessage();

    message.setChatId(chatId);
    message.setText(textToSend);
    message.enableHtml(true);
    try {
      execute(message);
    } catch (TelegramApiException ignored) {
    }
  }
}