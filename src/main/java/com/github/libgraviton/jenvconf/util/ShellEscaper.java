package com.github.libgraviton.jenvconf.util;

import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;

public class ShellEscaper {
    public static final Escaper SHELL_ESCAPE;
    static {
        final Escapers.Builder builder = Escapers.builder();
        builder.addEscape('\'', "'\"'\"'");
        SHELL_ESCAPE = builder.build();
    }
}
