/*
 * This file is part of  Mage Flame.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
 *
 * Mage Flame is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mage Flame is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mage Flame.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.fabric.mageflame.core.util;

import java.util.List;
import java.util.function.Consumer;

import mod.gottsch.fabric.mageflame.MageFlame;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author Mark Gottschling on Nov 13, 2022
 *
 */
public class LangUtil {
	public static final String NEWLINE = "";
	public static final String INDENT2 = "  ";
	public static final String INDENT4 = "    ";
	
	/**
	 * 
	 * @param tooltip
	 * @param consumer
	 */
	public static void appendAdvancedHoverText(String modid, List<Text> tooltip, Consumer<List<Text>> consumer) {
		if (!Screen.hasShiftDown()) {
			tooltip.add(Text.literal(NEWLINE));
			// TODO how do make this call to tooltip generic for any mod because it would require the modid
			tooltip.add(Text.translatable(tooltip(modid, "hold_shift")).formatted(Formatting.GRAY));
			tooltip.add(Text.literal(LangUtil.NEWLINE));
		}
		else {
			consumer.accept(tooltip);
		}
	}

    public static String name(String modid, String prefix, String suffix) {
    	return StringUtils.stripEnd(prefix.trim(), ".")
    			+ "."
    			+ modid
    			+ "."
    			+ StringUtils.stripStart(suffix.trim(), ".");
    }
    
    public static String item(String modid, String suffix) {
    	return name(modid, "item", suffix);
    }
    
    public static String tooltip(String modid, String suffix) {
    	return name(modid, "tooltip", suffix);
    }
    
    public static String screen(String modid, String suffix) {
    	return name(modid, "screen", suffix);
    }

	public static String chat(String modid, String suffix) {
		return name(modid, "chat", suffix);
	}
	
	public static void appendAdvancedHoverText(List<Text> tooltip, Consumer<List<Text>> consumer) {
		LangUtil.appendAdvancedHoverText(MageFlame.MOD_ID, tooltip, consumer);
	}
	
    public static String name(String prefix, String suffix) {
    	return name(MageFlame.MOD_ID, prefix, suffix);
    }
    
    /**
     * 
     * @param suffix
     * @return
     */
    public static String item(String suffix) {
    	return name(MageFlame.MOD_ID, "item", suffix);
    }
    
    public static String tooltip(String suffix) {
    	return name(MageFlame.MOD_ID, "tooltip", suffix);
    }
    
    public static String screen(String suffix) {
    	return name(MageFlame.MOD_ID, "screen", suffix);
    }

	public static String chat(String suffix) {
		return name(MageFlame.MOD_ID, "chat", suffix);
	}
}
