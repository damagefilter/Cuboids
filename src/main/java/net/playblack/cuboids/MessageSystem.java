package net.playblack.cuboids;


import net.canarymod.api.entity.living.humanoid.Player;
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
        super(Config.get().getLang());
    }

    /**
     * Send a red message to the player from the error message pool
     *
     * @param player
     * @param messageKey
     */
    public static void failMessage(Player player, String messageKey) {
        player.notice(ms.localeTranslate(messageKey, locale));
    }

    /**
     * Send a green message to the player from the messages pool
     *
     * @param player
     * @param messageKey
     */
    public static void successMessage(Player player, String messageKey) {
        player.message(ColorManager.LightGreen.concat(ms.localeTranslate(messageKey, locale)));
    }

    /**
     * Send a message from the messages list in yellow to the player
     *
     * @param player
     * @param messageKey
     */
    public static void yellowNote(Player player, String messageKey) {
        player.message(ColorManager.Yellow.concat(ms.localeTranslate(messageKey, locale)));
    }

    /**
     * Send a red custom message
     *
     * @param player
     * @param message
     */
    public static void customFailMessage(Player player, String message) {
        player.notice(message);
    }

    public static void customMessage(Player player, String color, String message) {
        player.message(color.concat(message));
    }

    public static void translateMessage(Player player, String color, String message) {
        player.message(color.concat(ms.localeTranslate(message, locale)));
    }

    public static void translateMessage(Player player, String color, String message, String... args) {
        player.message(color.concat(ms.localeTranslate(message, locale, (Object[])args)));
    }
}
