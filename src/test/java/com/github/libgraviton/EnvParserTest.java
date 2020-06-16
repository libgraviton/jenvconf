package com.github.libgraviton;

import com.github.libgraviton.jenvconf.lib.EnvParser;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import junit.framework.TestCase;

/**
 * test our parsing
 */
public class EnvParserTest extends TestCase
{

    public void testParsing()
    {
        Map<String, String> envValues = new HashMap<>();

        //*********** with lowercase and camel case
        envValues.put("ENVNAME", "envname");
        envValues.put("ENV__NAME", "env.name");
        envValues.put("ENV___NAME", "env.Name");

        EnvParser sut = new EnvParser(envValues);
        sut.setConvertCamelCase(true);
        sut.setDoLowerCase(true);
        assertEquals(envValues.size(), sut.getParsed().size());

        for (Entry<String, String> entry : sut.getParsed().entrySet()) {
            assertEquals(entry.getKey(), entry.getValue());
        }

        //*********** no camelcase
        envValues.clear();
        envValues.put("ENVNAME", "envname");
        envValues.put("ENV__NAME", "env.name");
        envValues.put("ENV___NAME", "env._name");

        sut = new EnvParser(envValues);
        assertEquals(envValues.size(), sut.getParsed().size());

        for (Entry<String, String> entry : sut.getParsed().entrySet()) {
            assertEquals(entry.getValue(), entry.getKey());
        }

        //*********** no camelcase, no lowercase
        envValues.clear();
        envValues.put("ENVNAME", "ENVNAME");
        envValues.put("ENV__NAME", "ENV.NAME");
        envValues.put("ENV___NAME", "ENV._NAME");

        sut = new EnvParser(envValues);
        sut.setDoLowerCase(false);
        assertEquals(envValues.size(), sut.getParsed().size());

        for (Entry<String, String> entry : sut.getParsed().entrySet()) {
            assertEquals(entry.getValue(), entry.getKey());
        }

        // with prefix
        envValues.clear();
        envValues.put("ENVNAME", "ENVNAME");
        envValues.put("PREFIX_ENV__NAME", "ENV.NAME");
        envValues.put("PREFIX_ENV___NAME", "ENV._NAME");

        sut = new EnvParser(envValues);
        sut.setPrefix("PREFIX_");
        sut.setDoLowerCase(false);
        assertEquals(envValues.size() - 1, sut.getParsed().size());

        for (Entry<String, String> entry : sut.getParsed().entrySet()) {
            assertEquals(entry.getValue(), entry.getKey());
        }
    }
}
