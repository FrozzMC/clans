package me.backstabber.epicsetclans.messager;

import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.Vector;

@SuppressWarnings({})
public class JSONMessage {
    private static final BiMap<ChatColor, String> stylesToNames;

    static {
        ImmutableBiMap.Builder<ChatColor, String> builder = ImmutableBiMap.builder();
        for (final ChatColor style : ChatColor.values()) {
            if (!style.isFormat()) {
                continue;
            }

            String styleName;
            switch (style) {
                case MAGIC:
                    styleName = "obfuscated";
                    break;
                case UNDERLINE:
                    styleName = "underlined";
                    break;
                default:
                    styleName = style.name().toLowerCase();
                    break;
            }

            builder.put(style, styleName);
        }
        stylesToNames = builder.build();
    }


    private final List<MessagePart> parts = new ArrayList<>();
    private int centeringStartIndex = -1;

    private JSONMessage(String text) {
        parts.add(new MessagePart(text));
    }


    public static JSONMessage create(String text) {
        return new JSONMessage(text);
    }


    public static JSONMessage create() {
        return create("");
    }

 
    public static void actionbar(String message, Player... players) {
        ReflectionHelper.sendPacket(ReflectionHelper.createActionbarPacket(ChatColor.translateAlternateColorCodes('&', message)), players);
    }

 
    public MessagePart last() {
        if (parts.size() <= 0) {
            throw new ArrayIndexOutOfBoundsException("No MessageParts exist!");
        }
        return parts.get(parts.size() - 1);
    }

 
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();

        obj.addProperty("text", "");

        JsonArray array = new JsonArray();

        parts.stream()
                .map(MessagePart::toJSON)
                .forEach(array::add);

        obj.add("extra", array);

