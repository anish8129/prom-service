package com.funnel.configreader;

import com.funnel.GamePlayFunnel;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;

public class YamlPropertySourceFactory {
    public static GamePlayFunnel readFile(Resource resourceFile) {
        try (InputStream inputStream = resourceFile.getInputStream()) {
            final Yaml yaml = new Yaml();
            return yaml.loadAs(inputStream, GamePlayFunnel.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}