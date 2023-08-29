package me.backstabber.epicsetclans.utils;

import org.bukkit.inventory.ItemStack;

import me.backstabber.epicsetclans.utils.NBT.SafeNBT;
import org.jetbrains.annotations.NotNull;

public class CommonUtils {
	//returns item's name from itemstack
	public static @NotNull String name(final @NotNull ItemStack item) {
		if (!item.hasItemMeta()) return "";
		
		final var meta = item.getItemMeta();
		assert meta != null;
		if(meta.hasDisplayName()) return item.getItemMeta().getDisplayName();
		
		final var builder = new StringBuilder();
		
		String name = "";
		for(final var s : item.getType().name().split("_")) {
			name = builder.append(name)
				.append(s.substring(0,1).toUpperCase())
				.append(s.substring(1).toLowerCase())
				.append(" ")
				.toString();
		}
		
		return name.substring(0,name.length()-1);
	}
	
	public static @NotNull String tag(final @NotNull ItemStack item) {
		String tag = "";
		
		final var clansTagString = SafeNBT.get(item).getString("clans-string");
		if (clansTagString != null) {
			tag = clansTagString;
		}
		
		return tag;
		
	}
	
	public static @NotNull ItemStack setTag(@NotNull ItemStack item, final @NotNull String tag) {
		final var nbtTag = SafeNBT.get(item);
		
		nbtTag.setString("clans-string", tag);
		item = nbtTag.apply(item);
		
		return item;
	}
	
	//used to evaluate arithematic expressions & get a double value (this is too usefull)
	//expressions like (2*100)/50 etc
	public static double evaluateString(String str) {
		return new Object() {
			int pos = -1, ch;

			void nextChar() {
				ch = (++pos < str.length()) ? str.charAt(pos) : -1;
			}
			
			boolean eat(int charToEat) {
				while (ch == ' ') nextChar();
				if (ch == charToEat) {
					nextChar();
					return true;
				}
				
				return false;
			}

			double parse() {
				nextChar();
				double x = parseExpression();
				if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
				return x;
			}

			// Grammar:
			// expression = term | expression `+` term | expression `-` term
			// term = factor | term `*` factor | term `/` factor
			// factor = `+` factor | `-` factor | `(` expression `)`
			//        | number | functionName factor | factor `^` factor

			double parseExpression() {
				double x = parseTerm();
				for (;;) {
					if      (eat('+')) x += parseTerm(); // addition
					else if (eat('-')) x -= parseTerm(); // subtraction
					else return x;
				}
			}

			double parseTerm() {
				double x = parseFactor();
				for (;;) {
					if      (eat('*')) x *= parseFactor(); // multiplication
					else if (eat('/')) x /= parseFactor(); // division
					else return x;
				}
			}

			double parseFactor() {
				if (eat('+')) return parseFactor(); // unary plus
				if (eat('-')) return -parseFactor(); // unary minus

				double x;
				int startPos = this.pos;
				if (eat('(')) { // parentheses
					x = parseExpression();
					eat(')');
				} else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
					while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
					x = Double.parseDouble(str.substring(startPos, this.pos));
				} else if (ch >= 'a' && ch <= 'z') { // functions
					while (ch >= 'a' && ch <= 'z') nextChar();
					String func = str.substring(startPos, this.pos);
					x = parseFactor();
					if (func.equals("sqrt")) x = Math.sqrt(x);
					else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
					else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
					else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
					else throw new RuntimeException("Unknown function: " + func);
				} else {
					throw new RuntimeException("Unexpected: " + (char)ch);
				}

				if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

				return x;
			}
		}.parse();
	}
}
