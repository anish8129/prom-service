/*
 * Copyright 2023 Play Games24x7 Pvt. Ltd. All Rights Reserved
 */

package com.custom.endpoint;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.springframework.boot.actuate.autoconfigure.metrics.export.ConditionalOnEnabledMetricsExport;
import org.springframework.boot.actuate.info.BuildInfoContributor;
import org.springframework.boot.actuate.info.GitInfoContributor;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.actuate.info.JavaInfoContributor;
import org.springframework.boot.actuate.info.OsInfoContributor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

public class EndpointConfig {

    @Bean
    @Conditional(InfoEndpointEnableCondition.class)
    InfoEndpoint infoEndpoint() {

        List<InfoContributor> infoContributors = Arrays.asList(overrideOsInfoContributer(),
                overrideJavaInfoContributor(),
                overrideGitInfoContributor(),
                overrideBuildInfoContributor()
        );

        return new InfoEndpoint(infoContributors);
    }

    private InfoContributor overrideOsInfoContributer() {
        return new OsInfoContributor();
    }

    InfoContributor overrideJavaInfoContributor()  {
        return new JavaInfoContributor();
    }

    InfoContributor overrideGitInfoContributor()  {
        Properties entries = new Properties();
        entries.put("branch", "dev");
        entries.put("commit.id", "233sfkf34w");
        return new GitInfoContributor(new GitProperties(entries));
    }

    InfoContributor overrideBuildInfoContributor()  {
        Properties entries = new Properties();
        entries.put("group", "com.prom.service");
        entries.put("name", "prom-service");
        entries.put("artifact", "prome-from-code");
        return new BuildInfoContributor(new BuildProperties(entries));
    }
}
