package net.playblack.cuboids;


import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.mcutils.ColorManager;
import net.visualillusionsent.utils.LocaleHelper;

/**
 * Message System. To send messages to a player or global chat. depending
 *
 * @author Chris
 */
public class MessageSystem extends LocaleHelper {
    private final static MessageSystem ms;
    private static String locale = Config.get().getLang();

    static {
        ms = new MessageSystem();
    }

    private MessageSystem() {
        super();
    }

    /**
     * Send a red message to the player from the error message pool
     *
     * @param player
     * @param messageKey
     */
    public static void failMessage(CPlayer player, String messageKey) {
        player.notify(ms.localeTranslate(messageKey, locale));
    }

    /**
     * Send a green message to the player from the messages pool
     *
     * @param player
     * @param messageKey
     */
    public static void successMessage(CPlayer player, String messageKey) {
        player.sendMessage(ColorManager.LightGreen.concat(ms.localeTranslate(messageKey, locale)));
    }

    /**
     * Send a message from the messages list in yellow to the player
     *
     * @param player
     * @param messageKey
     */
    public static void yellowNote(CPlayer player, String messageKey) {
        player.sendMessage(ColorManager.Yellow.concat(ms.localeTranslate(messageKey, locale)));
    }

    /**
     * Send a red custom message
     *
     * @param player
     * @param message
     */
    public static void customFailMessage(CPlayer player, String message) {
        player.notify(message);
    }

    public static void customMessage(CPlayer player, String color, String message) {
        player.sendMessage(color.concat(message));
    }

    public static void translateMessage(CPlayer player, String color, String message) {
        player.sendMessage(color.concat(ms.localeTranslate(message, locale)));
    }

    public static void translateMessage(CPlayer player, String color, String message, String... args) {
        player.sendMessage(color.concat(ms.localeTranslate(message, locale, args)));
    }
}