        return obj;
    }


    @Override
    public String toString() {
        return toJSON().toString();
    }

 
    public String toLegacy() {
        StringBuilder output = new StringBuilder();

        parts.stream()
                .map(MessagePart::toLegacy)
                .forEach(output::append);

        return output.toString();
    }

  
    public void send(Player... players) {
        ReflectionHelper.sendPacket(ReflectionHelper.createTextPacket(toString()), players);
    }

 
    public void title(int fadeIn, int stay, int fadeOut, Player... players) {
        ReflectionHelper.sendPacket(ReflectionHelper.createTitleTimesPacket(fadeIn, stay, fadeOut), players);
        ReflectionHelper.sendPacket(ReflectionHelper.createTitlePacket(toString()), players);
    }

 
    public void subtitle(Player... players) {
        ReflectionHelper.sendPacket(ReflectionHelper.createSubtitlePacket(toString()), players);
    }


    public void actionbar(Player... players) {
        actionbar(toLegacy(), players);
    }


    public JSONMessage color(ChatColor color) {
        last().addCode(color);
        return this;
    }
    public JSONMessage code(String color) {
    	while(color.length() != 1) color = color.substring(1).trim();
        color(ChatColor.getByChar(color));
        return this;
    }


    public JSONMessage style(ChatColor style) {
        last().addStyle(style);
        return this;
    }



    public JSONMessage runCommand(String command) {
        last().setOnClick(ClickEvent.runCommand(command));
        return this;
    }


    public JSONMessage suggestCommand(String command) {
        last().setOnClick(ClickEvent.suggestCommand(command));
        return this;
    }


    public JSONMessage openURL(String url) {
        last().setOnClick(ClickEvent.openURL(url));
        return this;
    }


    public JSONMessage changePage(int page) {
        last().setOnClick(ClickEvent.changePage(page));
        return this;
    }

 
    public JSONMessage tooltip(String text) {
        last().setOnHover(HoverEvent.showText(text));
        return this;
    }


    public JSONMessage tooltip(JSONMessage message) {
        last().setOnHover(HoverEvent.showText(message));
        return this;
    }


    public JSONMessage achievement(String id) {
        last().setOnHover(HoverEvent.showAchievement(id));
        return this;
    }


    public JSONMessage then(String text) {
        return then(new MessagePart(text));
    }


    public JSONMessage then(MessagePart nextPart) {
        parts.add(nextPart);
        return this;
    }
    public JSONMessage then(JSONMessage nextPart) {
    	for(MessagePart part:nextPart.parts)
    		parts.add(part);
        return this;
    }


    public JSONMessage bar(int length) {
        return then(Strings.repeat("-", length)).color(ChatColor.DARK_GRAY).style(ChatColor.STRIKETHROUGH);
    }


    public JSONMessage bar() {
        return bar(53);
    }

 
    public JSONMessage newline() {
        return then("\n");
    }


    public JSONMessage beginCenter() {
        // Start with the NEXT message part.
        centeringStartIndex = parts.size();
        return this;
    }


    public JSONMessage endCenter() {
        int current = centeringStartIndex;

        while (current < parts.size()) {
            Vector<MessagePart> currentLine = new Vector<>();
            int totalLineLength = 0;

            for (; ; current++) {
                MessagePart part = current < parts.size() ? parts.get(current) : null;
                String raw = part == null ? null : ChatColor.stripColor(part.toLegacy());

                if (current >= parts.size() || totalLineLength + raw.length() >= 53) {
                    int padding = Math.max(0, (53 - totalLineLength) / 2);
                    currentLine.firstElement().setText(Strings.repeat(" ", padding) + currentLine.firstElement().getText());
                    currentLine.lastElement().setText(currentLine.lastElement().getText() + "\n");
                    currentLine.clear();
                    break;
                }

                totalLineLength += raw.length();
                currentLine.add(part);
            }
        }

        MessagePart last = parts.get(parts.size() - 1);
        last.setText(last.getText().substring(0, last.getText().length() - 1));

        centeringStartIndex = -1;

        return this;
    }


    public static class MessageEvent {

        private String action;
        private Object value;

        public MessageEvent(String action, Object value) {
            this.action = action;
            this.value = value;
        }


        public JsonObject toJSON() {
            JsonObject obj = new JsonObject();
            obj.addProperty("action", action);
            if (value instanceof JsonElement) {
                obj.add("value", (JsonElement) value);
            } else {
                obj.addProperty("value", value.toString());
            }
            return obj;
        }

  
        public String getAction() {
            return action;
        }

     
        public void setAction(String action) {
            this.action = action;
        }

     
        public Object getValue() {
            return value;
        }

  
        public void setValue(Object value) {
            this.value = value;
        }

    }

    public static class ClickEvent {

   
        public static MessageEvent runCommand(String command) {
            return new MessageEvent("run_command", command);
        }

       
        public static MessageEvent suggestCommand(String command) {
            return new MessageEvent("suggest_command", command);
        }

       
        public static MessageEvent openURL(String url) {
            return new MessageEvent("open_url", url);
        }

       
        public static MessageEvent changePage(int page) {
            return new MessageEvent("change_page", page);
        }

    }

    public static class HoverEvent {

       
        public static MessageEvent showText(String text) {
            return new MessageEvent("show_text", text);
        }

        
        public static MessageEvent showText(JSONMessage message) {
            JsonArray arr = new JsonArray();
            arr.add(new JsonPrimitive(""));
            arr.add(message.toJSON());
            return new MessageEvent("show_text", arr);
        }

        
        public static MessageEvent showAchievement(String id) {
            return new MessageEvent("show_achievement", id);
        }

    }

    private static class ReflectionHelper {

        private static final String version;
        private static Class<?> craftPlayer;
        private static Constructor<?> chatComponentText;
        private static Class<?> packetPlayOutChat;
        private static Class<?> packetPlayOutTitle;
        private static Class<?> iChatBaseComponent;
        private static Class<?> titleAction;
        private static Field connection;
        private static MethodHandle GET_HANDLE;
        private static MethodHandle SEND_PACKET;
        private static MethodHandle STRING_TO_CHAT;
        private static Object enumActionTitle;
        private static Object enumActionSubtitle;
        private static Object enumChatMessage;
        private static Object enumActionbarMessage;
        private static boolean SETUP;
        private static int MAJOR_VER = -1;

        static {
            String[] split = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
            version = split[split.length - 1];

            try {
                SETUP = true;

                MAJOR_VER = getVersion();

                craftPlayer = getClass("{obc}.entity.CraftPlayer");
                Method getHandle = craftPlayer.getMethod("getHandle");
                connection = getHandle.getReturnType().getField("playerConnection");
                Method sendPacket = connection.getType().getMethod("sendPacket", getClass("{nms}.Packet"));

                chatComponentText = getClass("{nms}.ChatComponentText").getConstructor(String.class);

                iChatBaseComponent = getClass("{nms}.IChatBaseComponent");

                Method stringToChat;

                if (MAJOR_VER < 8) {
                    stringToChat = getClass("{nms}.ChatSerializer").getMethod("a", String.class);
                } else {
                    stringToChat = getClass("{nms}.IChatBaseComponent$ChatSerializer").getMethod("a", String.class);
                }

                GET_HANDLE = MethodHandles.lookup().unreflect(getHandle);
                SEND_PACKET = MethodHandles.lookup().unreflect(sendPacket);
                STRING_TO_CHAT = MethodHandles.lookup().unreflect(stringToChat);

                packetPlayOutChat = getClass("{nms}.PacketPlayOutChat");
                packetPlayOutTitle = getClass("{nms}.PacketPlayOutTitle");

                titleAction = getClass("{nms}.PacketPlayOutTitle$EnumTitleAction");

                enumActionTitle = titleAction.getField("TITLE").get(null);
                enumActionSubtitle = titleAction.getField("SUBTITLE").get(null);

                if (MAJOR_VER >= 12) {
                    Method getChatMessageType = getClass("{nms}.ChatMessageType").getMethod("a", byte.class);

                    enumChatMessage = getChatMessageType.invoke(null, (byte) 1);
                    enumActionbarMessage = getChatMessageType.invoke(null, (byte) 2);
                }

            } catch (Exception e) {
                e.printStackTrace();
                SETUP = false;
            }

        }

        static void sendPacket(Object packet, Player... players) {
            if (!SETUP) {
                throw new IllegalStateException("ReflectionHelper is not set up!");
            }
            if (packet == null) {
                return;
            }

            for (Player player : players) {
                try {
                    SEND_PACKET.bindTo(connection.get(GET_HANDLE.bindTo(player).invoke())).invoke(packet);
                } catch (Throwable e) {
                    System.err.println("Failed to send packet");
                    e.printStackTrace();
                }
            }

        }

        private static void setType(Object object, byte type) {
            if (MAJOR_VER < 12) {
                set("b", object, type);
                return;
            }
            if(MAJOR_VER >15) {
            	set("c", object, new UUID(0L, 0L));
            }
            switch (type) {
                case 1:
                    set("b", object, enumChatMessage);
                    break;
                case 2:
                    set("b", object, enumActionbarMessage);
                    break;
                default:
                    throw new IllegalArgumentException("type must be 1 or 2");
            }
        }

        static Object createActionbarPacket(String message) {
            if (!SETUP) {
                throw new IllegalStateException("ReflectionHelper is not set up!");
            }
            Object packet = createTextPacket(message);
            setType(packet, (byte) 2);
            return packet;
        }

		@SuppressWarnings("deprecation")
		static Object createTextPacket(String message) {
            if (!SETUP) {
                throw new IllegalStateException("ReflectionHelper is not set up!");
            }
            try {
                Object packet = packetPlayOutChat.newInstance();
                set("a", packet, fromJson(message));
                setType(packet, (byte) 1);
                return packet;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        static Object createTitlePacket(String message) {
            if (!SETUP) {
                throw new IllegalStateException("ReflectionHelper is not set up!");
            }
            try {
                return packetPlayOutTitle.getConstructor(titleAction, iChatBaseComponent).newInstance(enumActionTitle, fromJson(message));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        static Object createSubtitlePacket(String message) {
            if (!SETUP) {
                throw new IllegalStateException("ReflectionHelper is not set up!");
            }
            try {
                return packetPlayOutTitle.getConstructor(titleAction, iChatBaseComponent).newInstance(enumActionSubtitle, fromJson(message));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        static Object createTitleTimesPacket(int fadeIn, int stay, int fadeOut) {
            if (!SETUP) {
                throw new IllegalStateException("ReflectionHelper is not set up!");
            }
            try {
                return packetPlayOutTitle.getConstructor(int.class, int.class, int.class).newInstance(fadeIn, stay, fadeOut);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        
        static Object componentText(String message) {
            if (!SETUP) {
                throw new IllegalStateException("ReflectionHelper is not set up!");
            }
            try {
                return chatComponentText.newInstance(message);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        
        static Object fromJson(String json) {
            if (!SETUP) {
                throw new IllegalStateException("ReflectionHelper is not set up!");
            }
            if (!json.trim().startsWith("{")) {
                return componentText(json);
            }

            try {
                return STRING_TO_CHAT.invoke(json);
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }

        }

      
        static Class<?> getClass(String path) throws ClassNotFoundException {
            if (!SETUP) {
                throw new IllegalStateException("ReflectionHelper is not set up!");
            }
            return Class.forName(path.replace("{nms}", "net.minecraft.server." + version).replace("{obc}", "org.bukkit.craftbukkit." + version));
        }

    
        static void set(String field, Object obj, Object value) {
            try {
                Field f = obj.getClass().getDeclaredField(field);
                f.setAccessible(true);
                f.set(obj, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static int getVersion() {
            if (!SETUP) {
                throw new IllegalStateException("ReflectionHelper is not set up!");
            }
            try {
                return Integer.parseInt(version.split("_")[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 10;
            }

        }

    }

   
    public class MessagePart {

        private final List<ChatColor> styles = new ArrayList<>();
        private MessageEvent onClick;
        private MessageEvent onHover;
        private ChatColor color;
        private String text;

        public MessagePart(String text) {
            this.text = text == null ? "null" : text;
        }

       
        public JsonObject toJSON() {
            Objects.requireNonNull(text);

            JsonObject obj = new JsonObject();
            obj.addProperty("text", text);

            if (color != null) {
                obj.addProperty("color", color.name().toLowerCase());
            }

            for (ChatColor style : styles) {
                obj.addProperty(stylesToNames.get(style), true);
            }

            if (onClick != null) {
                obj.add("clickEvent", onClick.toJSON());
            }

            if (onHover != null) {
                obj.add("hoverEvent", onHover.toJSON());
            }

            return obj;

        }

        
        public String toLegacy() {
            StringBuilder output = new StringBuilder();
            if (color != null) {
                output.append(color.toString());
            }
            styles.stream()
                    .map(ChatColor::toString)
                    .forEach(output::append);

            return output.append(text).toString();
        }

        
        public MessageEvent getOnClick() {
            return onClick;
        }

        
        public void setOnClick(MessageEvent onClick) {
            this.onClick = onClick;
        }

       
        public MessageEvent getOnHover() {
            return onHover;
        }

        
        public void setOnHover(MessageEvent onHover) {
            this.onHover = onHover;
        }

        
        public ChatColor getColor() {
            return color;
        }

        public void addCode(ChatColor code) {
        	if (code == null) {
                throw new IllegalArgumentException("Code cannot be null!");
            }
        	if (color.isColor()) {
                this.color = code;
            }
            if (code.isFormat()) {
                styles.add(code);
            }
            throw new IllegalArgumentException(code.name() + " is not a style or color.");
        }
        public void setColor(ChatColor color) {
            if (!color.isColor()) {
                throw new IllegalArgumentException(color.name() + " is not a color!");
            }
            this.color = color;
        }

        
        public List<ChatColor> getStyles() {
            return styles;
        }

        
        public void addStyle(ChatColor style) {
            if (style == null) {
                throw new IllegalArgumentException("Style cannot be null!");
            }
            if (!style.isFormat()) {
                throw new IllegalArgumentException(color.name() + " is not a style!");
            }
            styles.add(style);
        }

        
        public String getText() {
            return text;
        }

        
        public void setText(String text) {
            this.text = text;
        }

    }

}