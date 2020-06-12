package com.github.libgraviton.jenvconf.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnvParser {

  private String prefix = "";

  private boolean doLowerCase = true;
  private boolean convertCamelCase = false;

  final private Pattern pattern = Pattern.compile("[_]{1}[a-z0-9]{1}");

  private Map<String, String> raw = new HashMap<>();
  private final Map<String, String> parsed = new HashMap<>();

  public EnvParser() {
    raw = System.getenv();
  }

  public EnvParser(Map<String, String> values) {
    raw = values;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public void setDoLowerCase(boolean doLowerCase) {
    this.doLowerCase = doLowerCase;
  }

  public void setConvertCamelCase(boolean convertCamelCase) {
    this.convertCamelCase = convertCamelCase;
  }

  public Map<String, String> getParsed() {
    parse(raw);
    return parsed;
  }

  public void parse(Map<String, String> values) {
    for (Map.Entry<String, String> entry : values.entrySet()) {
      if (includeVarName(entry.getKey())) {
        parsed.put(parseVarName(entry.getKey()), entry.getValue());
      }
    }
  }

  private String parseVarName(String varName) {
    // replace "__" with "." and lowercase
    varName = varName.replaceAll("__", ".");

    if (doLowerCase) {
      varName = varName.toLowerCase();
    }

    // replace "_[*]" with * in uppercase to allow camelCase names
    if (convertCamelCase) {
      StringBuilder sb = new StringBuilder();
      int last = 0;
      Matcher matcher = pattern.matcher(varName);
      while (matcher.find()) {
        sb.append(varName.substring(last, matcher.start()));
        sb.append(matcher.group(0).toUpperCase().replace("_", ""));
        last = matcher.end();
      }
      sb.append(varName.substring(last));

      varName = sb.toString();
    }

    return varName;
  }

  private boolean includeVarName(String varName) {
    if (prefix.isEmpty()) {
      return true;
    }

    if (prefix.equals(varName.substring(0, prefix.length()))) {
      return true;
    }

    return false;
  }
}
