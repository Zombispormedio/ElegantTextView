package com.zombispormedio.elegantrichtext;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.annimon.stream.function.BiFunction;
import com.annimon.stream.function.Function;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by xavierserrano on 23/11/16.
 */

public class ElegantUtils {

    public interface WithStyle<T> {

        T bold();

        T foregroundColor(String hex);

        T backgroundColor(String hex);

        T textCenter();
    }


    public static SpannableString bold(SpannableString sequence) {
        sequence.setSpan(new StyleSpan(Typeface.BOLD), 0, sequence.length(), 0);
        return sequence;
    }

    public static SpannableString foregroundColor(SpannableString sequence, List<String> color) {
        int hex = Color.parseColor(color.get(0));

        sequence.setSpan(new ForegroundColorSpan(hex), 0, sequence.length(), 0);
        return sequence;
    }

    public static SpannableString backgroundColor(SpannableString sequence, List<String> color) {
        int hex = Color.parseColor(color.get(0));

        sequence.setSpan(new BackgroundColorSpan(hex), 0, sequence.length(), 0);
        return sequence;
    }

    public static SpannableString centerText(SpannableString sequence) {
        sequence.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, sequence.length(), 0);
        return sequence;
    }


    public static class AbstractFilter<F> {
        private F fn;

        public AbstractFilter(F fn) {
            this.fn = fn;
        }

        public F getFunction() {
            return fn;
        }
    }

    public static class Filter extends AbstractFilter<Function<SpannableString, SpannableString>> {

        public Filter(Function<SpannableString, SpannableString> fn) {
            super(fn);
        }


    }

    public static class ArgsFilter extends AbstractFilter<BiFunction<SpannableString, List<String>, SpannableString>> {

        public ArgsFilter(BiFunction<SpannableString, List<String>, SpannableString> fn) {
            super(fn);
        }
    }

    public static class StyleCompound implements WithStyle<StyleCompound>{
        private HashSet<String> styles;

        private HashSet<String> globalStyles;

        private LinkedHashMap<String, AbstractFilter> customStyles;

        public StyleCompound() {
            styles = new HashSet<>();
            globalStyles = new HashSet<>();
            customStyles = new LinkedHashMap<>();
        }

        public StyleCompound addStyles(String... keys) {
            Collections.addAll(styles, keys);
            return this;
        }

        public StyleCompound addGlobalStyles(String... keys) {
            Collections.addAll(globalStyles, keys);
            return this;
        }

        public StyleCompound addCustomStyle(String key, Function<SpannableString, SpannableString> fn) {
            customStyles.put(key, new Filter(fn));
            return this;
        }

        public StyleCompound addCustomStyle(String key, BiFunction<SpannableString, List<String>, SpannableString> fn) {
            customStyles.put(key, new ArgsFilter(fn));
            return this;
        }

        public HashSet<String> getStyles() {
            return styles;
        }

        public HashSet<String> getGlobalStyles() {
            return globalStyles;
        }

        public LinkedHashMap<String, AbstractFilter> getCustomStyles() {
            return customStyles;
        }

        public StyleCompound bold(){
            addStyles(ElegantStyleManager.Style.BOLD);
            return this;
        }

        public StyleCompound foregroundColor(String hex){
            addStyles(ElegantStyleManager.Style.foregroundColor(hex));
            return this;
        }

        public StyleCompound backgroundColor(String hex){
            addStyles(ElegantStyleManager.Style.backgroundColor(hex));
            return this;
        }

        public StyleCompound textCenter(){
            addStyles(ElegantStyleManager.Style.TEXT_CENTER);
            return this;
        }

        public StyleCompound boldGlobal(){
            addGlobalStyles(ElegantStyleManager.Style.BOLD);
            return this;
        }

        public StyleCompound foregroundColorGlobal(String hex){
            addGlobalStyles(ElegantStyleManager.Style.foregroundColor(hex));
            return this;
        }

        public StyleCompound backgroundColorGlobal(String hex){
            addGlobalStyles(ElegantStyleManager.Style.backgroundColor(hex));
            return this;
        }

        public StyleCompound textCenterGlobal(){
            addGlobalStyles(ElegantStyleManager.Style.TEXT_CENTER);
            return this;
        }
    }
}
