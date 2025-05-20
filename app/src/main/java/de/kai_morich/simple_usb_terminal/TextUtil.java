package de.kai_morich.simple_usb_terminal;

import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import java.nio.charset.StandardCharsets;

final class TextUtil {

    @ColorInt static int caretBackground = 0xff666666;

    final static String newline_crlf = "\r\n";
    final static String newline_lf = "\n";

    // Convert string to UTF-8 encoded byte array
    static byte[] fromHexString(final CharSequence s) {
        return s.toString().getBytes(StandardCharsets.UTF_8);
    }

    // Convert UTF-8 encoded byte array to string
    static String toHexString(final byte[] buf) {
        return new String(buf, StandardCharsets.UTF_8);
    }

    // Convert a substring of UTF-8 byte array to string
    static String toHexString(final byte[] buf, int begin, int end) {
        return new String(buf, begin, end - begin, StandardCharsets.UTF_8);
    }

    // Append UTF-8 byte array as string to a StringBuilder
    static void toHexString(StringBuilder sb, final byte[] buf) {
        sb.append(new String(buf, StandardCharsets.UTF_8));
    }

    // Append UTF-8 substring to a StringBuilder
    static void toHexString(StringBuilder sb, final byte[] buf, int begin, int end) {
        sb.append(new String(buf, begin, end - begin, StandardCharsets.UTF_8));
    }

    /**
     * Use caret notation to represent non-printable control characters
     */
    static CharSequence toCaretString(CharSequence s, boolean keepNewline) {
        return toCaretString(s, keepNewline, s.length());
    }

    static CharSequence toCaretString(CharSequence s, boolean keepNewline, int length) {
        boolean found = false;
        for (int pos = 0; pos < length; pos++) {
            if (s.charAt(pos) < 32 && (!keepNewline || s.charAt(pos) != '\n')) {
                found = true;
                break;
            }
        }
        if (!found)
            return s;

        SpannableStringBuilder sb = new SpannableStringBuilder();
        for (int pos = 0; pos < length; pos++) {
            if (s.charAt(pos) < 32 && (!keepNewline || s.charAt(pos) != '\n')) {
                sb.append('^');
                sb.append((char) (s.charAt(pos) + 64));
                sb.setSpan(new BackgroundColorSpan(caretBackground), sb.length() - 2, sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                sb.append(s.charAt(pos));
            }
        }
        return sb;
    }

    /**
     * TextWatcher for handling UTF-8 input transformations
     */
    static class HexWatcher implements TextWatcher {

        private final TextView view;
        private final StringBuilder sb = new StringBuilder();
        private boolean self = false;
        private boolean enabled = false;

        HexWatcher(TextView view) {
            this.view = view;
        }

        void enable(boolean enable) {
            if (enable) {
                view.setInputType(InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                view.setInputType(InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            }
            enabled = enable;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No action needed
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // No action needed
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!enabled || self)
                return;

            sb.delete(0, sb.length());
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c >= 32) { // Include all printable characters
                    sb.append(c);
                }
            }

            final String s2 = sb.toString();

            if (!s2.equals(s.toString())) {
                self = true;
                s.replace(0, s.length(), s2);
                self = false;
            }
        }
    }
}
