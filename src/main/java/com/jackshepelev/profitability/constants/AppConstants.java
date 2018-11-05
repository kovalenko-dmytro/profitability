package com.jackshepelev.profitability.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.charset.Charset;
import java.util.Locale;

public enum AppConstants {

    DEFAULT_LOCALE(Locale.forLanguageTag("ru")),
    DEFAULT_ENCODING(Charset.forName("UTF-8"));

    AppConstants(Locale locale) {
        this.locale = locale;
    }

    AppConstants(Charset charset) {
        this.charset = charset;
    }

    AppConstants(long value) {
        this.value = value;
    }

    private Locale locale;
    private Charset charset;
    private long value;

    public Locale getLocale() {
        return locale;
    }

    public Charset getCharset() {
        return charset;
    }

    public long getValue() {
        return value;
    }
}
