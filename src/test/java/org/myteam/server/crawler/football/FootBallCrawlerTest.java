package org.myteam.server.crawler.football;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.myteam.server.IntegrationTestSupport;

public class FootBallCrawlerTest extends IntegrationTestSupport {

    @DisplayName("epl정보를 크롤링한다.")
    @Test
    void eplTest() {
        Crawler footBallCrawler = new InternationalFootBallCrawler();

        footBallCrawler.crawl();
    }
}
