package com.github.libgraviton.jenvconf.cli;

import com.github.libgraviton.jenvconf.lib.EnvParser;
import java.util.Map.Entry;

import com.github.libgraviton.jenvconf.util.ShellEscaper;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(name = "jenvconf", mixinStandardHelpOptions = true,
    description = "Takes the environment variables and outputs matching CLI parameter properties (-Dname=val)")
class App implements Callable<Integer> {

    public enum OutputFormat {
        cli, properties
    }

    @Option(names = {"-p", "--prefix"}, description = "ENV name var prefix to search for.")
    private String prefix = "";

    @Option(names = "--lowerCase", negatable = true, description = "Should ENV names be lowercased first?")
    private boolean doLowerCase;

    @Option(names = "--camelCase", negatable = true, description = "Should \"_\" replaced with the following letter uppercased to change the name to camelCase?")
    private boolean doCamelCase;

    @Option(names = "--output-format", description = "Output format to print, possible values: ${COMPLETION-CANDIDATES}")
    private OutputFormat outputFormat = OutputFormat.properties;

    public static void main(String[] args)
    {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        EnvParser parser = new EnvParser();
        parser.setPrefix(prefix);
        parser.setDoLowerCase(doLowerCase);
        parser.setConvertCamelCase(doCamelCase);

        if (outputFormat.equals(OutputFormat.cli)) {
            for (Entry<String, String> entry : parser.getParsed().entrySet()) {
                System.out.printf("-D%s='%s' ", entry.getKey(), ShellEscaper.SHELL_ESCAPE.escape(entry.getValue()));
            }
        }
        
        return 0;
    }
}